package com.hexaware.bankingsystem.bean;

public class SavingsAccount extends Account {
    private static final float INTEREST_RATE = 4.5f;
    private static final float MIN_BALANCE = 500;

    public SavingsAccount(Customer customer, float initialBalance) {
        super(customer, "Savings", initialBalance < MIN_BALANCE ? MIN_BALANCE : initialBalance);
    }

    @Override
    public boolean withdraw(float amount) {
        if (getAccountBalance() - amount >= MIN_BALANCE) {
            setAccountBalance(getAccountBalance() - amount);
            return true;
        }
        return false;
    }

    @Override
    public float calculateInterest() {
        float interest = (getAccountBalance() * INTEREST_RATE)/100;
        setAccountBalance(getAccountBalance() + interest);
        return interest;
    }
}
