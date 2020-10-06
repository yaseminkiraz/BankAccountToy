package com.example.BankAccountToy.Util;

import com.example.BankAccountToy.model.Account;
import com.example.BankAccountToy.model.AccountCommand;
import com.example.BankAccountToy.model.AccountType;

import java.math.BigDecimal;
import java.util.UUID;

public class TestUtil {
    public static Account anyAccount(final UUID iban, final BigDecimal balance,
                                     final AccountType accountType, final Boolean isWithdrawable) {
        final Account account = new Account();
        account.setIban(iban);
        account.setBalance(balance);
        account.setType(accountType);
        account.setIsWithdrawable(isWithdrawable);
        return account;
    }

    public static Account accountOf(final AccountCommand accountCommand) {
        return Account.builder()
                .iban(UUID.randomUUID())
                .customerNumber(accountCommand.getCustomerNumber())
                .balance(accountCommand.getBalance())
                .type(accountCommand.getType())
                .isWithdrawable(accountCommand.getType() != AccountType.PRIVATE_LOAN)
                .build();
    }
}
