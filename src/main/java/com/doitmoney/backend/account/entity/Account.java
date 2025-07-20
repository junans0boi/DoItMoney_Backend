package com.doitmoney.backend.account.entity;

import com.doitmoney.backend.fixedExpense.entity.FixedExpense;
import com.doitmoney.backend.transaction.entity.Transaction;
import com.doitmoney.backend.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountType accountType;

    @Column(length = 50, nullable = false)
    private String institutionName;

    @Column(length = 50)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "detail_type", length = 20, nullable = true)  // nullable=true로 변경
    private BankDetail detailType;

    @Column(nullable = false)
    private Long balance;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /** 고정지출: Account 삭제 시 연관 고정지출도 자동 삭제 */
    @Builder.Default
    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FixedExpense> fixedExpenses = new ArrayList<>();

    /** 거래: Account 삭제 시 연관 거래도 자동 삭제 */
    @Builder.Default
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions = new ArrayList<>();
}
