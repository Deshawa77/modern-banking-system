package com.banking.banking_system_backend.controller;

import com.banking.banking_system_backend.model.Account;
import com.banking.banking_system_backend.model.Transaction;
import com.banking.banking_system_backend.model.User;
import com.banking.banking_system_backend.repository.AccountRepository;
import com.banking.banking_system_backend.repository.TransactionRepository;
import com.banking.banking_system_backend.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // ----- ADMIN: Create Account -----
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(java.util.UUID.randomUUID().toString());
        account.setBalance(BigDecimal.ZERO);

        accountRepository.save(account);

        log.info("Account created for user {} with account number {}", user.getUsername(), account.getAccountNumber());

        return ResponseEntity.ok(new ApiResponse(true, "Account created successfully", account.getAccountNumber()));
    }

    // ----- Deposit -----
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if(request.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Deposit amount must be positive"));
        }

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction txn = new Transaction();
        txn.setAccount(account);
        txn.setAmount(request.getAmount());
        txn.setType("DEPOSIT");
        transactionRepository.save(txn);

        log.info("Deposit of {} to account {}", request.getAmount(), account.getAccountNumber());

        return ResponseEntity.ok(new ApiResponse(true, "Deposit successful", account.getBalance()));
    }

    // ----- Withdraw -----
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody TransactionRequest request) {
        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if(request.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Withdraw amount must be positive"));
        }

        if(account.getBalance().compareTo(request.getAmount()) < 0){
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Insufficient balance"));
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        Transaction txn = new Transaction();
        txn.setAccount(account);
        txn.setAmount(request.getAmount());
        txn.setType("WITHDRAW");
        transactionRepository.save(txn);

        log.info("Withdrawal of {} from account {}", request.getAmount(), account.getAccountNumber());

        return ResponseEntity.ok(new ApiResponse(true, "Withdrawal successful", account.getBalance()));
    }

    // ----- Balance Inquiry -----
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/balance/{accountId}")
    public ResponseEntity<?> balance(@PathVariable Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        return ResponseEntity.ok(new ApiResponse(true, "Balance retrieved", account.getBalance()));
    }

    // ----- Transaction History -----
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<?> transactionHistory(@PathVariable Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<Transaction> transactions = transactionRepository.findByAccountOrderByTimestampDesc(account);

        return ResponseEntity.ok(transactions);
    }

    // ----- DTO Classes -----
    @Data
    public static class CreateAccountRequest {
        private Long userId;
    }

    @Data
    public static class TransactionRequest {
        private Long accountId;
        private BigDecimal amount;
    }

    @Data
    public static class ApiResponse {
        private boolean success;
        private String message;
        private Object data;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public ApiResponse(boolean success, String message, Object data) {
            this.success = success;
            this.message = message;
            this.data = data;
        }
    }
}
