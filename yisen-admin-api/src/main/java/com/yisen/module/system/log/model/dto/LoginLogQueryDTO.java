package com.yisen.module.system.log.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 登录日志查询参数
 *
 * @author rainluo
 * @date 2025-11-15
 */
@Data
@Schema(description = "登录日志查询参数")
public class LoginLogQueryDTO {

    /**
     * 用户名（模糊查询）
     */
    @Schema(description = "用户名", example = "admin")
    private String username;

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

