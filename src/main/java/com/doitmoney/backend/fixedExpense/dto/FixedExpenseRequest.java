package com.doitmoney.backend.fixedExpense.dto;

import com.doitmoney.backend.transaction.entity.TransactionType;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FixedExpenseRequest {
    private Long amount;
    private String category;
    private String content;
    private int dayOfMonth;
    private TransactionType transactionType;
    private Long fromAccountId;
    private Long toAccountId;
    private boolean active = true;
}