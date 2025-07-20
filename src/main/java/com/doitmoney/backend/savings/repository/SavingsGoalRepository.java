// src/main/java/com/doitmoney/backend/savings/repository/SavingsGoalRepository.java
package com.doitmoney.backend.savings.repository;

import com.doitmoney.backend.savings.entity.SavingsGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SavingsGoalRepository extends JpaRepository<SavingsGoal, Long> {
    // original method (you can leave it or remove it if unused)
    List<SavingsGoal> findByUserUserId(Long userId);

    // new: fetch the targetAccount in the same query
    @Query("SELECT sg FROM SavingsGoal sg LEFT JOIN FETCH sg.targetAccount ta WHERE sg.user.userId = :userId")
    List<SavingsGoal> findByUserUserIdWithTargetAccount(@Param("userId") Long userId);
}