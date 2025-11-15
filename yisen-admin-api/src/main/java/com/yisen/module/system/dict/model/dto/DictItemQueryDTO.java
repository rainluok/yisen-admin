package com.yisen.module.system.dict.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典项查询参数
 *
 * @author rainluo
 * @date 2025-11-15
 */
@Data
@Schema(description = "字典项查询参数")
public class DictItemQueryDTO {

    /**
     * 字典ID
     */
    @Schema(description = "字典ID", example = "1")
    private String dictId;

    /**
     * 字典项名称（模糊查询）
     */
    @Schema(description = "字典项名称", example = "男")
    private String itemName;

    /**
     * 字典项值（模糊查询）
     */
    @Schema(description = "字典项值", example = "male")
    private String itemValue;

    /**
     * 状态 0-禁用 1-启用
     */
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

}

