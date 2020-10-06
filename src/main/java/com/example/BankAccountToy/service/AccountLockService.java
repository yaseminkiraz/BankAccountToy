package com.example.BankAccountToy.service;

import com.example.BankAccountToy.model.Account;
import com.example.BankAccountToy.model.AccountLock;
import org.springframework.stereotype.Service;

@Service
public class AccountLockService implements AccountLock {

    @Override
    public void lockAccount(Account account) {
        // do something
    }

    @Override
    public void releaseAccount(Account account) {
        // do something
    }

}
