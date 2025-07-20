// File: Planary_Backend/src/main/java/com/Planairy/backend/repository/UserRepository.java
package com.doitmoney.backend.user.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.doitmoney.backend.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);             // ⚡
    boolean existsByEmail(String email);                  // ⚡ 실시간 중복 검사용
    boolean existsByPhone(String phone);                  // ⚡
}