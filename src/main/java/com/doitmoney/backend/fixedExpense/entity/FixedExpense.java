package com.doitmoney.backend.fixedExpense.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.doitmoney.backend.account.entity.Account;
import com.doitmoney.backend.transaction.entity.TransactionType;
import com.doitmoney.backend.user.entity.User;

@Entity
@Table(name = "fixed_expense")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FixedExpense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어느 유저의 고정지출인지
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 고정 지출(또는 이체) 금액
    @Column(nullable = false)
    private Long amount;

    // 카테고리 (예: 월세, 구독료 등)
    @Column(length = 50)
    private String category;

    // 설명/메모
    @Column(length = 255)
    private String content;

    // 매월 실행할 일자 (1~31)
    @Column(name = "day_of_month", nullable = false)
    private int dayOfMonth;

    // 거래 타입 (expense: 지출, transfer: 이체, income: 수입[고정수입도 가능])
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    // (지출/이체의 경우) 출금 계좌
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id")
    private Account fromAccount;

    // (수입/이체의 경우) 입금 계좌
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id")
    private Account toAccount;

    // 고정지출 활성화 여부
    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 마지막으로 실행한 날짜 (중복 생성 방지용)
    @Column(name = "last_executed_date")
    private LocalDate lastExecutedDate;
}