package com.hexaware.bankingsystem.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.hexaware.bankingsystem.bean.*;
import com.hexaware.bankingsystem.exceptions.*;

public class BankRepositoryImpldao implements IBankRepositorydao {

	@Override
	public void createAccount(Customer customer, long accNo, String accType, float balance) {
	    try (Connection conn = DBUtil.getDBConnection()) {

	        boolean customerExists = isCustomerExists(customer.getCustomerId());

	        if (!customerExists) {
	            String insertCustomer = "INSERT INTO customers (customer_id, first_name, last_name, DOB, email, phone_number, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
	            PreparedStatement pstmt1 = conn.prepareStatement(insertCustomer);
	            pstmt1.setInt(1, customer.getCustomerId());
	            pstmt1.setString(2, customer.getFirstName());
	            pstmt1.setString(3, customer.getLastName());
	            pstmt1.setDate(4, new java.sql.Date(new Date().getTime())); 
	            pstmt1.setString(5, customer.getEmail());
	            pstmt1.setString(6, customer.getPhoneNumber());
	            pstmt1.setString(7, customer.getAddress());
	            pstmt1.executeUpdate();
	        } else {
	            System.out.println("Customer already exists. Skipping customer insert...");
	        }

	        // Insert account
	        String insertAccount = "INSERT INTO accounts (account_id, customer_id, account_type, balance) VALUES (?, ?, ?, ?)";
	        PreparedStatement pstmt2 = conn.prepareStatement(insertAccount);
	        pstmt2.setLong(1, accNo);
	        pstmt2.setInt(2, customer.getCustomerId());
	        pstmt2.setString(3, accType.toLowerCase());
	        pstmt2.setFloat(4, balance);
	        pstmt2.executeUpdate();

	    } catch (SQLIntegrityConstraintViolationException e) {
	        System.err.println("Account already exists");
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public float getAccountBalance(long accNo) throws InvalidAccountException {
		float balance = 0;

		try {
			Connection conn = DBUtil.getDBConnection();

			String query = "SELECT balance FROM accounts WHERE account_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, accNo);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				balance = rs.getFloat("balance");
			} else {
				throw new InvalidAccountException("Account not found: " + accNo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return balance;
	}

	@Override
	public float deposit(long accNo, float amount) throws InvalidAccountException {
		float newBalance = 0;

		try {
			Connection conn = DBUtil.getDBConnection();
			conn.setAutoCommit(false);

			float current = getAccountBalance(accNo);
			newBalance = current + amount;

			String update = "UPDATE accounts SET balance = ? WHERE account_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(update);
			pstmt.setFloat(1, newBalance);
			pstmt.setLong(2, accNo);
			pstmt.executeUpdate();

			addTransaction(conn, accNo, "deposit", amount, "Amount deposited");

			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return newBalance;
	}

	@Override
	public float withdraw(long accNo, float amount)
			throws InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException {

		float newBalance = 0;

		try {
			Connection conn = DBUtil.getDBConnection();
			conn.setAutoCommit(false);

			Account acc = getAccountDetails(accNo);

			if (acc == null) {
				throw new InvalidAccountException("Account not found: " + accNo);
			}

			if (!acc.withdraw(amount)) {
				if (acc instanceof CurrentAccount) {
					throw new OverDraftLimitExceededException("Overdraft limit exceeded for account: " + accNo);
				} else {
					throw new InsufficientFundException("Insufficient balance in account: " + accNo);
				}
			}

			newBalance = acc.getAccountBalance();

			String update = "UPDATE accounts SET balance = ? WHERE account_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(update);
			pstmt.setFloat(1, newBalance);
			pstmt.setLong(2, accNo);
			pstmt.executeUpdate();

			addTransaction(conn, accNo, "withdrawal", amount, "Amount withdrawn");

			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return newBalance;
	}

	@Override
	public void transfer(long fromAccNo, long toAccNo, float amount)
			throws InvalidAccountException, InsufficientFundException, OverDraftLimitExceededException {

		try {
			Connection conn = DBUtil.getDBConnection();
			conn.setAutoCommit(false);

			Account fromAcc = getAccountDetails(fromAccNo);
			Account toAcc = getAccountDetails(toAccNo);

			if (fromAcc == null || toAcc == null) {
				throw new InvalidAccountException("One or both accounts are invalid.");
			}

			withdraw(fromAccNo, amount);
			deposit(toAccNo, amount);

			addTransaction(conn, fromAccNo, "transfer", amount, "Transferred to " + toAccNo);
			addTransaction(conn, toAccNo, "deposit", amount, "Received from " + fromAccNo);

			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Account getAccountDetails(long accNo) throws InvalidAccountException {
		Account acc = null;

		try {
			Connection conn = DBUtil.getDBConnection();

			String query = "SELECT a.*, c.* FROM accounts a JOIN customers c ON a.customer_id = c.customer_id WHERE account_id = ?";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, accNo);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				Customer customer = extractCustomer(rs);
				String accType = rs.getString("account_type");
				float balance = rs.getFloat("balance");

				switch (accType.toLowerCase()) {
					case "savings":
						acc = new SavingsAccount(customer, balance);
						break;
					case "current":
						acc = new CurrentAccount(customer, balance);
						break;
					case "zero_balance":
						acc = new ZeroBalanceAccount(customer);
						break;
				}
				if (acc != null) acc.setAccountNumber(accNo);
			} else {
				throw new InvalidAccountException("Account not found: " + accNo);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return acc;
	}

	@Override
	public List<Account> listAccounts() {
		List<Account> list = new ArrayList<>();

		try {
			Connection conn = DBUtil.getDBConnection();

			String query = "SELECT a.*, c.* FROM accounts a JOIN customers c ON a.customer_id = c.customer_id ORDER BY a.account_id ASC";

			PreparedStatement pstmt = conn.prepareStatement(query);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Customer customer = extractCustomer(rs);
				String accType = rs.getString("account_type");
				float balance = rs.getFloat("balance");
				long accNo = rs.getLong("account_id");

				Account acc = null;

				switch (accType.toLowerCase()) {
					case "savings":
						acc = new SavingsAccount(customer, balance);
						break;
					case "current":
						acc = new CurrentAccount(customer, balance);
						break;
					case "zero_balance":
						acc = new ZeroBalanceAccount(customer);
						break;
				}

				if (acc != null) {
					acc.setAccountNumber(accNo);
					list.add(acc);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	@Override
	public List<Transaction> getTransactions(long accNo, Date fromDate, Date toDate) {
		List<Transaction> txns = new ArrayList<>();

		try {
			Connection conn = DBUtil.getDBConnection();

			String query = "SELECT * FROM transactions WHERE account_id = ? AND transaction_date BETWEEN ? AND ?";
			PreparedStatement pstmt = conn.prepareStatement(query);

			pstmt.setLong(1, accNo);
			pstmt.setTimestamp(2, new Timestamp(fromDate.getTime()));
			pstmt.setTimestamp(3, new Timestamp(toDate.getTime()));

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Transaction t1 = new Transaction(
						rs.getLong("account_id"),
						rs.getString("transaction_type"),
						rs.getTimestamp("transaction_date"),
						rs.getString("transaction_type"),
						rs.getFloat("amount")
				);

				txns.add(t1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return txns;
	}

	
	@Override
	public void calculateInterest() {
	    List<Account> allAccounts = listAccounts();

	    List<Long> noIntAcc = new ArrayList<>();

	    for (Account acc : allAccounts) {
	        float interest = acc.calculateInterest();

	        if (interest > 0) {
	            try {
	                deposit(acc.getAccountNumber(), interest);
	                System.out.println("Interest of Rs %.2f" + interest + " added to " + acc.getAccountType() + " account #" + acc.getAccountNumber());
	            } catch (InvalidAccountException e) {
	                System.out.println("Failed to add interest to account #" + acc.getAccountNumber());
	            }
	        } else {
	            noIntAcc.add(acc.getAccountNumber());
	        }
	    }

	    if (!noIntAcc.isEmpty()) {
	        System.out.print("No interest for accounts ");
	        for (int i = 0; i < noIntAcc.size(); i++) {
	            System.out.print("#" + noIntAcc.get(i));
	            if (i != noIntAcc.size() - 1) {
	                System.out.print(", ");
	            }
	        }
	        System.out.println();
	    }
	}



	
	//this is for withdraw(),deposit(),transfer()
	private void addTransaction(Connection conn, long accNo, String type, float amount, String desc) throws SQLException {
		String txnSQL = "INSERT INTO transactions (account_id, transaction_type, amount, transaction_date) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

		PreparedStatement pstmt = conn.prepareStatement(txnSQL);

		pstmt.setLong(1, accNo);
		pstmt.setString(2, type);
		pstmt.setFloat(3, amount);

		pstmt.executeUpdate();
	}

	//this is for listAccountss() and getAccountDetails()
	private Customer extractCustomer(ResultSet rs) throws SQLException {
		return new Customer(
				rs.getInt("customer_id"),
				rs.getString("first_name"),
				rs.getString("last_name"),
				rs.getString("email"),
				rs.getString("phone_number"),
				rs.getString("address")
		);
	}
	
	public boolean isCustomerExists(int customerId) {
	    try (Connection conn = DBUtil.getDBConnection();
	         PreparedStatement pstmt = conn.prepareStatement("SELECT 1 FROM customers WHERE customer_id = ?")) {
	        pstmt.setInt(1, customerId);
	        ResultSet rs = pstmt.executeQuery();
	        return rs.next();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false;
	}

}
