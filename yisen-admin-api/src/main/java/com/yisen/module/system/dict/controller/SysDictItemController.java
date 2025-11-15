package com.yisen.module.system.dict.controller;

import com.yisen.common.annotation.OperationLog;
import com.yisen.common.annotation.RequirePermission;
import com.yisen.common.enums.BusinessType;
import com.yisen.common.enums.OperationType;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.common.result.Result;
import com.yisen.module.system.dict.model.dto.DictItemAddDTO;
import com.yisen.module.system.dict.model.dto.DictItemQueryDTO;
import com.yisen.module.system.dict.model.dto.DictItemUpdateDTO;
import com.yisen.module.system.dict.model.vo.DictItemVO;
import com.yisen.module.system.dict.service.SysDictItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典项管理接口
 *
 * @author rainluo
 * @date 2025-11-15
 */
@Slf4j
@RestController
@RequestMapping("/sys/dict-item")
@RequiredArgsConstructor
@Tag(name = "字典项管理", description = "字典项相关接口")
public class SysDictItemController {

    private final SysDictItemService sysDictItemService;

    /**
     * 分页查询字典项列表
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询字典项列表", description = "支持多条件查询、排序")
    @RequirePermission("sys:dict:list")
    public Result<PageResult<DictItemVO>> pageDictItems(
            @RequestBody @Valid PageRequest<DictItemQueryDTO> pageRequest) {
        PageResult<DictItemVO> result = sysDictItemService.pageDictItems(pageRequest);
        return Result.success(result);
    }

    /**
     * 根据字典编码查询字典项
     */
    @GetMapping("/list/{dictCode}")
    @Operation(summary = "根据字典编码查询字典项", description = "用于前端下拉选择等场景")
    public Result<List<DictItemVO>> getItemsByDictCode(
            @Parameter(description = "字典编码", required = true)
            @PathVariable String dictCode) {
        List<DictItemVO> list = sysDictItemService.getItemsByDictCode(dictCode);
        return Result.success(list);
    }

    /**
     * 根据字典ID查询字典项
     */
    @GetMapping("/list-by-id/{dictId}")
    @Operation(summary = "根据字典ID查询字典项", description = "查询指定字典的所有字典项")
    @RequirePermission("sys:dict:list")
    public Result<List<DictItemVO>> getItemsByDictId(
            @Parameter(description = "字典ID", required = true)
            @PathVariable String dictId) {
        List<DictItemVO> list = sysDictItemService.getItemsByDictId(dictId);
        return Result.success(list);
    }

    /**
     * 新增字典项
     */
    @PostMapping("/add")
    @Operation(summary = "新增字典项", description = "新增数据字典项")
    @OperationLog(value = "新增字典项", type = OperationType.INSERT, bizType = BusinessType.DICT)
    @RequirePermission("sys:dict:add")
    public Result<String> addDictItem(@RequestBody @Valid DictItemAddDTO dto) {
        String itemId = sysDictItemService.addDictItem(dto);
        return Result.success("新增字典项成功", itemId);
    }

    /**
     * 获取字典项详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取字典项详情", description = "根据字典项ID获取详细信息")
    @RequirePermission("sys:dict:detail")
    public Result<DictItemVO> getDictItemDetail(
            @Parameter(description = "字典项ID", required = true)
            @PathVariable String id) {
        DictItemVO detail = sysDictItemService.getDictItemDetail(id);
        return Result.success(detail);
    }

    /**
     * 修改字典项信息
     */
    @PutMapping("/update")
    @Operation(summary = "修改字典项信息", description = "修改字典项基本信息")
    @OperationLog(value = "修改字典项信息", type = OperationType.UPDATE, bizType = BusinessType.DICT)
    @RequirePermission("sys:dict:update")
    public Result<Void> updateDictItem(@RequestBody @Valid DictItemUpdateDTO dto) {
        sysDictItemService.updateDictItem(dto);
        return Result.success("修改字典项成功");
    }

    /**
     * 删除字典项
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典项", description = "逻辑删除字典项")
    @OperationLog(value = "删除字典项", type = OperationType.DELETE, bizType = BusinessType.DICT)
    @RequirePermission("sys:dict:delete")
    public Result<Void> deleteDictItem(
            @Parameter(description = "字典项ID", required = true)
            @PathVariable String id) {
        sysDictItemService.deleteDictItem(id);
        return Result.success("删除字典项成功");
    }

}

