package com.yisen.module.system.dict.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 数据字典项
 *
 * @TableName sys_dict_item
 */
@TableName(value = "sys_dict_item")
@Data
public class SysDictItem {
    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 字典ID
     */
    private String dictId;

    /**
     * 字典项名称
     */
    private String itemName;

    /**
     * 字典项值
     */
    private String itemValue;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 状态 0-禁用 1-启用
     */
    private Integer status;

    /**
     * 逻辑删除
     */
    private Integer isDeleted;
}