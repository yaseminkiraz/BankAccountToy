package com.example.BankAccountToy.exception;

public class InvalidWithdrawAccountException extends Exception {
    public InvalidWithdrawAccountException(String message){
        super(message);
    }
}
