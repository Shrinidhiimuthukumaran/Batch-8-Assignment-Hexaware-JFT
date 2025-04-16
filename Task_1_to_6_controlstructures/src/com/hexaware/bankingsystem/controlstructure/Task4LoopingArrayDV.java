package com.hexaware.bankingsystem.controlstructure;
import java.util.Scanner;

public class Task4LoopingArrayDV {

	public static void main(String[] args) {
		 Scanner sc = new Scanner(System.in);

	        int[] accountNumbers = {101, 102, 103};
	        double[] balances = {1000.0, 2500.5, 3200.75};

	        boolean flag = false;

	        while (!flag) { //here if true infinite loops will run
	            System.out.print("Enter your account number: ");
	            int accNum = sc.nextInt();

	            for (int i = 0; i < accountNumbers.length; i++) {
	                if (accountNumbers[i] == accNum) {//if the accno matches with any of these array of accnos then the flag value will become true
	                    System.out.println("Your balance is: $" + balances[i]);
	                    flag = true;
	                    break;
	                }
	            }

	            if (!flag) {
	                System.out.println("Invalid account number. Please try again.");
	            }
	        }

	        sc.close();

	}

}
