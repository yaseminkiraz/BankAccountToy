package com.example.BankAccountToy.service;

import com.example.BankAccountToy.model.Account;
import com.example.BankAccountToy.model.TransactionHistory;
import com.example.BankAccountToy.model.TransactionHistoryType;
import com.example.BankAccountToy.repository.TransactionHistoryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionHistoryService {

    private TransactionHistoryRepository transactionHistoryRepository;

    public TransactionHistoryService(final TransactionHistoryRepository transactionHistoryRepository) {
        this.transactionHistoryRepository = transactionHistoryRepository;
    }

    protected TransactionHistory save(final Account ownerAccount, final Account sourceAccount, final Account destinationAccount, final BigDecimal amount, final TransactionHistoryType historyType) {

        final TransactionHistory transactionHistory = new TransactionHistory(ownerAccount.getIban(),sourceAccount.getIban(),
                destinationAccount.getIban(),amount,historyType);
        return transactionHistoryRepository.save(transactionHistory);
    }

    public List<TransactionHistory> getHistory (final UUID iban) {
        return transactionHistoryRepository.findTransactionHistoryByOwnerAccount(iban);
    }
}
