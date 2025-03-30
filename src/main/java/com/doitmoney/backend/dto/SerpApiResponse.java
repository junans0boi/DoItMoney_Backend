package com.doitmoney.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SerpApiResponse {
    @JsonProperty("news_results")
    private List<NewsArticle> newsResults;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NewsArticle {
        private String title;
        private String link;
        private String snippet;
        private String date;
        private String thumbnail;
        private String content; // content 필드 추가
        private Source source;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Source {
        private String name;
        private String icon;
    }
}