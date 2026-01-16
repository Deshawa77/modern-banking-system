package com.banking.banking_system_backend.controller;

import com.banking.banking_system_backend.model.Account;
import com.banking.banking_system_backend.model.Transaction;
import com.banking.banking_system_backend.model.User;
import com.banking.banking_system_backend.repository.AccountRepository;
import com.banking.banking_system_backend.repository.TransactionRepository;
import com.banking.banking_system_backend.repository.UserRepository;
import com.banking.banking_system_backend.service.AccountService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private AccountService accountService;

    // ----------------- Create Account (Admin Only) -----------------
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<?> createAccount(@RequestBody CreateAccountRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Optional: check if user already has an account
        if (accountRepository.findByUser(user).isPresent()) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "User already has an account"));
        }

        Account account = new Account();
        account.setUser(user);
        account.setAccountNumber(java.util.UUID.randomUUID().toString());
        account.setBalance(BigDecimal.ZERO);

        accountRepository.save(account);

        log.info("Account created for user {} with account number {}", user.getUsername(), account.getAccountNumber());

        return ResponseEntity.ok(new ApiResponse(true, "Account created successfully", account.getAccountNumber()));
    }

    // ----------------- Deposit -----------------
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionRequest request) {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Deposit amount must be positive"));
        }

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getUsername().equals(loggedInUsername) && !hasRole("ADMIN")) {
            throw new AccessDeniedException("You can only deposit into your own account");
        }

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        // Record transaction
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setType("DEPOSIT");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        return ResponseEntity.ok(new ApiResponse(true, "Deposit successful", account.getBalance()));
    }

    // ----------------- Withdraw -----------------
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody TransactionRequest request) {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Withdrawal amount must be positive"));
        }

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getUsername().equals(loggedInUsername) && !hasRole("ADMIN")) {
            throw new AccessDeniedException("You can only withdraw from your own account");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "Insufficient balance"));
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        accountRepository.save(account);

        // Record transaction
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setType("WITHDRAW");
        transaction.setTimestamp(LocalDateTime.now());
        transactionRepository.save(transaction);

        return ResponseEntity.ok(new ApiResponse(true, "Withdrawal successful", account.getBalance()));
    }

    // ----------------- Balance Inquiry -----------------
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/balance")
    public ResponseEntity<?> getBalance(@RequestParam Long accountId) {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getUsername().equals(loggedInUsername) && !hasRole("ADMIN")) {
            throw new AccessDeniedException("You can only view your own account balance");
        }

        return ResponseEntity.ok(Map.of(
                "accountId", account.getId(),
                "balance", account.getBalance()
        ));
    }

    // ----------------- Transaction History -----------------
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/transactions")
    public ResponseEntity<?> transactionHistory(@RequestParam Long accountId) {
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getUsername().equals(loggedInUsername) && !hasRole("ADMIN")) {
            throw new AccessDeniedException("You can only view your own transactions");
        }

        List<Transaction> transactions = transactionRepository.findByAccountOrderByTimestampDesc(account);
        return ResponseEntity.ok(transactions);
    }

    // ----------------- Get Accounts of Logged-in User -----------------
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/my-accounts")
    public ResponseEntity<?> getMyAccounts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<Account> accounts;

        if (hasRole("ADMIN")) {
            accounts = accountRepository.findAll();
        } else {
            accounts = accountRepository.findAllByUserUsername(username);
        }

        return ResponseEntity.ok(accounts);
    }


    // ----------------- Helper -----------------
    private boolean hasRole(String role) {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_" + role));
    }

    // ----------------- DTO Classes -----------------
    @Data
    public static class CreateAccountRequest {
        private Long userId;
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

    @Data
    public static class TransactionRequest {
        private Long accountId;
        private BigDecimal amount;
    }
}
