package com.hexaware.bankingsystem.app;

import java.util.Comparator;
import com.hexaware.bankingsystem.bean.Account;

public class AccountBalanceComparator implements Comparator<Account> {
    @Override
    public int compare(Account a1, Account a2) {
        return Float.compare(a1.getAccountBalance(), a2.getAccountBalance());
    }
}