package com.doitmoney.backend.fixedExpense.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.doitmoney.backend.fixedExpense.entity.FixedExpense;

import jakarta.transaction.Transactional;

import java.util.List;

public interface FixedExpenseRepository extends JpaRepository<FixedExpense, Long> {
    List<FixedExpense> findByUserUserIdAndIsActiveTrue(Long userId);

    @Transactional
    void deleteByFromAccount_Id(Long accountId);

}