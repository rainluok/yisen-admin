package com.yisen.common.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 字符编码过滤器
 * 注意：不要使用 @Component，由 FilterConfig 手动注册
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
public class CharacterEncodingFilter implements Filter {

    private static final String ENCODING = "UTF-8";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 设置请求编码
        if (httpRequest.getCharacterEncoding() == null) {
            httpRequest.setCharacterEncoding(ENCODING);
        }

        // 设置响应编码
        httpResponse.setCharacterEncoding(ENCODING);
        httpResponse.setContentType("application/json;charset=" + ENCODING);

        chain.doFilter(request, response);
    }
}

