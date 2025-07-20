package com.doitmoney.backend.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.doitmoney.backend.account.entity.Account;
import java.util.List;
import java.util.Optional;

// src/main/java/com/doitmoney/backend/account/repository/AccountRepository.java

public interface AccountRepository extends JpaRepository<Account, Long> {
    // 기존 메서드는 그대로 냅두셔도 됩니다.
    List<Account> findByUserUserId(Long userId);

    // 새로 추가: 사용자+계좌번호 로 조회
    Optional<Account> findByUserUserIdAndAccountNumber(Long userId, String accountNumber);

    // (원하시면 institutionName까지 함께 쓸 수도 있습니다)
    // Optional<Account> findByUserUserIdAndInstitutionNameAndAccountNumber(
    // Long userId, String institutionName, String accountNumber);
}