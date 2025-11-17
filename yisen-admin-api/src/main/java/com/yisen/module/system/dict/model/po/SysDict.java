package com.yisen.module.system.dict.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 数据字典
 *
 * @TableName sys_dict
 */
@TableName(value = "sys_dict")
@Data
public class SysDict {
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 字典名称
     */
    private String dictName;

    /**
     * 字典编码
     */
    private String dictCode;

    /**
     * 描述
     */
    private String description;

    /**
     * 状态 0-禁用 1-启用
     */
    private Integer status;

    /**
     * 逻辑删除
     */
    private Integer isDeleted;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}