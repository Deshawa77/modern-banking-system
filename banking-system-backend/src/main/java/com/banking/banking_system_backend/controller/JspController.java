package com.banking.banking_system_backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jsp")
public class JspController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/accounts")
    public String accounts() {
        return "accounts";
    }

    @GetMapping("/create-account")
    public String createAccount() {
        return "create-account";
    }

    @GetMapping("/deposit")
    public String deposit() {
        return "deposit";
    }

    @GetMapping("/withdraw")
    public String withdraw() {
        return "withdraw";
    }

    @GetMapping("/transactions")
    public String transactions() {
        return "transactions";
    }

    @GetMapping("/transfer")
    public String transfer() {
        return "transfer";
    }
}
