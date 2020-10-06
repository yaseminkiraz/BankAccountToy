package com.example.BankAccountToy.service;

import com.example.BankAccountToy.model.Account;
import com.example.BankAccountToy.model.AccountCommand;
import com.example.BankAccountToy.model.AccountType;
import com.example.BankAccountToy.repository.AccountRepository;
import com.example.BankAccountToy.util.Utils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> createAccount(final AccountCommand accountCommand) {
        final Account account = accountOf(accountCommand);
        if(Utils.greaterThanOrEquals(account.getBalance(),BigDecimal.ZERO))
            return ofNullable(save(account));

        return Optional.empty();
    }

    protected Account save(final Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> getAccountByIban(final UUID iban) {
        final Account account = accountRepository.findAccountByIban(iban);
        return ofNullable(account);
    }

    public Optional<List<Account>> getAccountsByType(final List<AccountType> types)  {
        if(CollectionUtils.isEmpty(types))
            throw new IllegalArgumentException("");

        final List<Account> accounts = accountRepository.findAllByTypeIn(types);
        return ofNullable(accounts);
    }

    private Account accountOf(final AccountCommand accountCommand) {
         return Account.builder()
                .iban(UUID.randomUUID())
                .customerNumber(accountCommand.getCustomerNumber())
                .balance(accountCommand.getBalance())
                .type(accountCommand.getType())
                .isWithdrawable(accountCommand.getType() != AccountType.PRIVATE_LOAN)
                .build();
    }

}
