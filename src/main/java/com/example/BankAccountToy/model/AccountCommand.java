package com.example.BankAccountToy.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class AccountCommand {

    @NotNull
    private BigDecimal balance;

    @NotNull
    private AccountType type;

    @NotNull
    private Long customerNumber;

    public AccountCommand() {
        super();
    }

    public AccountCommand(@NotNull BigDecimal balance, @NotNull AccountType type, @NotNull Long customerNumber) {
        this.balance = balance;
        this.type = type;
        this.customerNumber = customerNumber;
    }
}
