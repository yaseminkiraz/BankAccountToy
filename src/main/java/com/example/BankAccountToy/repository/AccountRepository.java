package com.example.BankAccountToy.repository;

import com.example.BankAccountToy.model.Account;
import com.example.BankAccountToy.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findAllByTypeIn(List<AccountType> types);

//    @Query(name="lockAccount",
//            query="SELECT a FROM account a WHERE a.iban:iban",
//            lockMode = PESSIMISTIC_READ)
    Account findAccountByIban(UUID iban);
}
