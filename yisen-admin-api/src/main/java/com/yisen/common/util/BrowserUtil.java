package com.yisen.common.util;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author rainluo
 * @version 1.0
 * @description 浏览器工具类，提供常用浏览器操作相关方法
 * @date 2025/11/14 13:48
 */
public class BrowserUtil {

    private BrowserUtil() {
        // 工具类禁止实例化
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * 根据User-Agent判断是否为移动端浏览器
     *
     * @param userAgent 请求头中的User-Agent
     * @return 是否为移动端（true/false）
     */
    public static boolean isMobile(String userAgent) {
        if (userAgent == null) return false;
        String ua = userAgent.toLowerCase();
        return ua.contains("android")
                || ua.contains("iphone")
                || ua.contains("ipad")
                || ua.contains("ipod")
                || ua.contains("windows phone")
                || ua.contains("mqqbrowser")
                || ua.contains("mobile");
    }

    /**
     * 获取浏览器完整信息，包括类型和版本号
     *
     * @param request HttpServletRequest对象
     * @return 浏览器完整信息
     */
    public static String getBrowserInfo(HttpServletRequest request) {
        if (request == null) {
            return "Unknown";
        }
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            return "Unknown";
        }
        String ua = userAgent.toLowerCase();
        String browser = "Other";
        String version = "";

        if (ua.contains("msie")) {
            browser = "IE";
            int idx = ua.indexOf("msie");
            if (idx > -1) {
                int start = idx + 5;
                int end = ua.indexOf(";", start);
                if (end > start) {
                    version = ua.substring(start, end).trim();
                }
            }
        } else if (ua.contains("trident")) {
            browser = "IE";
            int idx = ua.indexOf("rv:");
            if (idx > -1) {
                int start = idx + 3;
                int end = ua.indexOf(")", start);
                if (end > start) {
                    version = ua.substring(start, end).trim();
                }
            }
        } else if (ua.contains("edg/")) { // 新版Edge (Chromium)
            browser = "Edge";
            int idx = ua.indexOf("edg/");
            if (idx > -1) {
                int start = idx + 4;
                int end = ua.indexOf(" ", start);
                if (end == -1) end = ua.length();
                version = ua.substring(start, end).trim();
            }
        } else if (ua.contains("opr/") || ua.contains("opera")) {
            browser = "Opera";
            int idx = ua.indexOf("opr/");
            if (idx > -1) {
                int start = idx + 4;
                int end = ua.indexOf(" ", start);
                if (end == -1) end = ua.length();
                version = ua.substring(start, end).trim();
            } else {
                idx = ua.indexOf("opera/");
                if (idx > -1) {
                    int start = idx + 6;
                    int end = ua.indexOf(" ", start);
                    if (end == -1) end = ua.length();
                    version = ua.substring(start, end).trim();
                }
            }
        } else if (ua.contains("chrome")) {
            browser = "Chrome";
            int idx = ua.indexOf("chrome/");
            if (idx > -1) {
                int start = idx + 7;
                int end = ua.indexOf(" ", start);
                if (end == -1) end = ua.length();
                version = ua.substring(start, end).trim();
            }
        } else if (ua.contains("firefox")) {
            browser = "Firefox";
            int idx = ua.indexOf("firefox/");
            if (idx > -1) {
                int start = idx + 8;
                int end = ua.indexOf(" ", start);
                if (end == -1) end = ua.length();
                version = ua.substring(start, end).trim();
            }
        } else if (ua.contains("safari")) {
            browser = "Safari";
            int idx = ua.indexOf("version/");
            if (idx > -1) {
                int start = idx + 8;
                int end = ua.indexOf(" ", start);
                if (end == -1) end = ua.length();
                version = ua.substring(start, end).trim();
            }
        }

        if (!version.isEmpty()) {
            return browser + " " + version;
        }
        return browser;
    }

    /**
     * 获取操作系统类型
     *
     * @param request HttpServletRequest对象
     * @return 操作系统名称
     */
    public static String getOS(HttpServletRequest request) {
        if (request == null) {
            return "Unknown";
        }
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            return "Unknown";
        }
        String ua = userAgent.toLowerCase();
        if (ua.contains("windows")) {
            return "Windows";
        }
        if (ua.contains("mac os") || ua.contains("macintosh")) {
            return "MacOS";
        }
        if (ua.contains("android")) {
            return "Android";
        }
        if (ua.contains("iphone") || ua.contains("ipad") || ua.contains("ipod")) {
            return "iOS";
        }
        if (ua.contains("linux")) {
            return "Linux";
        }
        return "Other";
    }
}
