package com.doitmoney.backend.savings.dto;

import com.doitmoney.backend.savings.entity.SavingType;

public record SavingsGoalDTO(
    Long id,
    String title,
    Long targetAmount,
    Long savedAmount,
    Long targetAccountId
) {}