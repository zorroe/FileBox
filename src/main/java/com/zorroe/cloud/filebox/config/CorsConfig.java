package com.zorroe.cloud.filebox.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 拦截所有路径
                .allowedOriginPatterns("*") // Spring Boot 2.4+ 必须用 allowedOriginPatterns 代替 allowedOrigins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false) // 如果前端带 credentials（如 cookies），需设为 true，但 origin 不能为 *
                .maxAge(3600); // 预检缓存时间（秒）
    }
}
