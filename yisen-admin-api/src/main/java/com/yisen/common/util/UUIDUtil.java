package com.yisen.common.util;

/**
 * @author rainluo
 * @version 1.0
 * @description
 * @date 2025/11/14 13:03
 */
public class UUIDUtil {
    public static String generateUUID() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
}
