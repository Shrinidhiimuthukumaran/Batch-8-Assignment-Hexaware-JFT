package com.hexaware.bankingsystem.controlstructure;
import java.util.Scanner;

public class Task3LoopStructures {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of customers: ");
        int customers = sc.nextInt();

        for (int i = 1; i <= customers; i++) {
            System.out.println("\nCustomer " + i + ":");

            System.out.print("Enter initial balance: ");
            double initialBalance = sc.nextDouble();

            System.out.print("Enter annual interest rate (%): ");
            double interestRate = sc.nextDouble();

            System.out.print("Enter number of years: ");
            int years = sc.nextInt();

            double futureBalance = initialBalance * Math.pow((1 + interestRate / 100), years);
            System.out.printf("Future Balance after %d years: $%.2f%n", years, futureBalance);
        }

        sc.close();
		

	}

}
