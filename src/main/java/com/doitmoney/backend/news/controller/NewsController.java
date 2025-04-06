package com.doitmoney.backend.news.controller;

import com.doitmoney.backend.news.service.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    // DB에 저장된 오늘의 금융 관련 뉴스 반환
    @GetMapping("/today")
    public Object getTodayFinancialNews() {
        return newsService.getStoredNews();
    }

    // 강제로 DB에 뉴스 업데이트를 트리거하는 엔드포인트
    @GetMapping("/force-update")
    public String forceUpdateNews() {
        newsService.fetchAndCacheNews();
        return "뉴스 DB 업데이트가 강제로 수행되었습니다.";
    }
}