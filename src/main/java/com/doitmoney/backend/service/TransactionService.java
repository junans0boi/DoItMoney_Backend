package com.doitmoney.backend.service;

import com.doitmoney.backend.entity.Transaction;
import com.doitmoney.backend.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // 특정 회원의 거래 내역 조회
    public List<Transaction> getTransactionsByUserId(Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    // 거래 추가: 회원별 거래 ID를 자동 증가 (해당 회원의 마지막 ID + 1)
    public Transaction addTransaction(Long userId, Transaction transaction) {
        transaction.setUserId(userId);
        Integer nextId = transactionRepository.findByUserId(userId)
                .stream()
                .map(Transaction::getId)
                .max(Integer::compareTo)
                .orElse(0) + 1;
        transaction.setId(nextId);
        return transactionRepository.save(transaction);
    }
}