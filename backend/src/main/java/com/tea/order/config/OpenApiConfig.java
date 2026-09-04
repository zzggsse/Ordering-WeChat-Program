package com.tea.order.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("奶茶点单系统 API")
                .description("顾客端小程序 + 商家 PC 共用的 REST API")
                .version("v1.0.0"));
    }
}
