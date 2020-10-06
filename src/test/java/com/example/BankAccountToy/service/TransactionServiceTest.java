package com.example.BankAccountToy.service;

import com.example.BankAccountToy.Util.TestUtil;
import com.example.BankAccountToy.exception.AccountNotFoundException;
import com.example.BankAccountToy.exception.InsufficientFundsException;
import com.example.BankAccountToy.exception.InvalidAmountException;
import com.example.BankAccountToy.exception.InvalidWithdrawAccountException;
import com.example.BankAccountToy.model.Account;
import com.example.BankAccountToy.model.AccountLock;
import com.example.BankAccountToy.model.AccountType;
import com.example.BankAccountToy.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TransactionServiceTest {
    private final Logger LOGGER = LoggerFactory.getLogger(AccountServiceTest.class);
    private TransactionService transactionService;

    @Mock
    private TransactionHistoryService transactionHistoryService;

    @Mock
    private AccountService accountService;

    @Mock
    private AccountLock accountLock;

    @BeforeEach
    public void setUp() {
        transactionService = new TransactionService(accountService,transactionHistoryService);
    }

    @Test
    public void transfer_returnsAccountNotFoundException_whenWithdrawAccountNotFoundByGivenIban() {
        final UUID withdrawAccountIban=UUID.randomUUID();
        final UUID depositAccountIban=UUID.randomUUID();
        final BigDecimal amount = BigDecimal.valueOf(100L);

        assertThrows(AccountNotFoundException.class, () -> transactionService.transfer(withdrawAccountIban, depositAccountIban, amount));
    }

    @Test
    public void transfer_returnsInvalidAmountException_whenAmountIsMinus() {
        final Account withdrawAccount = TestUtil.anyAccount(UUID.randomUUID(),BigDecimal.valueOf(150),AccountType.PRIVATE_LOAN,false);
        final Account depositAccount = TestUtil.anyAccount(UUID.randomUUID(),BigDecimal.valueOf(100),AccountType.CHECKING,true);
        final BigDecimal amount = BigDecimal.valueOf(-1);

        when(accountService.getAccountByIban(withdrawAccount.getIban())).thenReturn(Optional.of(withdrawAccount));
        when(accountService.getAccountByIban(depositAccount.getIban())).thenReturn(Optional.of(depositAccount));

        assertThrows(InvalidAmountException.class, () -> transactionService.transfer(withdrawAccount.getIban(), depositAccount.getIban(), amount));
    }

    @Test
    public void transfer_returnsInvalidAmountException_whenAmountIsZero() {
        final Account withdrawAccount = TestUtil.anyAccount(UUID.randomUUID(),BigDecimal.valueOf(150),AccountType.CHECKING,true);
        final Account depositAccount = TestUtil.anyAccount(UUID.randomUUID(),BigDecimal.valueOf(100),AccountType.CHECKING,true);

        when(accountService.getAccountByIban(withdrawAccount.getIban())).thenReturn(Optional.of(withdrawAccount));
        when(accountService.getAccountByIban(depositAccount.getIban())).thenReturn(Optional.of(depositAccount));

        assertThrows(InvalidAmountException.class, () -> transactionService.transfer(withdrawAccount.getIban(), depositAccount.getIban(), BigDecimal.ZERO));
    }

    @Test
    public void transfer_returnsInsufficientFundsException_whenWithdrawAmountNotEnough() {
        final Account withdrawAccount = TestUtil.anyAccount(UUID.randomUUID(),BigDecimal.valueOf(150),AccountType.PRIVATE_LOAN,false);
        final Account depositAccount = TestUtil.anyAccount(UUID.randomUUID(),BigDecimal.valueOf(100),AccountType.CHECKING,true);
        final BigDecimal amount = BigDecimal.valueOf(1000);

        when(accountService.getAccountByIban(withdrawAccount.getIban())).thenReturn(Optional.of(withdrawAccount));
        when(accountService.getAccountByIban(depositAccount.getIban())).thenReturn(Optional.of(depositAccount));

        assertThrows(InsufficientFundsException.class, () -> transactionService.transfer(withdrawAccount.getIban(), depositAccount.getIban(), amount));
    }

    @Test
    public void transfer_returnsInvalidWithdrawAccountException_whenWithdrawAccountNotAvailable() {
        final Account withdrawAccount = TestUtil.anyAccount(UUID.randomUUID(),BigDecimal.valueOf(150),AccountType.PRIVATE_LOAN,false);
        final Account depositAccount = TestUtil.anyAccount(UUID.randomUUID(),BigDecimal.valueOf(100),AccountType.CHECKING,true);
        final BigDecimal amount = BigDecimal.valueOf(10);

        when(accountService.getAccountByIban(withdrawAccount.getIban())).thenReturn(Optional.of(withdrawAccount));
        when(accountService.getAccountByIban(depositAccount.getIban())).thenReturn(Optional.of(depositAccount));

        assertThrows(InvalidWithdrawAccountException.class, () -> transactionService.transfer(withdrawAccount.getIban(), depositAccount.getIban(), amount));
    }

    @Test
    public void transfer_succeeds() throws InsufficientFundsException, InvalidAmountException, InvalidWithdrawAccountException, AccountNotFoundException {
        final Account withdrawAccount = TestUtil.anyAccount(UUID.randomUUID(),BigDecimal.valueOf(150),AccountType.CHECKING,true);
        final Account depositAccount = TestUtil.anyAccount(UUID.randomUUID(),BigDecimal.valueOf(100),AccountType.CHECKING,true);
        final BigDecimal amount = BigDecimal.valueOf(10);
        final BigDecimal expectedAmount = withdrawAccount.getBalance().subtract(amount);

        when(accountService.getAccountByIban(withdrawAccount.getIban())).thenReturn(Optional.of(withdrawAccount));
        when(accountService.getAccountByIban(depositAccount.getIban())).thenReturn(Optional.of(depositAccount));

        transactionService.transfer(withdrawAccount.getIban(),depositAccount.getIban(),amount);

        assertThat(Utils.equals(withdrawAccount.getBalance(),expectedAmount));

    }

    @Test
    public void depositAccount_returnsAccountNotFoundException_whenDepositAccountNotFoundByGivenIban() {
        final UUID depositIban = UUID.randomUUID();
        final BigDecimal amount = BigDecimal.valueOf(10);
        assertThrows(AccountNotFoundException.class, () -> transactionService.depositAccount(depositIban, amount));

    }

    @Test
    public void depositAccount_returnsInvalidAmountException_whenAmountIsMinus() {
        final Account depositAccount = TestUtil.anyAccount(UUID.randomUUID(),BigDecimal.valueOf(100),AccountType.CHECKING,true);
        final BigDecimal amount = BigDecimal.valueOf(-10);

        when(accountService.getAccountByIban(depositAccount.getIban())).thenReturn(Optional.of(depositAccount));

        assertThrows(InvalidAmountException.class, () -> transactionService.depositAccount(depositAccount.getIban(), amount));
    }

    @Test
    public void depositAccount_returnsInvalidAmountException_whenAmountIsZero() {
        final Account depositAccount = TestUtil.anyAccount(UUID.randomUUID(),BigDecimal.valueOf(100),AccountType.CHECKING,true);
        final BigDecimal amount = BigDecimal.ZERO;

        when(accountService.getAccountByIban(depositAccount.getIban())).thenReturn(Optional.of(depositAccount));

        assertThrows(InvalidAmountException.class, () -> transactionService.depositAccount(depositAccount.getIban(), amount));
    }

    @Test
    public void depositAccount_succeeds() throws InvalidAmountException, AccountNotFoundException {
        final Account depositAccount = TestUtil.anyAccount(UUID.randomUUID(),BigDecimal.valueOf(100),AccountType.CHECKING,true);
        final BigDecimal amount = BigDecimal.valueOf(50);

        when(accountService.getAccountByIban(depositAccount.getIban())).thenReturn(Optional.of(depositAccount));

        transactionService.depositAccount(depositAccount.getIban(),amount);

        assertThat(Utils.equals(depositAccount.getBalance(),BigDecimal.valueOf(150)));
    }
}