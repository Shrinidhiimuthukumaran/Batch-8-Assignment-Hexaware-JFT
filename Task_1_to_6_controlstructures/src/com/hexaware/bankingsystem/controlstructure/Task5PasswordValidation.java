package com.hexaware.bankingsystem.controlstructure;
import java.util.Scanner;

public class Task5PasswordValidation {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

        System.out.print("Create a password: ");
        String password = sc.nextLine();

        boolean isValid = password.length() >= 8;
        boolean hasUpper = false;
        boolean hasDigit = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUpper = true;
            }
            if (Character.isDigit(ch)) {
                hasDigit = true;
            }
        }

        if (isValid && hasUpper && hasDigit) {
            System.out.println("Password is valid.");
        } else {
            System.out.println("Password is invalid. Make sure it has:");
            if (!isValid) System.out.println("- At least 8 characters");
            if (!hasUpper) System.out.println("- At least one uppercase letter");
            if (!hasDigit) System.out.println("- At least one digit");
        }

        sc.close();

	}

}
