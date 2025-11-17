package com.yisen.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.result.Result;
import com.yisen.common.service.RedisCache;
import com.yisen.common.util.IpUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * IP 黑名单过滤器：拦截高频异常 IP
 * 注意：不要使用 @Component，由 FilterConfig 手动注册
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
public class IpBlacklistFilter implements Filter {

    // 黑名单前缀
    private static final String BLACKLIST_KEY_PREFIX = "blacklist:ip:";
    // IP 访问频率前缀
    private static final String IP_FREQUENCY_KEY_PREFIX = "ip:frequency:";
    // 时间窗口（秒）
    private static final long TIME_WINDOW = 60;
    // 频率阈值
    private static final long FREQUENCY_THRESHOLD = 100;
    // 黑名单过期时间（小时）
    private static final long BLACKLIST_EXPIRE_HOURS = 24;
    private final RedisCache redisCache;
    private final ObjectMapper objectMapper;

    public IpBlacklistFilter(RedisCache redisCache, ObjectMapper objectMapper) {
        this.redisCache = redisCache;
        this.objectMapper = objectMapper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String ip = IpUtil.getIpAddr(httpRequest);
        String blacklistKey = BLACKLIST_KEY_PREFIX + ip;
        String frequencyKey = IP_FREQUENCY_KEY_PREFIX + ip;

        // 检查是否在黑名单中
        if (redisCache.hasKey(blacklistKey)) {
            log.warn("IP {} 在黑名单中，拒绝访问", ip);
            rejectRequest(httpResponse, "IP已被封禁，请联系管理员");
            return;
        }

        // 检查访问频率
        Long currentCount = redisCache.increment(frequencyKey);
        if (currentCount != null && currentCount == 1) {
            // 首次访问，设置过期时间
            redisCache.expire(frequencyKey, TIME_WINDOW, TimeUnit.SECONDS);
        }

        // 超过频率阈值，加入黑名单
        if (currentCount != null && currentCount > FREQUENCY_THRESHOLD) {
            log.warn("IP {} 访问频率过高（{}次/{}秒），加入黑名单", ip, currentCount, TIME_WINDOW);
            redisCache.set(blacklistKey, true, BLACKLIST_EXPIRE_HOURS, TimeUnit.HOURS);
            rejectRequest(httpResponse, "访问频率过高，已被暂时封禁");
            return;
        }

        chain.doFilter(request, response);
    }

    /**
     * 拒绝请求
     */
    private void rejectRequest(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        Result<?> result = Result.fail(ResponseCodeEnum.IP_BLACKLISTED.getCode(), message);
        response.getWriter().write(objectMapper.writeValueAsString(result));
    }
}

