package com.example.BankAccountToy.exception;

import java.util.UUID;

public class AccountNotFoundException extends Exception{

    private static final long serialVersionUID = 1L;

    public AccountNotFoundException(UUID iban){
        super("Account not found:" + iban.toString());
    }

    public AccountNotFoundException(){
        super("Account not found");
    }
}
