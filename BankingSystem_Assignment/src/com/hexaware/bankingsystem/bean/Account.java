package com.hexaware.bankingsystem.bean;

public abstract class Account {
    private static long lastAccNo = 1000;

    private long accountNumber;
    private String accountType;
    private float accountBalance;
    private Customer customer; //association

    public Account() {
    	
    }

    public Account(Customer customer, String accountType, float accountBalance) {
        this.accountNumber = ++lastAccNo;
        this.customer = customer;
        this.accountType = accountType;
        this.accountBalance = accountBalance;
    }


    public static long getLastAccNo() {
		return lastAccNo;
	}

	public static void setLastAccNo(long lastAccNo) {
		Account.lastAccNo = lastAccNo;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public float getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(float accountBalance) {
		this.accountBalance = accountBalance;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public void deposit(float amount) {
        if (amount > 0) {
            this.accountBalance += amount;
        }
    }

    public abstract boolean withdraw(float amount);

    public abstract float calculateInterest();

	@Override
	public String toString() {
		return "Account [accountNumber=" + accountNumber + ", accountType=" + accountType + ", accountBalance="
				+ accountBalance + ", customer=" + customer + "]";
	}

    
}