package com.banking.banking_system_backend.repository;

import com.banking.banking_system_backend.model.Transaction;
import com.banking.banking_system_backend.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {


    List<Transaction> findByAccountOrderByTimestampDesc(Account account);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'DEPOSIT'")
    Double getTotalDeposits();

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.type = 'WITHDRAW'")
    Double getTotalWithdrawals();

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.amount > 500000")
    Long countHighValueTransactions();
}
