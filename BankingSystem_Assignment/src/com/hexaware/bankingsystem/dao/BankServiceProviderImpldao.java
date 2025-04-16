package com.hexaware.bankingsystem.dao;

import com.hexaware.bankingsystem.bean.*;


public class BankServiceProviderImpldao extends CustomerServiceProviderImpldao implements IBankServiceProviderdao {

    private IBankRepositorydao bankRepository = new BankRepositoryImpldao();

    @Override
    public void createAccount(Customer customer, long accNo, String accType, float balance) {
        bankRepository.createAccount(customer, accNo, accType, balance);
    }

    @Override
    public Account[] listAccounts() {
        return bankRepository.listAccounts().toArray(new Account[0]);
    }

    @Override
    public void calculateInterest() {
        bankRepository.calculateInterest();
    }
}
