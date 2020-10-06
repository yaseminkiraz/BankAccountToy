package com.example.BankAccountToy.model;

public interface AccountLock {
     void lockAccount(Account account);
     void releaseAccount(Account account);
}
