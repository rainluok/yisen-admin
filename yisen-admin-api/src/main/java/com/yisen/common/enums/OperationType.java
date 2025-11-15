package com.yisen.common.enums;

import lombok.Getter;

/**
 * 操作类型枚举
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Getter
public enum OperationType {

    /**
     * 新增
     */
    INSERT(1, "新增"),

    /**
     * 修改
     */
    UPDATE(2, "修改"),

    /**
     * 删除
     */
    DELETE(3, "删除"),

    /**
     * 查询
     */
    SELECT(4, "查询"),

    /**
     * 导出
     */
    EXPORT(5, "导出"),

    /**
     * 导入
     */
    IMPORT(6, "导入"),

    /**
     * 授权
     */
    GRANT(7, "授权"),

    /**
     * 其他
     */
    OTHER(0, "其他");

    private final int code;
    private final String description;

    OperationType(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据 code 获取枚举
     */
    public static OperationType valueOf(int code) {
        for (OperationType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return OTHER;
    }
}

