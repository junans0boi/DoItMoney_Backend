// src/main/java/com/doitmoney/backend/savings/controller/SavingsController.java
package com.doitmoney.backend.savings.controller;

import com.doitmoney.backend.security.CustomUserDetails;
import com.doitmoney.backend.savings.dto.*;
import com.doitmoney.backend.savings.entity.SavingType;
import com.doitmoney.backend.savings.entity.SavingsGoal;
import com.doitmoney.backend.savings.service.SavingsService;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/savings")
public class SavingsController {
    private final SavingsService svc;

    public SavingsController(SavingsService svc) {
        this.svc = svc;
    }

    
    @GetMapping("/goals")
    public List<SavingsGoalDTO> list(@AuthenticationPrincipal CustomUserDetails user) {
        return svc.getGoalsByUserId(user.getUserId()).stream()
                .map(g -> new SavingsGoalDTO(
                        g.getId(),
                        g.getTitle(),
                        g.getTargetAmount(),
                        g.getTargetAccount() != null
                                ? g.getTargetAccount().getBalance() // ← 기존 잔액 사용
                                : g.getSavedAmount(),
                        g.getTargetAccount() != null
                                ? g.getTargetAccount().getId()
                                : null))
                .collect(Collectors.toList());
    }

    @PostMapping("/goals")
    public SavingsGoalDTO create(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody CreateSavingsGoalRequest req) {
        var g = svc.createGoal(
                user.getUserId(),
                req.title(),
                req.targetAmount(),
                req.targetAccountId());
        return new SavingsGoalDTO(
                g.getId(), g.getTitle(), g.getTargetAmount(),
                g.getTargetAccount().getBalance(),
                g.getTargetAccount().getId());
    }

    @PutMapping("/goals/{id}")
    public SavingsGoalDTO update(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id,
            @RequestBody CreateSavingsGoalRequest req) {
        var g = svc.updateGoal(
                user.getUserId(),
                id,
                req.title(),
                req.targetAmount(),
                req.targetAccountId());
        return new SavingsGoalDTO(
                g.getId(), g.getTitle(), g.getTargetAmount(),
                g.getTargetAccount().getBalance(),
                g.getTargetAccount().getId());
    }

    // ← 이 부분이 있어야 합니다
    @DeleteMapping("/goals/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // (선택) 성공 시 204로 내려주려면
    public void delete(
            @AuthenticationPrincipal CustomUserDetails user,
            @PathVariable Long id) {
        svc.deleteGoal(user.getUserId(), id);
    }
}