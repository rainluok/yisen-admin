package com.yisen.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * CORS 跨域过滤器（开发环境）
 * 注意：不要使用 @Component，由 FilterConfig 手动注册
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 允许的域名（生产环境应该配置具体域名）
        httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));
        // 允许携带凭证
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        // 允许的请求方法
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        // 允许的请求头
        httpResponse.setHeader("Access-Control-Allow-Headers",
                "Authorization, Content-Type, X-Requested-With, Accept, Origin");
        // 预检请求的有效期（秒）
        httpResponse.setHeader("Access-Control-Max-Age", "3600");
        // 允许暴露的响应头
        httpResponse.setHeader("Access-Control-Expose-Headers",
                "Authorization, Content-Type, X-Total-Count");

        // 处理 OPTIONS 预检请求
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }
}

