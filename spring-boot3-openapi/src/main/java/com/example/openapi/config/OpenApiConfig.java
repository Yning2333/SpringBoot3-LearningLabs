package com.example.openapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "我的 API 文档",
                version = "1.0.0",
                description = "这是我的 OpenAPI 文档示例。"
        )
)
@Configuration
public class OpenApiConfig {

}
