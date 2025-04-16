package com.hexaware.bankingsystem.bean;

public class CurrentAccount extends Account {
    private static final float OVERDRAFT_LIMIT = 1000;

    public CurrentAccount(Customer customer, float balance) {
        super(customer, "Current", balance);
    }

    @Override
    public boolean withdraw(float amount) {
        if (getAccountBalance() + OVERDRAFT_LIMIT >= amount) {
            setAccountBalance(getAccountBalance() - amount);
            return true;
        }
        return false;
    }

    @Override
    public float calculateInterest() {
        return 0;
    }
}
