package com.hexaware.bankingsystem.controlstructure;
import java.util.Scanner;

public class Task1ConditionalStatement {

	public static void main(String[] args) {
		
		Scanner sc = new Scanner(System.in);
        System.out.print("Enter credit score: ");
        int creditScore = sc.nextInt();

        System.out.print("Enter annual income: ");
        double annualIncome = sc.nextDouble();

        if (creditScore > 700 && annualIncome >= 50000) {
            System.out.println("Customer is eligible for a loan.");
        } else {
            System.out.println("Customer is NOT eligible for a loan.");
        }
        
        sc.close();
        

	}

}
