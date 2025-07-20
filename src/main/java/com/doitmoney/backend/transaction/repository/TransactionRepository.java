package com.doitmoney.backend.transaction.repository;

import com.doitmoney.backend.transaction.entity.Transaction;
import com.doitmoney.backend.transaction.entity.TransactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, TransactionId> {

    List<Transaction> findByUserId(Long userId);

    Optional<Transaction> findByUserIdAndId(Long userId, Integer id);

    /** AccountService.deleteAccount() 에서 사용 */
    @Transactional
    void deleteByAccount_Id(Long accountId);
}