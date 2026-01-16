package com.banking.banking_system_backend.repository;

import com.banking.banking_system_backend.model.Account;
import com.banking.banking_system_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);
    Optional<Account> findByUser(User user);
    List<Account> findAllByUser(User user);
    List<Account> findAllByUserUsername(String username);
}

