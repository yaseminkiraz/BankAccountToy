package com.example.BankAccountToy.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name="account",schema = "test")
@ToString
@EqualsAndHashCode
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @Column(name="iban",nullable = false,updatable = false)
    @NotNull
    private UUID iban;

    @Column(name = "customer_number")
    @NotNull
    private Long customerNumber;

    @Column(name = "balance")
    @NotNull
    private BigDecimal balance;

    @Column(name = "type")
    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @JsonIgnore
    @NotNull
    @Column(name = "is_withdrawable",columnDefinition = "Boolean default true")
    private Boolean isWithdrawable;
}