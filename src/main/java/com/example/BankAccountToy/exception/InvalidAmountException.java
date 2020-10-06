package com.example.BankAccountToy.exception;

public class InvalidAmountException extends Exception {
    public InvalidAmountException(String message){
        super(message);
    }
}