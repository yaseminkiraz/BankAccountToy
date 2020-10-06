package com.example.BankAccountToy.service;

import com.example.BankAccountToy.exception.AccountNotFoundException;
import com.example.BankAccountToy.exception.InsufficientFundsException;
import com.example.BankAccountToy.exception.InvalidAmountException;
import com.example.BankAccountToy.exception.InvalidWithdrawAccountException;
import com.example.BankAccountToy.model.Account;
import com.example.BankAccountToy.model.AccountLock;
import com.example.BankAccountToy.model.TransactionHistoryType;
import com.example.BankAccountToy.util.Utils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Transactional
public class TransactionService {

    private final AccountService accountService;
    private final TransactionHistoryService transactionHistoryService;

    private AccountLock accountLock;

    public TransactionService(final AccountService accountService,final TransactionHistoryService transactionHistoryService) {
        this.accountService = accountService;
        this.transactionHistoryService = transactionHistoryService;
    }

    @Transactional
    public void transfer(final UUID withdrawAccountIban, final UUID depositAccountIban, final BigDecimal amount) throws InsufficientFundsException, InvalidWithdrawAccountException, AccountNotFoundException, InvalidAmountException {
        Account withdrawAccount = accountService.getAccountByIban(withdrawAccountIban).orElseThrow(AccountNotFoundException::new);
        Account depositAccount = accountService.getAccountByIban(depositAccountIban).orElseThrow(AccountNotFoundException::new);

        if (Utils.lessThanOrEquals(amount,BigDecimal.ZERO))
            throw new InvalidAmountException(String.format("Invalid amount [%s]",
                    amount));

        try {
//            accountLock.lockAccount(withdrawAccount.get());
//            accountLock.lockAccount(depositAccount.get());

            withdraw(withdrawAccount,amount);
            deposit(depositAccount,amount);

            transactionHistoryService.save(withdrawAccount,withdrawAccount,depositAccount,amount, TransactionHistoryType.WITHDRAW);
            transactionHistoryService.save(depositAccount,withdrawAccount,depositAccount,amount, TransactionHistoryType.DEPOSIT);
        } catch (Exception exception) {
            throw exception;
        } finally {
//            accountLock.releaseAccount(withdrawAccount.get());
//            accountLock.releaseAccount(depositAccount.get());
        }
    }

    @Transactional
    public void depositAccount(final UUID iban, final BigDecimal amount) throws AccountNotFoundException, InvalidAmountException {
        Account depositAccount = accountService.getAccountByIban(iban).orElseThrow(AccountNotFoundException::new);

//        accountLock.lockAccount(depositAccount.get());
        deposit(depositAccount,amount);
//        accountLock.releaseAccount(depositAccount.get());
        transactionHistoryService.save(depositAccount,null,depositAccount,amount, TransactionHistoryType.DEPOSIT);
    }

    private void deposit(final Account depositAccount,final BigDecimal amount) throws InvalidAmountException {
        if (Utils.lessThanOrEquals(amount,BigDecimal.ZERO))
            throw new InvalidAmountException(String.format("Invalid deposit amount [%s]",
                    amount));

        depositAccount.setBalance(depositAccount.getBalance().add(amount));
        accountService.save(depositAccount);
    }

    private void withdraw(final Account withdrawAccount,final BigDecimal amount) throws InsufficientFundsException, InvalidWithdrawAccountException {
        if(Utils.lessThan(withdrawAccount.getBalance(), amount)) {
            throw new InsufficientFundsException("Insufficient balance");
        }

        if(!withdrawAccount.getIsWithdrawable()) {
            throw new InvalidWithdrawAccountException(String.format("Invalid withdraw account [%s]",
                    withdrawAccount.getIban()));
        }

        withdrawAccount.setBalance(withdrawAccount.getBalance().subtract(amount));
        accountService.save(withdrawAccount);
    }
}
