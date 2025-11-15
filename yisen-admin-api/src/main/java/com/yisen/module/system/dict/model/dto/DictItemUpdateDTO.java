package com.yisen.module.system.dict.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 修改字典项请求参数
 *
 * @author rainluo
 * @date 2025-11-15
 */
@Data
@Schema(description = "修改字典项请求参数")
public class DictItemUpdateDTO {

    /**
     * 字典项ID
     */
    @NotBlank(message = "字典项ID不能为空")
    @Schema(description = "字典项ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    /**
     * 字典项名称
     */
    @Size(max = 50, message = "字典项名称长度不能超过50个字符")
    @Schema(description = "字典项名称", example = "男")
    private String itemName;

    /**
     * 字典项值
     */
    @Size(max = 50, message = "字典项值长度不能超过50个字符")
    @Schema(description = "字典项值", example = "male")
    private String itemValue;

    /**
     * 排序
     */
    @Schema(description = "排序", example = "1")
    private Integer sort;

    /**
     * 状态 0-禁用 1-启用
     */
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

}

