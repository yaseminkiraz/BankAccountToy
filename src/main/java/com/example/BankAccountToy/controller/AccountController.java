package com.example.BankAccountToy.controller;

import com.example.BankAccountToy.model.Account;
import com.example.BankAccountToy.model.AccountCommand;
import com.example.BankAccountToy.model.AccountType;
import com.example.BankAccountToy.service.AccountService;
import com.example.BankAccountToy.service.TransactionHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;
    private final TransactionHistoryService transactionHistoryService;


    public AccountController(final AccountService accountService, final TransactionHistoryService transactionHistoryService) {
        this.accountService = accountService;
        this.transactionHistoryService = transactionHistoryService;
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody AccountCommand accountCommand){
        final Optional<Account> account = accountService.createAccount(accountCommand);
        if(!account.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(account.get());
    }

    @GetMapping("/accounts/{iban}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(@PathVariable(value = "iban") UUID iban){
        final Optional<Account> account = accountService.getAccountByIban(iban);
        if(!account.isPresent())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.ok(account.get().getBalance());
    }

    @GetMapping(value="/accounts/list")
    public ResponseEntity<List<Account>> getAccountsByType(@RequestBody List<AccountType> types){
        final Optional<List<Account>> accountList = accountService.getAccountsByType(types);
        if (!accountList.isPresent())
            return ResponseEntity.ok().build();
        return ResponseEntity.ok(accountList.get());
    }

}
