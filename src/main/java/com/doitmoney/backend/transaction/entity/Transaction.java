package com.doitmoney.backend.transaction.entity;

import com.doitmoney.backend.account.entity.Account;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@IdClass(TransactionId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private Integer amount;

    @Column(length = 50)
    private String category;

    private String description;

    /** 참고용: 실제로는 account 연관관계로도 조회 가능 */
    @Column(name = "account_name", length = 50, nullable = false)
    private String accountName;

    @Column(name = "account_number", length = 50, nullable = false)
    private String accountNumber;

    /** FK 컬럼 매핑 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private Account account;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}