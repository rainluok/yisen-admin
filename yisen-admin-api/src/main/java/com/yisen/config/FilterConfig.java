package com.yisen.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yisen.common.filter.*;
import com.yisen.common.service.RedisService;
import jakarta.annotation.Resource;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Filter 配置：配置过滤器执行顺序
 * <p>
 * 说明：
 * - Filter 不使用 @Component 注解，统一在这里手动注册，避免重复注册
 * - 敏感数据脱敏已改为 AOP 切面实现（SensitiveDataAspect）
 * <p>
 * 保留的 Filter：
 * 1. CorsFilter - CORS 跨域处理
 * 2. CharacterEncodingFilter - 字符编码
 * 3. RequestBodyCacheFilter - 请求体缓存（支持多次读取）
 * 4. IpBlacklistFilter - IP 黑名单拦截
 * 5. PerformanceMonitorFilter - 性能监控
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Configuration
public class FilterConfig {

    @Resource
    private RedisService redisService;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * CORS 跨域过滤器（最高优先级）
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CorsFilter());
        registration.addUrlPatterns("/*");
        registration.setName("CorsFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registration;
    }

    /**
     * 字符编码过滤器
     * 注意：Bean名称不能用 characterEncodingFilter，会与 Spring Boot 自动配置冲突
     */
    @Bean
    public FilterRegistrationBean<CharacterEncodingFilter> customCharacterEncodingFilter() {
        FilterRegistrationBean<CharacterEncodingFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CharacterEncodingFilter());
        registration.addUrlPatterns("/*");
        registration.setName("CustomCharacterEncodingFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return registration;
    }

    /**
     * 请求体缓存过滤器
     */
    @Bean
    public FilterRegistrationBean<RequestBodyCacheFilter> requestBodyCacheFilter() {
        FilterRegistrationBean<RequestBodyCacheFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new RequestBodyCacheFilter());
        registration.addUrlPatterns("/*");
        registration.setName("RequestBodyCacheFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 2);
        return registration;
    }

    /**
     * IP 黑名单过滤器
     */
    @Bean
    public IpBlacklistFilter ipBlacklistFilterBean() {
        return new IpBlacklistFilter(redisService, objectMapper);
    }

    @Bean
    public FilterRegistrationBean<IpBlacklistFilter> ipBlacklistFilter(IpBlacklistFilter ipBlacklistFilter) {
        FilterRegistrationBean<IpBlacklistFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(ipBlacklistFilter);
        registration.addUrlPatterns("/*");
        registration.setName("IpBlacklistFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 3);
        return registration;
    }

    /**
     * 性能监控过滤器
     */
    @Bean
    public FilterRegistrationBean<PerformanceMonitorFilter> performanceMonitorFilter() {
        FilterRegistrationBean<PerformanceMonitorFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new PerformanceMonitorFilter());
        registration.addUrlPatterns("/*");
        registration.setName("PerformanceMonitorFilter");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE + 4);
        return registration;
    }

    // 注意：敏感数据脱敏已改为 AOP 切面实现（SensitiveDataAspect），无需在此配置
}

