package com.example.BankAccountToy.controller;

import com.example.BankAccountToy.model.Account;
import com.example.BankAccountToy.model.TransactionHistory;
import com.example.BankAccountToy.service.AccountService;
import com.example.BankAccountToy.service.TransactionHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TransactionHistoryController {
    private final AccountService accountService;
    private final TransactionHistoryService transactionHistoryService;

    public TransactionHistoryController(final AccountService accountService, final TransactionHistoryService transactionHistoryService) {
        this.accountService = accountService;
        this.transactionHistoryService = transactionHistoryService;
    }

    @GetMapping("/accounts/{iban}/history")
    public ResponseEntity<List<TransactionHistory>> getAccountTransactionHistory(@PathVariable(value = "iban") UUID iban){
        final Optional<Account> account = accountService.getAccountByIban(iban);
        if(!account.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(transactionHistoryService.getHistory(iban));
    }
}
