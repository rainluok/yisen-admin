package com.yisen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author rainluo
 * @version 1.0
 * @description Web 配置类，支持静态资源映射
 * @date 2025/11/14 10:11
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 静态资源映射
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射swagger和knife4j等静态资源
        registry.addResourceHandler("/doc.html**").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        // 可按需添加其他静态资源映射
    }

    // 注意：
    // 1. CORS 配置已迁移到 CorsFilter，避免冲突
    // 2. 拦截器配置在 InterceptorConfig 中
}
