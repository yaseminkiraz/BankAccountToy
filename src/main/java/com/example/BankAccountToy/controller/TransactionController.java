package com.example.BankAccountToy.controller;

import com.example.BankAccountToy.exception.AccountNotFoundException;
import com.example.BankAccountToy.exception.InsufficientFundsException;
import com.example.BankAccountToy.exception.InvalidAmountException;
import com.example.BankAccountToy.exception.InvalidWithdrawAccountException;
import com.example.BankAccountToy.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/accounts/deposit")
    public ResponseEntity<Void> depositAccount(@Valid @RequestParam(value = "Deposit Account Iban") UUID depositAccountIban,
                                               @Valid @RequestParam(value = "Deposit Amount") BigDecimal amount) {
        try {
            transactionService.depositAccount(depositAccountIban,amount);
        }
        catch (AccountNotFoundException acc)
        {
            ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        catch (InvalidAmountException ex)
        {
            ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accounts/transfer")
    public ResponseEntity<Void> transferAccount(@Valid @RequestParam(value = "Withdraw Account Iban") UUID withdrawAccountIban,
                                                @Valid @RequestParam(value = "Deposit Account Iban") UUID depositAccountIban,
                                                @Valid @RequestParam(value = "Transfer Amount") BigDecimal amount){
        try {
            transactionService.transfer(withdrawAccountIban,depositAccountIban,amount);
        }
        catch (AccountNotFoundException acc)
        {
            ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        catch (InsufficientFundsException | InvalidAmountException ex)
        {
            ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE);
        }
        catch (InvalidWithdrawAccountException ex)
        {
            ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED);
        }
        return ResponseEntity.ok().build();
    }
}
