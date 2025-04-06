package com.doitmoney.backend.account.entity;

import com.doitmoney.backend.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore; // 추가
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 이 계좌가 속한 사용자 정보 (직렬화에서 제외)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore  // 추가: JSON 직렬화 시 user 필드 무시
    private User user;

    // 계좌 유형: BANK, CARD, CASH, ETC
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    // 은행 또는 카드명 (예: "신한은행", "국민카드")
    @Column(length = 50, nullable = false)
    private String institutionName;

    // 계좌번호, 카드번호 등 (현금인 경우 null 가능)
    @Column(length = 50)
    private String accountNumber;

    // 현재 잔액 (카드의 경우 잔액 대신 결제 예정금액 등으로 활용 가능)
    @Column(nullable = false)
    private Long balance;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}