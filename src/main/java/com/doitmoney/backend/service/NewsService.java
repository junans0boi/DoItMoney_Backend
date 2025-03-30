package com.doitmoney.backend.service;

import com.doitmoney.backend.dto.SerpApiResponse;
import com.doitmoney.backend.entity.NewsArticleEntity;
import com.doitmoney.backend.repository.NewsArticleRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Service
public class NewsService {

    @Value("${serp.api.key}")
    private String serpApiKey;

    @Value("${serp.api.url}")
    private String serpApiUrl;

    private final RestTemplate restTemplate;
    private final NewsArticleRepository newsArticleRepository;

    public NewsService(RestTemplate restTemplate, NewsArticleRepository newsArticleRepository) {
        this.restTemplate = restTemplate;
        this.newsArticleRepository = newsArticleRepository;
    }

    // 매일 오전 8시에 DB에 뉴스 저장 (전날 뉴스 삭제 후 저장)
    @Scheduled(cron = "0 0 8 * * ?")
    public void fetchAndCacheNews() {
        // 기존 뉴스 삭제 (전날 뉴스 삭제)
        newsArticleRepository.deleteAll();

        // 금융 관련 뉴스 조회 (쿼리: 저축, 적금, 재테크)
        SerpApiResponse response = fetchFinancialNews();

        if (response != null && response.getNewsResults() != null) {
            for (SerpApiResponse.NewsArticle article : response.getNewsResults()) {
                NewsArticleEntity entity = new NewsArticleEntity();
                entity.setTitle(article.getTitle());
                entity.setLink(article.getLink());
                entity.setSnippet(article.getSnippet());
                entity.setDate(article.getDate());
                entity.setThumbnail(article.getThumbnail());
                if (article.getSource() != null) {
                    entity.setSourceName(article.getSource().getName());
                    entity.setSourceIcon(article.getSource().getIcon());
                }
                // 기사 링크를 기반으로 본문 내용을 추출
                String content = fetchArticleContent(article.getLink());
                entity.setContent(content);
                newsArticleRepository.save(entity);
            }
        }
        System.out.println("[뉴스 DB 저장 완료]");
    }

    // SERP API를 호출하여 금융 관련 뉴스 조회
    public SerpApiResponse fetchFinancialNews() {
        // 쿼리를 금융 관련 주제로 수정 (저축, 적금, 재테크)
        String query = "저축 OR 적금 OR 재테크";
        String queryUrl = serpApiUrl + "?engine=google_news&q=" + query + "&hl=ko&num=10&api_key=" + serpApiKey;
        return restTemplate.getForObject(queryUrl, SerpApiResponse.class);
    }

    // DB에 저장된 뉴스 목록 조회
    public List<NewsArticleEntity> getStoredNews() {
        return newsArticleRepository.findAll();
    }

    // 기사 링크의 HTML을 파싱하여 본문 내용을 추출하는 메서드
    private String fetchArticleContent(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            // 간단한 방법: <article> 태그가 있다면 해당 태그의 텍스트를 사용
            Element articleElement = doc.selectFirst("article");
            if (articleElement != null) {
                return articleElement.text();
            }
            // <article> 태그가 없다면 body 전체 텍스트를 반환 (사이트마다 다르게 조정 필요)
            return doc.body().text();
        } catch (Exception e) {
            e.printStackTrace();
            // 오류 발생 시 빈 문자열 반환
            return "";
        }
    }
}