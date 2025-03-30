package com.doitmoney.backend.entity;

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
@Builder  // @Builder 추가: Transaction.builder() 사용 가능
public class Transaction {

    @Id
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;  // 회원별 거래 ID

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "transaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    // 분류 필드 추가 (예: 식비, 교통/차량, 문화생활, 등)
    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "description")
    private String description;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "receipt_image")
    private String receiptImage;

    @Column(name = "withdraw_owner")
    private String withdrawOwner;

    @Column(name = "deposit_owner")
    private String depositOwner;

    @Builder.Default
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}