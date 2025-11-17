package com.yisen.module.system.dict.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典类型VO
 *
 * @author rainluo
 * @date 2025-11-15
 */
@Data
@Schema(description = "字典类型VO")
public class DictVO {

    /**
     * 字典ID
     */
    @Schema(description = "字典ID")
    private String id;

    /**
     * 字典名称
     */
    @Schema(description = "字典名称")
    private String dictName;

    /**
     * 字典编码
     */
    @Schema(description = "字典编码")
    private String dictCode;

    /**
     * 描述
     */
    @Schema(description = "描述")
    private String description;

    /**
     * 状态 0-禁用 1-启用
     */
    @Schema(description = "状态")
    private Integer status;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人")
    private String updateBy;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;

}

