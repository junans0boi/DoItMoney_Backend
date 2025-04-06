package com.doitmoney.backend.transaction.repository;

    import com.doitmoney.backend.transaction.entity.Transaction;
    import com.doitmoney.backend.transaction.entity.TransactionId;
    import org.springframework.data.jpa.repository.JpaRepository;
    import java.util.List;
    
    public interface TransactionRepository extends JpaRepository<Transaction, TransactionId> {
        List<Transaction> findByUserId(Long userId);
    }