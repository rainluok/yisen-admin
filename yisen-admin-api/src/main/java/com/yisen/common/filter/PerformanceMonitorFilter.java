package com.yisen.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 请求耗时过滤器（接入 APM）
 * 注意：不要使用 @Component，由 FilterConfig 手动注册
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
public class PerformanceMonitorFilter implements Filter {

    // 慢请求阈值（毫秒）
    private static final long SLOW_REQUEST_THRESHOLD = 3000;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(request, response);
        } finally {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // 记录请求耗时
            String uri = httpRequest.getRequestURI();
            String method = httpRequest.getMethod();

            if (duration > SLOW_REQUEST_THRESHOLD) {
                log.warn("慢请求告警: {} {} 耗时 {} ms", method, uri, duration);
                // TODO: 接入 APM 系统（如 Skywalking、Prometheus）
            } else {
                log.debug("请求完成: {} {} 耗时 {} ms", method, uri, duration);
            }

            // TODO: 可以将性能数据发送到监控系统
            // apmService.recordPerformance(method, uri, duration);
        }
    }
}

