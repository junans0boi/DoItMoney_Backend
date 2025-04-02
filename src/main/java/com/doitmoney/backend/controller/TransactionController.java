package com.doitmoney.backend.controller;

import com.doitmoney.backend.entity.Transaction;
import com.doitmoney.backend.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // (1) 기존: URL 파라미터로 userId를 받아 거래 내역 조회
    @GetMapping("/{userId}")
    public List<Transaction> getTransactionsByUserId(@PathVariable Long userId) {
        return transactionService.getTransactionsByUserId(userId);
    }

    // (2) 기존: 거래 추가
    @PostMapping("/{userId}")
    public Transaction addTransaction(@PathVariable Long userId, @RequestBody Transaction transaction) {
        return transactionService.addTransaction(userId, transaction);
    }

    // (3) 새로 추가: 로그인된 사용자의 거래 내역 조회
    @GetMapping
    public List<Transaction> getMyTransactions(Authentication authentication) {
        // ──────────────────────────────────────────────────────────────────────────────────────
        // ① Authentication에서 현재 로그인된 사용자 정보를 가져옴
        //    (예: CustomUserDetails에 getId()가 있다면 캐스팅 후 userId 추출)
        // ──────────────────────────────────────────────────────────────────────────────────────
        /*
          예시:
          CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
          Long loggedInUserId = userDetails.getUserId();
        */

        // 아래는 "로그인한 userId"를 하드코딩 가정 또는 별도 로직으로 얻었다고 예시
        Long loggedInUserId = 1L; // 실제로는 인증 객체에서 ID를 꺼내야 합니다.

        // ──────────────────────────────────────────────────────────────────────────────────────
        // ② 로그인된 사용자의 거래 내역을 서비스 계층에서 조회
        // ──────────────────────────────────────────────────────────────────────────────────────
        return transactionService.getTransactionsByUserId(loggedInUserId);
 
    }
}