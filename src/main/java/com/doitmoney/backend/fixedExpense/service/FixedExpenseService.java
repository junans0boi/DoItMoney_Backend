package com.doitmoney.backend.fixedExpense.service;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import com.doitmoney.backend.fixedExpense.entity.FixedExpense;
import com.doitmoney.backend.fixedExpense.repository.FixedExpenseRepository;
import com.doitmoney.backend.transaction.repository.TransactionRepository;

@Service
public class FixedExpenseService {

    private final FixedExpenseRepository feRepo;

    public FixedExpenseService(FixedExpenseRepository feRepo, TransactionRepository txRepo) {
        this.feRepo = feRepo;
    }

    // (스케줄러 로직: 변경 없음)
    @Scheduled(cron = "0 5 0 * * ?")
    public void processFixedExpenses() { /* … */ }

    public FixedExpense addFixedExpense(FixedExpense fe) {
        return feRepo.save(fe);
    }

    public List<FixedExpense> getFixedExpensesByUserId(Long userId) {
        return feRepo.findByUserUserIdAndIsActiveTrue(userId);
    }

    public FixedExpense updateFixedExpense(Long userId, Long id, FixedExpense upd) {
        var ex = feRepo.findById(id)
                 .orElseThrow(() -> new RuntimeException("고정지출 없음"));
        if (!ex.getUser().getUserId().equals(userId))
            throw new RuntimeException("본인 항목 아님");
        ex.setAmount(upd.getAmount());
        ex.setCategory(upd.getCategory());
        ex.setContent(upd.getContent());
        ex.setDayOfMonth(upd.getDayOfMonth());
        ex.setTransactionType(upd.getTransactionType());
        ex.setFromAccount(upd.getFromAccount());
        ex.setToAccount(upd.getToAccount());
        ex.setActive(upd.isActive());
        return feRepo.save(ex);
    }

    public void deleteFixedExpense(Long userId, Long id) {
        var ex = feRepo.findById(id)
                 .orElseThrow(() -> new RuntimeException("고정지출 없음"));
        if (!ex.getUser().getUserId().equals(userId))
            throw new RuntimeException("본인 항목 아님");
        feRepo.delete(ex);
    }
}