package com.example.BankAccountToy.repository;

import com.example.BankAccountToy.model.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    List<TransactionHistory> findTransactionHistoryByOwnerAccount(UUID iban);

}
