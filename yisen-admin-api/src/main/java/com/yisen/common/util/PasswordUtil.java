package com.yisen.common.util;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.exception.BusinessException;

/**
 * @author rainluo
 * @version 1.0
 * @description 密码加密工具类（基于 BCrypt）
 * @date 2025/11/14 10:49
 */
public class PasswordUtil {

    // BCrypt 强度（cost factor），默认 12（范围 4~31）
    private static final int BCRYPT_COST = 12;

    // 使用默认的 BCrypt 实例（推荐）
    private static final BCrypt.Hasher HASHER = BCrypt.withDefaults();

    // 验证器（用于校验）
    private static final BCrypt.Verifyer VERIFIER = BCrypt.verifyer();

    /**
     * 加密明文密码
     *
     * @param rawPassword 明文密码（不能为 null）
     * @return BCrypt 哈希字符串，格式如：$2a$12$R9h/cIPz0gi.URNNX3kh2OPF...
     * @throws BusinessException 如果密码为空
     */
    public static String encode(String rawPassword) {
        if (rawPassword == null || rawPassword.isEmpty()) {
            throw new BusinessException(ResponseCodeEnum.PASSWORD_ERROR);
        }
        // 可选：处理超长密码（BCrypt 最多处理 72 字节，多余部分会被忽略）
        // 此处使用默认策略（截断），也可改为哈希后再 bcrypt（见下方注释）
        return HASHER.hashToString(BCRYPT_COST, rawPassword.toCharArray());
    }

    /**
     * 验证明文密码是否与存储的哈希匹配
     *
     * @param rawPassword     用户输入的明文密码
     * @param encodedPassword 数据库存储的 BCrypt 哈希值
     * @return true 匹配，false 不匹配或异常
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }
        return VERIFIER.verify(rawPassword.toCharArray(), encodedPassword).verified;
    }

    // =================== 高级用法（按需启用）===================

    /**
     * 【可选】对超长密码先 SHA-256 再 bcrypt（避免 72 字节限制）
     * 适用于允许用户设置极长密码的场景
     */
//    public static String encodeWithPreHash(String rawPassword) {
//        if (rawPassword == null || rawPassword.isEmpty()) {
//            throw new IllegalArgumentException("密码不能为空");
//        }
//        BCrypt.Hasher hasher = BCrypt.with(LongPasswordStrategies.sha256());
//        return hasher.hashToString(BCRYPT_COST, rawPassword.toCharArray());
//    }
}
