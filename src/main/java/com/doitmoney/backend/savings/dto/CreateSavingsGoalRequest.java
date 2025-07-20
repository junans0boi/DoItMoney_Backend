package com.doitmoney.backend.savings.dto;

import com.doitmoney.backend.savings.entity.SavingType;

public record CreateSavingsGoalRequest(
    String title,
    Long targetAmount,
    Long targetAccountId
) {}