package com.doitmoney.backend.repository;

import com.doitmoney.backend.entity.NewsArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticleEntity, Long> {
    // 필요시 커스텀 쿼리 메서드 추가 가능
}