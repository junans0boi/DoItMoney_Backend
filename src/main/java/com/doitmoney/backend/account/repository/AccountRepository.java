package com.doitmoney.backend.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doitmoney.backend.account.entity.Account;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUserId(Long userId);
}