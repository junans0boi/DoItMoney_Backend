package com.doitmoney.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // (1) Spring MVC 레벨에서 설정한 CORS를 Security에서도 적용
            .cors(Customizer.withDefaults())

            // (2) 개발 단계에서는 CSRF를 끄는 경우가 많습니다. (운영 단계에서는 주의)
            .csrf().disable()

            // (3) URL별로 인증/인가 설정. 여기서는 간단히 모두 허용
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll()
            );

        return http.build();
    }
}