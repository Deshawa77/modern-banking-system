package com.banking.banking_system_backend.service;

import com.banking.banking_system_backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsService {

    @Autowired
    private TransactionRepository transactionRepository;

    public Map<String, Object> getSummary() {
        Map<String, Object> data = new HashMap<>();

        data.put("totalDeposits", transactionRepository.getTotalDeposits());
        data.put("totalWithdrawals", transactionRepository.getTotalWithdrawals());
        data.put("highValueTransactions", transactionRepository.countHighValueTransactions());

        return data;
    }
}
