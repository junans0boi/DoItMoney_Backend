package com.doitmoney.backend.savings.service;

import com.doitmoney.backend.account.repository.AccountRepository;
import com.doitmoney.backend.savings.entity.SavingType;
import com.doitmoney.backend.savings.entity.SavingsGoal;
import com.doitmoney.backend.user.repository.UserRepository;
import com.doitmoney.backend.savings.repository.SavingsGoalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SavingsService {
    private final SavingsGoalRepository repo;
    private final UserRepository userRepo;
    private final AccountRepository accountRepo;

    public SavingsService(
            SavingsGoalRepository repo,
            UserRepository userRepo,
            AccountRepository accountRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
        this.accountRepo = accountRepo;
    }

    public List<SavingsGoal> getGoalsByUserId(Long userId) {
        return repo.findByUserUserIdWithTargetAccount(userId);
    }

    // SavingsService.java
    public SavingsGoal createGoal(
            Long userId,
            String title,
            Long targetAmount,
            Long targetAccountId) {
        var user = userRepo.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자 없음"));
        var acct = accountRepo.findById(targetAccountId)
                .orElseThrow(() -> new EntityNotFoundException("저축 계좌가 없습니다"));

        SavingsGoal goal = SavingsGoal.builder()
                .user(user)
                .title(title)
                .targetAmount(targetAmount)
                .targetAccount(acct)
                .savingType(SavingType.ACCOUNT)
                .savedAmount(acct.getBalance()) // ← 여기에 계좌 잔액 넣기
                .build();

        return repo.save(goal);
    }

    public SavingsGoal updateGoal(
            Long userId,
            Long goalId,
            String title,
            Long targetAmount,
            Long targetAccountId) {
        var ex = repo.findById(goalId)
                .orElseThrow(() -> new EntityNotFoundException("저축 목표 없음"));
        if (!ex.getUser().getUserId().equals(userId))
            throw new EntityNotFoundException("본인 목표가 아닙니다");

        var acct = accountRepo.findById(targetAccountId)
                .orElseThrow(() -> new EntityNotFoundException("저축 계좌 없음"));

        ex.setTitle(title);
        ex.setTargetAmount(targetAmount);
        ex.setTargetAccount(acct);

        return repo.save(ex);
    }

    public void deleteGoal(Long userId, Long goalId) {
        var ex = repo.findById(goalId)
                .orElseThrow(() -> new EntityNotFoundException("저축 목표 없음"));
        if (!ex.getUser().getUserId().equals(userId))
            throw new EntityNotFoundException("본인 목표가 아닙니다");
        repo.delete(ex);
    }
}