/* repository/PasswordResetTokenRepository.java */
package com.doitmoney.backend.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.doitmoney.backend.user.entity.PasswordResetToken;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}