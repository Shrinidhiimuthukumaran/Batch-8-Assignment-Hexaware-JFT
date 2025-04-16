package com.hexaware.bankingsystem.app;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Scanner;

import com.hexaware.bankingsystem.bean.Account;
import com.hexaware.bankingsystem.bean.Customer;
import com.hexaware.bankingsystem.exceptions.*;
import com.hexaware.bankingsystem.service.BankRepositoryImpl;
import com.hexaware.bankingsystem.service.IBankRepository;

public class BankApp {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        IBankRepository bankService = new BankRepositoryImpl();
        Map<Long, Account> accountMap = new HashMap<>();

        int choice;

        do {
            System.out.println("\nWelcome to HMBank");
            System.out.println("1.Create Account");
            System.out.println("2.View Account Balance");
            System.out.println("3.Deposit");
            System.out.println("4.Withdraw");
            System.out.println("5.Transfer");
            System.out.println("6.View Account Details");
            System.out.println("7.List All Accounts");
            System.out.println("8.Calculate Interest");
            System.out.println("9.Exit");
            
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine(); 

            try {
                switch (choice) {
                case 1:
                    System.out.print("Enter Customer ID: ");
                    int cid = sc.nextInt();
                    sc.nextLine();

                    Customer customer;
                    if (!bankService.isCustomerExists(cid)) {
                        System.out.println("Customer not found. Please enter new customer details:");
                        
                        System.out.print("First Name: ");
                        String fname = sc.nextLine();
                        
                        System.out.print("Last Name: ");
                        String lname = sc.nextLine();
                        
                        System.out.print("Email: ");
                        String email = sc.nextLine();
                        
                        System.out.print("Phone Number: ");
                        String phone = sc.nextLine();
                        
                        System.out.print("Address: ");
                        String address = sc.nextLine();

                        customer = new Customer(cid, fname, lname, email, phone, address);
                    } else {
                        System.out.println("Customer already exists.");
                        customer = new Customer(cid, "", "", "", "", "");
                    }

                    
                    
                    System.out.print("Account Number: ");
                    long accNo = sc.nextLong();
                    
                    sc.nextLine();
                    System.out.print("Account Type (savings/current/zero_balance): ");
                    String accType = sc.nextLine();

                    if (!accType.equalsIgnoreCase("savings") &&
                        !accType.equalsIgnoreCase("current") &&
                        !accType.equalsIgnoreCase("zero_balance")) {
                        System.out.println("invalid account type");
                        break;
                    }

                    
                    System.out.print("Initial Balance: ");
                    float balance = sc.nextFloat();

                    bankService.createAccount(customer, accNo, accType, balance);
                    System.out.println("Account created successfully!");
                    break;

                	case 2:
                		System.out.print("Enter Account Number: ");
                		long accNo1 = sc.nextLong();
                    
                		float bal = bankService.getAccountBalance(accNo1);
                		System.out.printf("Current Balance: Rs%.2f\n", bal);
                        break;


                    case 3:
                        System.out.print("Enter Account Number: ");
                        long depNo = sc.nextLong();
                        
                        System.out.print("Enter Amount to Deposit: ");
                        float depAmt = sc.nextFloat();
                        
                        float newBal = bankService.deposit(depNo, depAmt);
                        System.out.println("Amount Deposited. New Balance: Rs" + newBal);
                        break;

                    case 4:
                        System.out.print("Enter Account Number: ");
                        long witNo = sc.nextLong();
                        
                        System.out.print("Enter Amount to Withdraw: ");
                        float witAmt = sc.nextFloat();
                        
                        float balAfterWithdraw = bankService.withdraw(witNo, witAmt);
                        System.out.println("Amount Withdrawn. new Balance: Rs" + balAfterWithdraw);
                        break;

                    case 5:
                        System.out.print("Enter Sender Account Number: ");
                        long from = sc.nextLong();
                        
                        System.out.print("Enter Receiver Account Number: ");
                        long to = sc.nextLong();
                        
                        System.out.print("Enter Amount to Transfer: ");
                        float amount = sc.nextFloat();
                        
                        bankService.transfer(from, to, amount);
                        System.out.println("Transfer Successful!");
                        break;

                    case 6:
                        System.out.print("Enter Account Number: ");
                        long detailsNo = sc.nextLong();
                        
                        Account account = bankService.getAccountDetails(detailsNo);
                        System.out.println("Account Details:");
                        
                        System.out.println(account);
                        break;

                    case 7:
                        List<Account> accounts = bankService.listAccounts();
                        System.out.println("All Accounts:");
                        for (Account acc : accounts) {
                            System.out.println(acc);
                            System.out.println(" ");
                        }
                        break;

                    case 8:
                        bankService.calculateInterest();
                        System.out.println("Interest calculated ");
                        break;
                    
                        
                    case 9:
                        System.out.println("Thank you");
                        break;
  
                    case 10:
                        List<Account> allAccounts = bankService.listAccounts();
                        allAccounts.sort(new AccountBalanceComparator());

                        System.out.println("Accounts sorted by balance (ascending):");
                        for (Account acc : allAccounts) {
                            System.out.println(acc);
                        }
                        break;
                    
                        
                    case 11:
                        System.out.print("Enter Customer ID: ");
                        int custId = sc.nextInt();

                        Map<Integer, List<Account>> map = new HashMap<>();
                        for (Account acc : bankService.listAccounts()) {
                            map.computeIfAbsent(acc.getCustomer().getCustomerId(), k -> new ArrayList<>()).add(acc);
                        }

                        if (map.containsKey(custId)) {
                            System.out.println("Accounts for Customer ID " + custId + ":");
                            for (Account acc : map.get(custId)) {
                                System.out.println("Account No: " + acc.getAccountNumber() + ", Type: " + acc.getAccountType());
                            }
                        } else {
                            System.out.println("No accounts found.");
                        }
                        break;

   
                    default:
                        System.out.println("Invalid choice");
                        break;
                }

            } catch (InvalidAccountException e) {
                e.printStackTrace();
            } catch (InsufficientFundException e) {
            	e.printStackTrace();
            } catch (OverDraftLimitExceededException e) {
            	e.printStackTrace();
            } catch (Exception e) {
                System.out.println("Unexpected Error" + e.getMessage());
            }

        } while (choice != 9);

        sc.close();
    }
}
