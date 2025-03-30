package com.doitmoney.backend.repository;

import com.doitmoney.backend.entity.FixedExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FixedExpenseRepository extends JpaRepository<FixedExpense, Long> {
    List<FixedExpense> findByUserIdAndIsActiveTrue(Long userId);
}