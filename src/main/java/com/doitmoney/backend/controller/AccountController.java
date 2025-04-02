package com.doitmoney.backend.controller;

import com.doitmoney.backend.entity.Account;
import com.doitmoney.backend.entity.User;
import com.doitmoney.backend.repository.UserRepository;
import com.doitmoney.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final UserRepository userRepository; // UserRepository 주입

    @Autowired
    public AccountController(AccountService accountService, UserRepository userRepository) {
        this.accountService = accountService;
        this.userRepository = userRepository;
    }

    // 계좌 등록 – 실제 서비스에서는 인증된 사용자 정보를 사용해야 합니다.
    @PostMapping("/{userId}")
    public Account addAccount(@PathVariable Long userId, @RequestBody Account account) {
        // User 엔티티를 조회하여 account에 세팅
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        account.setUser(user);
        return accountService.addAccount(account);
    }

    // 특정 사용자의 등록된 계좌 목록 조회
    @GetMapping("/{userId}")
    public List<Account> getAccountsByUserId(@PathVariable Long userId) {
        return accountService.getAccountsByUserId(userId);
    }
}