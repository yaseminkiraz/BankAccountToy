package com.example.BankAccountToy.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="transaction_history",schema = "test")
@ToString
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE)
    private Long id;

    @NotNull
    @Column(name="owner_account")
    private UUID ownerAccount;

    @Column(name="from_account")
    private UUID fromAccount;

    @Column(name="to_account")
    private UUID toAccount;

    @Column(name="amount")
    private BigDecimal amount;

    @Column(name="history_type")
    private TransactionHistoryType transactionHistoryType;

    @Column(name="transaction_time")
    private LocalDateTime timestamp;

    public TransactionHistory(UUID ownerAccount, UUID fromAccount, UUID toAccount, BigDecimal amount, TransactionHistoryType transactionHistoryType) {
        this.ownerAccount = ownerAccount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.transactionHistoryType = transactionHistoryType;
        this.timestamp = LocalDateTime.now();
    }
}
