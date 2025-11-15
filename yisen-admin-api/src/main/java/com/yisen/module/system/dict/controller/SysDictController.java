package com.yisen.module.system.dict.controller;

import com.yisen.common.annotation.OperationLog;
import com.yisen.common.annotation.RequirePermission;
import com.yisen.common.enums.BusinessType;
import com.yisen.common.enums.OperationType;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.common.result.Result;
import com.yisen.module.system.dict.model.dto.DictAddDTO;
import com.yisen.module.system.dict.model.dto.DictQueryDTO;
import com.yisen.module.system.dict.model.dto.DictUpdateDTO;
import com.yisen.module.system.dict.model.vo.DictVO;
import com.yisen.module.system.dict.service.SysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典管理接口
 *
 * @author rainluo
 * @date 2025-11-15
 */
@Slf4j
@RestController
@RequestMapping("/sys/dict")
@RequiredArgsConstructor
@Tag(name = "字典管理", description = "字典相关接口")
public class SysDictController {

    private final SysDictService sysDictService;

    /**
     * 分页查询字典列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询字典列表", description = "支持多条件查询、排序")
    @RequirePermission("sys:dict:list")
    public Result<PageResult<DictVO>> pageDicts(
            @RequestBody @Valid PageRequest<DictQueryDTO> pageRequest) {
        PageResult<DictVO> result = sysDictService.pageDicts(pageRequest);
        return Result.success(result);
    }

    /**
     * 查询所有字典列表
     */
    @PostMapping("/list")
    @Operation(summary = "查询所有字典", description = "查询所有字典的列表")
    @RequirePermission("sys:dict:list")
    public Result<List<DictVO>> getAllDicts(@RequestBody(required = false) DictQueryDTO queryDTO) {
        List<DictVO> list = sysDictService.getAllDicts(queryDTO);
        return Result.success(list);
    }

    /**
     * 新增字典
     */
    @PostMapping("/add")
    @Operation(summary = "新增字典", description = "新增数据字典")
    @OperationLog(value = "新增字典", type = OperationType.INSERT, bizType = BusinessType.DICT)
    @RequirePermission("sys:dict:add")
    public Result<String> addDict(@RequestBody @Valid DictAddDTO dto) {
        String dictId = sysDictService.addDict(dto);
        return Result.success("新增字典成功", dictId);
    }

    /**
     * 获取字典详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取字典详情", description = "根据字典ID获取详细信息")
    @RequirePermission("sys:dict:detail")
    public Result<DictVO> getDictDetail(
            @Parameter(description = "字典ID", required = true)
            @PathVariable String id) {
        DictVO detail = sysDictService.getDictDetail(id);
        return Result.success(detail);
    }

    /**
     * 修改字典信息
     */
    @PutMapping("/update")
    @Operation(summary = "修改字典信息", description = "修改字典基本信息")
    @OperationLog(value = "修改字典信息", type = OperationType.UPDATE, bizType = BusinessType.DICT)
    @RequirePermission("sys:dict:update")
    public Result<Void> updateDict(@RequestBody @Valid DictUpdateDTO dto) {
        sysDictService.updateDict(dto);
        return Result.success("修改字典成功");
    }

    /**
     * 删除字典
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典", description = "逻辑删除字典")
    @OperationLog(value = "删除字典", type = OperationType.DELETE, bizType = BusinessType.DICT)
    @RequirePermission("sys:dict:delete")
    public Result<Void> deleteDict(
            @Parameter(description = "字典ID", required = true)
            @PathVariable String id) {
        sysDictService.deleteDict(id);
        return Result.success("删除字典成功");
    }

}

