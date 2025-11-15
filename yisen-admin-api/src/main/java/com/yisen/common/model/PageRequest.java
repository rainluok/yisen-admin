package com.yisen.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

/**
 * 通用分页请求参数
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "分页请求参数")
public class PageRequest<T> {

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1")
    @Min(value = 1, message = "页码最小为1")
    private Integer pageNum = 1;

    /**
     * 每页条数
     */
    @Schema(description = "每页条数", example = "10")
    @Min(value = 1, message = "每页条数最小为1")
    @Max(value = 100, message = "每页条数最大为100")
    private Integer pageSize = 10;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段", example = "createTime")
    private String orderBy;

    /**
     * 排序方式：asc 升序，desc 降序
     */
    @Schema(description = "排序方式", example = "desc", allowableValues = {"asc", "desc"})
    private String orderDirection = "desc";

    /**
     * 查询参数（泛型，支持各种查询条件）
     */
    @Schema(description = "查询参数")
    private T params;

    /**
     * 是否需要查询总数（默认 true）
     */
    @Schema(description = "是否需要查询总数", example = "true")
    private Boolean needCount = true;

}

