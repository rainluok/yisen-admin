package com.yisen.module.system.log.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 操作日志查询参数
 *
 * @author rainluo
 * @date 2025-11-15
 */
@Data
@Schema(description = "操作日志查询参数")
public class LogQueryDTO {

    /**
     * 用户名（模糊查询）
     */
    @Schema(description = "用户名", example = "admin")
    private String username;

    /**
     * 操作类型 1-新增 2-修改 3-删除 4-查询
     */
    @Schema(description = "操作类型", example = "1", allowableValues = {"1", "2", "3", "4"})
    private Integer operationType;

    /**
     * 业务类型（如：user, order）
     */
    @Schema(description = "业务类型", example = "user")
    private String bizType;

    /**
     * 状态 0-失败 1-成功
     */
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

    /**
     * 开始时间
     */
    @Schema(description = "开始时间", example = "2025-11-01 00:00:00")
    private String startTime;

    /**
     * 结束时间
     */
    @Schema(description = "结束时间", example = "2025-11-30 23:59:59")
    private String endTime;

}

