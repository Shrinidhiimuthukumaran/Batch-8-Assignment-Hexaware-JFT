package com.hexaware.bankingsystem.exceptions;

public class InsufficientFundException extends Exception {
    public InsufficientFundException(String message) {
        super(message);
    }
}
