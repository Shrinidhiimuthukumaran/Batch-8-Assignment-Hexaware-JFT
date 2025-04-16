package com.hexaware.bankingsystem.controlstructure;
import java.util.Scanner;
public class Task6TransactionHistory {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

        String[] transactions = new String[100]; // Can store up to 100 transactions
        int transactionCount = 0;
        boolean exit = true;

        while (exit) {
            System.out.println("Choose:\n1. Deposit\n2. Withdraw\n3. Exit");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter deposit amount: ");
                    double deposit = sc.nextDouble();
                    transactions[transactionCount] = "Deposited: Rs" + deposit;
                    transactionCount++;
                    System.out.println("Deposit successful.");
                    break;

                case 2:
                    System.out.print("Enter withdrawal amount: ");
                    double withdraw = sc.nextDouble();
                    transactions[transactionCount] = "Withdrew: Rs" + withdraw;
                    transactionCount++;
                    System.out.println("Withdrawal successful.");
                    break;

                case 3:
                    exit = false;
                    break;

                default:
                    System.out.println("Invalid choice. Please choose again.");
            }

            // Prevent overflow of transaction array
            if (transactionCount >= transactions.length) {
                System.out.println("Transaction limit reached!");
                break;
            }
        }

        // Display transaction history
        System.out.println("\nTransaction History:");
        for (int i = 0; i < transactionCount; i++) {
            System.out.println((i + 1) + ". " + transactions[i]);
        }

        sc.close();

	}

}
