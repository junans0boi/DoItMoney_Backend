package com.doitmoney.backend.repository;

    import com.doitmoney.backend.entity.Transaction;
    import com.doitmoney.backend.entity.TransactionId;
    import org.springframework.data.jpa.repository.JpaRepository;
    import java.util.List;
    
    public interface TransactionRepository extends JpaRepository<Transaction, TransactionId> {
        List<Transaction> findByUserId(Long userId);
    }