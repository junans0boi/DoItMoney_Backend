package com.doitmoney.backend.fixedExpense.service;

import com.doitmoney.backend.transaction.entity.Transaction;
import com.doitmoney.backend.fixedExpense.entity.FixedExpense;
import com.doitmoney.backend.fixedExpense.repository.FixedExpenseRepository;
import com.doitmoney.backend.transaction.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class FixedExpenseService {

    private final FixedExpenseRepository fixedExpenseRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public FixedExpenseService(FixedExpenseRepository fixedExpenseRepository,
            TransactionRepository transactionRepository) {
        this.fixedExpenseRepository = fixedExpenseRepository;
        this.transactionRepository = transactionRepository;
    }

    // 매일 새벽 0시 5분에 실행 (예시)
    @Scheduled(cron = "0 5 0 * * ?")
    public void processFixedExpenses() {
        LocalDate today = LocalDate.now();
        int todayDay = today.getDayOfMonth();

        List<FixedExpense> fixedExpenses = fixedExpenseRepository.findAll();
        for (FixedExpense fe : fixedExpenses) {
            if (fe.isActive() &&
                    fe.getDayOfMonth() == todayDay &&
                    (fe.getLastExecutedDate() == null || !fe.getLastExecutedDate().isEqual(today))) {

                // FixedExpense 정보를 바탕으로 Transaction 생성
                Transaction transaction = Transaction.builder()
                        .userId(fe.getUser().getId())
                        .transactionDate(LocalDate.now()) // LocalDate.now()로 변경
                        .transactionType(fe.getTransactionType())
                        .amount(fe.getAmount().intValue()) // Transaction.amount가 Integer일 경우 변환
                        .description(fe.getCategory())
                        .accountName(null) // 필요 시 설정
                        .accountNumber(null) // 필요 시 설정
                        .receiptImage(null) // 필요 시 설정
                        .withdrawOwner(null) // 필요 시 설정
                        .depositOwner(null) // 필요 시 설정
                        .createdAt(LocalDateTime.now())
                        .build();
                transactionRepository.save(transaction);

                // 마지막 실행일 업데이트
                fe.setLastExecutedDate(today);
                fixedExpenseRepository.save(fe);
            }
        }
    }

    // 고정지출 등록 (추가/수정/삭제 등 필요한 메소드 확장 가능)
    public FixedExpense addFixedExpense(FixedExpense fixedExpense) {
        return fixedExpenseRepository.save(fixedExpense);
    }

    public List<FixedExpense> getFixedExpensesByUserId(Long userId) {
        return fixedExpenseRepository.findByUserIdAndIsActiveTrue(userId);
    }
}