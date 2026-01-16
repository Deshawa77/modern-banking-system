package com.banking.banking_system_backend.service;

import com.banking.banking_system_backend.model.Account;
import com.banking.banking_system_backend.model.Transaction;
import com.banking.banking_system_backend.repository.AccountRepository;
import com.banking.banking_system_backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // ----- Deposit -----
    public BigDecimal deposit(Long accountId, BigDecimal amount, String username) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));


        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You can only deposit into your own account");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Deposit amount must be positive");
        }

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);


        Transaction txn = new Transaction();
        txn.setAccount(account);
        txn.setAmount(amount);
        txn.setType("DEPOSIT");
        transactionRepository.save(txn);

        return account.getBalance(); // Return updated balance
    }

    // ----- Withdraw -----
    public BigDecimal withdraw(Long accountId, BigDecimal amount, String username) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));


        if (!account.getUser().getUsername().equals(username)) {
            throw new RuntimeException("You can only withdraw from your own account");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Withdrawal amount must be positive");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);


        Transaction txn = new Transaction();
        txn.setAccount(account);
        txn.setAmount(amount);
        txn.setType("WITHDRAW");
        transactionRepository.save(txn);

        return account.getBalance();
    }

    // ----- Get all accounts for a user -----
    public List<Account> getAccountsByUsername(String username) {
        return accountRepository.findAllByUserUsername(username);
    }
}
