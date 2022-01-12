package com.techbank.account.query.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends CrudRepository<BankAccount, String> {

    Optional<BankAccount> findByAccountHolder(String accountHolder);
    List<BankAccount> findByBalanceGreaterThan(double balance);
    List<BankAccount> findByBalanceLessThan(double balance);
}
