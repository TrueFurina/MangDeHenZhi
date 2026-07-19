package com.mangdehenzhi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Swagger/OpenAPI 配置 — 接口文档
 * 访问地址: http://localhost:8080/swagger-ui.html
 */
@Configuration
@Profile("!prod")
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("芒得很职 API")
                        .description("AI + 元宇宙 + 区块链 职业技能培训认证平台接口文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("芒得很职团队")
                                .email("support@mangdehenzhi.com"))
                        .license(new License()
                                .name("MIT License")))
                // 全局 JWT 认证
                .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("BearerAuth", new SecurityScheme()
                                .name("BearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("在请求头中添加 Authorization: Bearer <token>")));
    }
}