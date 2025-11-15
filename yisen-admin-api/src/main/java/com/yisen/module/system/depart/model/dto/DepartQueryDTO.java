package com.yisen.module.system.depart.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 部门查询参数
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "部门查询参数")
public class DepartQueryDTO {

    /**
     * 部门名称（模糊查询）
     */
    @Schema(description = "部门名称", example = "技术部")
    private String departName;

    /**
     * 部门编码（模糊查询）
     */
    @Schema(description = "部门编码", example = "TECH_DEPART")
    private String departCode;

    /**
     * 负责人（模糊查询）
     */
    @Schema(description = "负责人", example = "张三")
    private String leader;

    /**
     * 状态 0-禁用 1-启用
     */
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

}

