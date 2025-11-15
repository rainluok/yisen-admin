package com.yisen.common.constant;

/**
 * 缓存常量key
 *
 * @author rainluo
 * @version 1.0
 * @date 2025/11/14 10:11
 */
public class CacheKey {

    /**
     * 用户信息缓存key，后接用户ID
     * 示例: user:info:123
     */
    public static final String USER_INFO = "user:info:";

    /**
     * 登录验证码
     * 示例: captcha:login:手机号
     */
    public static final String CAPTCHA_LOGIN = "captcha:login:";

    /**
     * 菜单权限缓存，后接用户ID
     * 示例: menu:permission:123
     */
    public static final String MENU_PERMISSION = "menu:permission:";

    /**
     * 角色权限缓存，后接角色ID
     * 示例: role:permission:1
     */
    public static final String ROLE_PERMISSION = "role:permission:";

    /**
     * 系统字典缓存
     */
    public static final String SYS_DICT = "sys:dict:";

    /**
     * 参数配置缓存
     */
    public static final String SYS_CONFIG = "sys:config:";

    /**
     * 登录token的缓存key，后接token
     * 示例: auth:token:uuid
     */
    public static final String AUTH_TOKEN = "auth:token:";

    /**
     * 在线用户缓存key
     */
    public static final String ONLINE_USERS = "online:users";

    /**
     * 限流标识符
     * 示例: rate:limit:ip:127.0.0.1
     */
    public static final String RATE_LIMIT = "rate:limit:";

    /**
     * 防重提交
     * 示例: repeat:submit:请求唯一标识
     */
    public static final String REPEAT_SUBMIT = "repeat:submit:";

    private CacheKey() {
        // 工具类·常量类不允许被实例化
        throw new UnsupportedOperationException("CacheKey is a utility class and cannot be instantiated");
    }
}
