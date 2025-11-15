package com.yisen.module.system.dict.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典项VO
 *
 * @author rainluo
 * @date 2025-11-15
 */
@Data
@Schema(description = "字典项VO")
public class DictItemVO {

    /**
     * 字典项ID
     */
    @Schema(description = "字典项ID")
    private String id;

    /**
     * 字典ID
     */
    @Schema(description = "字典ID")
    private String dictId;

    /**
     * 字典项名称
     */
    @Schema(description = "字典项名称")
    private String itemName;

    /**
     * 字典项值
     */
    @Schema(description = "字典项值")
    private String itemValue;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 状态 0-禁用 1-启用
     */
    @Schema(description = "状态")
    private Integer status;

}

