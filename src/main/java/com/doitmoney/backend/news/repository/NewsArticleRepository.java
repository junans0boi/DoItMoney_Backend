package com.doitmoney.backend.news.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doitmoney.backend.news.entity.NewsArticleEntity;

@Repository
public interface NewsArticleRepository extends JpaRepository<NewsArticleEntity, Long> {
    // 필요시 커스텀 쿼리 메서드 추가 가능
}