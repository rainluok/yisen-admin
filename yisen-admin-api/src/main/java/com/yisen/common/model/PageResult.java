package com.yisen.common.model;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 通用分页响应结果
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "分页响应结果")
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 数据列表
     */
    @Schema(description = "数据列表")
    private List<T> records;

    /**
     * 总记录数
     */
    @Schema(description = "总记录数", example = "100")
    private Long total;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1")
    private Long pageNum;

    /**
     * 每页条数
     */
    @Schema(description = "每页条数", example = "10")
    private Long pageSize;

    /**
     * 总页数
     */
    @Schema(description = "总页数", example = "10")
    private Long pages;

    /**
     * 是否有上一页
     */
    @Schema(description = "是否有上一页", example = "false")
    private Boolean hasPrevious;

    /**
     * 是否有下一页
     */
    @Schema(description = "是否有下一页", example = "true")
    private Boolean hasNext;

    /**
     * 从 MyBatis-Plus 的 IPage 转换
     */
    public static <T> PageResult<T> of(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setRecords(page.getRecords());
        result.setTotal(page.getTotal());
        result.setPageNum(page.getCurrent());
        result.setPageSize(page.getSize());
        result.setPages(page.getPages());
        result.setHasPrevious(page.getCurrent() > 1);
        result.setHasNext(page.getCurrent() < page.getPages());
        return result;
    }

    /**
     * 构建空分页结果
     */
    public static <T> PageResult<T> empty() {
        PageResult<T> result = new PageResult<>();
        result.setRecords(List.of());
        result.setTotal(0L);
        result.setPageNum(1L);
        result.setPageSize(10L);
        result.setPages(0L);
        result.setHasPrevious(false);
        result.setHasNext(false);
        return result;
    }

}

