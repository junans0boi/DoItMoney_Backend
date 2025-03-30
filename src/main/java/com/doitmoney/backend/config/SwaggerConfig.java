package com.doitmoney.backend.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "DoItMoney API 문서",
                 description = "DoItMoney API에 대한 문서입니다.",
                 version = "v1")
)
public class SwaggerConfig {

}