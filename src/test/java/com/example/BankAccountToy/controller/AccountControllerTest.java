package com.example.BankAccountToy.controller;

import com.example.BankAccountToy.Util.TestUtil;
import com.example.BankAccountToy.model.Account;
import com.example.BankAccountToy.model.AccountCommand;
import com.example.BankAccountToy.model.AccountType;
import com.example.BankAccountToy.service.AccountService;
import com.example.BankAccountToy.service.TransactionHistoryService;
import com.example.BankAccountToy.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.web.util.UriComponentsBuilder.fromPath;


@ExtendWith(SpringExtension.class)
@WebMvcTest
public class AccountControllerTest {

    public static final String ENDPOINT_ACCOUNTS = "/api/accounts/";
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private AccountController accountController;

    @MockBean
    private AccountService accountService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private TransactionHistoryService transactionHistoryService;

    @BeforeEach
    public void setUp() {
        mockMvc = webAppContextSetup(webApplicationContext).build();
        assertNotNull(mockMvc);
    }

    @Test
    public void createAccount_returnsBadRequestStatusCode_OnMissingAccountDetails() throws Exception {
        final AccountCommand accountCommand = new AccountCommand();
        final URI uri= fromPath(ENDPOINT_ACCOUNTS).build().toUri();

        final MockHttpServletResponse response = mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(accountCommand))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void createAccount_returnsNotAcceptableStatusCode_whenGivenMinusBalance() throws Exception {
        final AccountCommand accountCommand = new AccountCommand(BigDecimal.valueOf(-100),AccountType.CHECKING,1L);
        final Optional<Account> account = Optional.empty();
        final URI uri= fromPath(ENDPOINT_ACCOUNTS).build().toUri();

        when(accountService.createAccount(accountCommand)).thenReturn(account);

        final MockHttpServletResponse response = mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(accountCommand))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    @Test
    public void createAccount_returnsHttpStatusOk_whenAccountInformationsAreGiven() throws Exception {
        final URI uri= fromPath(ENDPOINT_ACCOUNTS).build().toUri();
        final AccountCommand accountCommand = new AccountCommand(BigDecimal.valueOf(100),AccountType.CHECKING,1L);
        final Optional<Account> account = Optional.of(TestUtil.accountOf(accountCommand));

        when(accountService.createAccount(accountCommand)).thenReturn(account);

        final MockHttpServletResponse response = mockMvc.perform(post(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(accountCommand))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(account.get().getBalance()).isEqualTo(accountCommand.getBalance());
        assertThat(account.get().getType()).isEqualTo(accountCommand.getType());
        assertThat(account.get().getCustomerNumber()).isEqualTo(accountCommand.getCustomerNumber());
    }

    @Test
    public void getAccountBalance_returnsHttpStatusNotFound_WhenAccountIbanNotExist() throws Exception {
        final UUID iban = UUID.randomUUID();
        final URI uri = fromPath(ENDPOINT_ACCOUNTS + iban).build().toUri();
        final Optional<Account> account = Optional.empty();


        when(accountService.getAccountByIban(iban)).thenReturn(account);

        final MockHttpServletResponse response = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void getAccountBalance_returnsHttpStatusOk_WhenAccountFound() throws Exception {
        final UUID iban = UUID.randomUUID();
        final URI uri = fromPath(ENDPOINT_ACCOUNTS).path(iban.toString()).pathSegment("balance").build().toUri();
        final Optional<Account> account = Optional.of(TestUtil.anyAccount(iban,BigDecimal.valueOf(100),AccountType.SAVINGS,true));

        when(accountService.getAccountByIban(iban)).thenReturn(account);

        final MockHttpServletResponse response = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(account.get().getBalance().toString());
    }

    @Test
    public void getAccountsByType_returnsHttpStatusCodeOk_WhenTypesGiven() throws Exception {
        final List<AccountType> types = Arrays.asList(AccountType.CHECKING,AccountType.SAVINGS);
        final URI uri = fromPath(ENDPOINT_ACCOUNTS).pathSegment("list").build().toUri();
        Optional<List<Account>> accountList = Optional.empty();

        when(accountService.getAccountsByType(types)).thenReturn(accountList);

        final MockHttpServletResponse response = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(types))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}