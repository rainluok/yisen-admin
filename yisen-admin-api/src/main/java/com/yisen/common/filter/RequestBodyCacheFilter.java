package com.yisen.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 请求体缓存过滤器（支持多次读取）
 * 注意：不要使用 @Component，由 FilterConfig 手动注册
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
public class RequestBodyCacheFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;

            // 只缓存 POST/PUT/PATCH 请求的请求体
            String method = httpRequest.getMethod();
            if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) ||
                    "PATCH".equalsIgnoreCase(method)) {

                // 使用可重复读取的请求包装器
                CachedBodyHttpServletRequest cachedRequest =
                        new CachedBodyHttpServletRequest(httpRequest);
                chain.doFilter(cachedRequest, response);
                return;
            }
        }

        chain.doFilter(request, response);
    }
}

