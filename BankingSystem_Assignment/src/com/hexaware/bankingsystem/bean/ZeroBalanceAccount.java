package com.hexaware.bankingsystem.bean;

public class ZeroBalanceAccount extends Account {

    public ZeroBalanceAccount(Customer customer) {
        super(customer, "ZeroBalance", 0);
    }

    @Override
    public boolean withdraw(float amount) {
        if (getAccountBalance() >= amount) {
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
