package com.example.BankAccountToy.service;

import com.example.BankAccountToy.Util.TestUtil;
import com.example.BankAccountToy.model.Account;
import com.example.BankAccountToy.model.AccountCommand;
import com.example.BankAccountToy.model.AccountType;
import com.example.BankAccountToy.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        accountService = new AccountService(accountRepository);
    }

    @Test
    public void createAccount_returnAccount_whenByGivenBalanceAndType() {
        final AccountCommand accountCommand = new AccountCommand();
        accountCommand.setBalance(BigDecimal.valueOf(100L));
        accountCommand.setType(AccountType.PRIVATE_LOAN);

        final Account account = TestUtil.accountOf(accountCommand);

        when(accountRepository.save(account)).thenReturn(account);

        final Account accountFetched = accountService.save(account);

        assertThat(accountFetched.toString()).isEqualTo(account.toString());
    }

    @Test
    public void getAccountByIban_doesNotReturnAccount_whenAccountNotFoundByGivenIban() {
        final UUID iban = UUID.randomUUID();
        when(accountRepository.findAccountByIban(iban)).thenReturn(null);

        final Optional<Account> accountFetched = accountService.getAccountByIban(iban);

        assertThat(accountFetched.isPresent()).isFalse();
    }

    @Test
    public void getAccountByIban_returnsAccount_whenAccountFoundByGivenIban() {
        final Account account = TestUtil.anyAccount(UUID.randomUUID(),
                BigDecimal.valueOf(100),AccountType.SAVINGS,true);

        when(accountRepository.findAccountByIban(account.getIban())).thenReturn(account);

        final Optional<Account> accountFetched = accountService.getAccountByIban(account.getIban());

        assertThat(accountFetched.isPresent()).isTrue();
        assertThat(accountFetched.get().getIban()).isEqualTo(account.getIban());
        assertThat(accountFetched.get().getBalance()).isEqualTo(account.getBalance());
        assertThat(accountFetched.get().getType()).isEqualTo(account.getType());
        assertThat(accountFetched.get().getIsWithdrawable()).isEqualTo(account.getIsWithdrawable());
    }

    @Test
    public void getAccountsByType_throwException_whenTypesNull() throws IllegalArgumentException    {
        final List<AccountType> types = null;
        when(accountRepository.findAllByTypeIn(types)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> accountService.getAccountsByType(types));

    }

    @Test
    public void getAccountsByType_returnsAccountList_whenAccountFoundByTypes(){
        final List<AccountType> types = new ArrayList<>();
        types.add(AccountType.SAVINGS);

        List<Account> accounts = new ArrayList<>();
        accounts.add(TestUtil.anyAccount(UUID.randomUUID(),
                BigDecimal.valueOf(100),AccountType.SAVINGS,true));

        when(accountRepository.findAllByTypeIn(types)).thenReturn(accounts);

        final Optional<List<Account>> accountFetched = accountService.getAccountsByType(types);

        assertThat(accountFetched.isPresent()).isTrue();
        assertThat(accountFetched.get().toString()).isEqualTo(accounts.toString());
    }


}