package com.yisen.common.util;

import com.yisen.common.constant.CacheKey;
import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.exception.BusinessException;
import com.yisen.common.service.RedisCache;
import com.yisen.module.system.user.model.vo.LoginUserVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

/**
 * @author rainluo
 * @version 1.0
 * @description JWT工具类，依赖io.jsonwebtoken实现
 * @date 2025/11/14 15:31
 */
@Slf4j
@Component
public class JwtUtil {


    private final String TOKEN_PREFIX = "Bearer ";
    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${jwt.secret}")
    private String SECRET;

    private Key KEY;

    @Value("${jwt.expire}")
    private long expireTimeSeconds;

    @Resource
    private RedisCache redisCache;

    /**
     * 初始化密钥（在 @Value 注入后执行）
     */
    @PostConstruct
    public void init() {
        this.KEY = Keys.hmacShaKeyFor(SECRET.getBytes());
    }

    /**
     * 生成用户Token并缓存到Redis（单端登录：踢出旧会话）
     *
     * @param loginUser 用户信息
     * @return token
     */
    public String generateToken(LoginUserVO loginUser) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        Date expire = new Date(nowMillis + expireTimeSeconds * 1000);
        String id = loginUser.getId();
        // 建议仅存必要敏感字段，如用户ID、用户名
        String token = Jwts
                .builder()
                .subject(id == null ? "" : id)
                .claim("id", id)
                .claim("username", loginUser.getUsername())
                .issuedAt(now).expiration(expire)
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();

        // 单端登录：删除旧的 token，强制踢出之前的登录会话
        redisCache.delete(CacheKey.AUTH_TOKEN + id);
        
        // 缓存新的用户信息和 token
        loginUser.setToken(token);
        redisCache.set(CacheKey.AUTH_TOKEN + id, loginUser, expireTimeSeconds);

        return token;
    }

    /**
     * 校验并解析Token
     *
     * @param token token字符串
     * @return Claims
     * @throws JwtException 校验失败抛出的异常
     */
    public Claims parseToken(String token) throws JwtException {
        return Jwts.parser()
                .setSigningKey(KEY)
                .build()
                .parseSignedClaims(token).getPayload();
    }

    /**
     * 获取token中的用户名
     *
     * @param token token字符串
     * @return 用户名
     */
    public String getUsername(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.get("username", String.class);
        } catch (JwtException e) {
            return null;
        }
    }

    /**
     * 验证Token是否有效（单端登录：校验 token 是否为最新）
     * 
     * @param token token字符串
     * @throws BusinessException Token验证失败时抛出具体异常
     */
    public void validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            if (claims == null) {
                throw new BusinessException(ResponseCodeEnum.TOKEN_INVALID);
            }

            String userId = claims.get("id", String.class);
            String username = claims.get("username", String.class);
            if (StringUtils.isEmpty(username) || StringUtils.isEmpty(userId)) {
                throw new BusinessException(ResponseCodeEnum.TOKEN_INVALID);
            }

            // 单端登录验证：从 Redis 获取当前有效的用户信息，比对 token
            LoginUserVO cachedUser = redisCache.get(CacheKey.AUTH_TOKEN + userId);
            if (cachedUser == null) {
                // Redis 中没有用户信息，说明已登出或 token 过期
                throw new BusinessException(ResponseCodeEnum.TOKEN_EXPIRED);
            }
            
            // 验证当前 token 是否与 Redis 中存储的 token 一致
            // 如果不一致，说明用户在其他地方登录了（被踢下线）
            if (!token.equals(cachedUser.getToken())) {
                throw new BusinessException(ResponseCodeEnum.TOKEN_KICKED_OUT);
            }

        } catch (JwtException e) {
            // JWT 解析异常，token 格式错误或签名验证失败
            log.error("JWT 解析异常：{}", e.getMessage());
            throw new BusinessException(ResponseCodeEnum.TOKEN_INVALID);
        }
    }

    /**
     * 从请求头中提取 Token
     */
    public String getToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(tokenHeader);
        if (StringUtils.isNotEmpty(bearerToken) && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    /**
     * 从Token获取用户信息（从Redis缓存中获取）
     *
     * @param token token字符串
     * @return 用户信息
     */
    public LoginUserVO getUserInfoFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims == null) {
            return null;
        }
        String id = claims.get("id", String.class);
        return redisCache.get(CacheKey.AUTH_TOKEN + id);
    }
}
