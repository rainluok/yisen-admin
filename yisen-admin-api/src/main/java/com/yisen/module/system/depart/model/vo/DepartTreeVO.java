package com.yisen.module.system.depart.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 部门树形结构VO
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@Schema(description = "部门树形结构VO")
public class DepartTreeVO {

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    private String id;

    /**
     * 父部门ID
     */
    @Schema(description = "父部门ID")
    private String parentId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String departName;

    /**
     * 部门编码
     */
    @Schema(description = "部门编码")
    private String departCode;

    /**
     * 负责人
     */
    @Schema(description = "负责人")
    private String leader;

    /**
     * 部门电话
     */
    @Schema(description = "部门电话")
    private String phone;

    /**
     * 部门邮箱
     */
    @Schema(description = "部门邮箱")
    private String email;

    /**
     * 状态 0-禁用 1-启用
     */
    @Schema(description = "状态")
    private Integer status;

    /**
     * 排序
     */
    @Schema(description = "排序")
    private Integer sort;

    /**
     * 创建人
     */
    @Schema(description = "创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
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

    /**
     * 子部门列表
     */
    @Schema(description = "子部门列表")
    private List<DepartTreeVO> children = new ArrayList<>();

}

