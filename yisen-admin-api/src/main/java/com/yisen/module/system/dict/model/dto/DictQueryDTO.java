package com.yisen.module.system.dict.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 字典类型查询参数
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "字典类型查询参数")
public class DictQueryDTO {

    /**
     * 字典名称（模糊查询）
     */
    @Schema(description = "字典名称", example = "性别")
    private String dictName;

    /**
     * 字典编码（模糊查询）
     */
    @Schema(description = "字典编码", example = "gender")
    private String dictCode;

    /**
     * 状态 0-禁用 1-启用
     */
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

}

