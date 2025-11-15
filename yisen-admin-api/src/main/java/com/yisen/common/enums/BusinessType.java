package com.yisen.common.enums;

import lombok.Getter;

/**
 * 业务类型枚举
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Getter
public enum BusinessType {

    /**
     * 用户管理
     */
    USER("user", "用户管理"),

    /**
     * 角色管理
     */
    ROLE("role", "角色管理"),

    /**
     * 菜单管理
     */
    MENU("menu", "菜单管理"),

    /**
     * 字典管理
     */
    DICT("dict", "字典管理"),

    /**
     * 部门管理
     */
    DEPT("dept", "部门管理"),

    /**
     * 岗位管理
     */
    POST("post", "岗位管理"),

    /**
     * 参数管理
     */
    CONFIG("config", "参数管理"),

    /**
     * 通知公告
     */
    NOTICE("notice", "通知公告"),

    /**
     * 操作日志
     */
    LOG("log", "操作日志"),

    /**
     * 登录日志
     */
    LOGIN_LOG("login_log", "登录日志"),

    /**
     * 文件管理
     */
    FILE("file", "文件管理"),

    /**
     * 系统监控
     */
    MONITOR("monitor", "系统监控"),

    /**
     * 定时任务
     */
    JOB("job", "定时任务"),

    /**
     * 其他
     */
    OTHER("other", "其他");

    private final String code;
    private final String description;

    BusinessType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据 code 获取枚举
     */
    public static BusinessType fromCode(String code) {
        for (BusinessType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return OTHER;
    }
}

