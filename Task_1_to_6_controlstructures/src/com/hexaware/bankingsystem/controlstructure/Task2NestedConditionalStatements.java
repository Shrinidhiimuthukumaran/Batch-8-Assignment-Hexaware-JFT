package com.hexaware.bankingsystem.controlstructure;
import java.util.Scanner;

public class Task2NestedConditionalStatements {

	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double balance;

        System.out.print("Enter your current balance: ");
        balance = sc.nextDouble();

        System.out.println("Choose an option:\n1. Check Balance\n2. Withdraw\n3. Deposit");
        int choice = sc.nextInt();

        if (choice == 1) {
            System.out.println("Your balance is: Rs" + balance);
        } else if (choice == 2) {
            System.out.print("Enter amount to withdraw: ");
            double withdrawAmount = sc.nextDouble();

            if (withdrawAmount > balance) {
                System.out.println("Insufficient balance!");
            } else if (withdrawAmount % 100 == 0 || withdrawAmount % 500 == 0) {
                balance -= withdrawAmount;
                System.out.println("Withdrawal successful. Remaining balance: Rs" + balance);
            } else {
                System.out.println("Invalid amount. Must be in multiples of 100 or 500.");
            }
        } else if (choice == 3) {
            System.out.print("Enter amount to deposit: ");
            double depositAmount = sc.nextDouble();
            balance += depositAmount;
            System.out.println("Deposit successful. Updated balance: Rs" + balance);
        } else {
            System.out.println("Invalid option.");
        }

        sc.close();
		

	}

}
