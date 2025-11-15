package com.yisen.module.system.dict.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.exception.BusinessException;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.module.system.dict.mapper.SysDictItemMapper;
import com.yisen.module.system.dict.mapper.SysDictMapper;
import com.yisen.module.system.dict.model.dto.DictItemAddDTO;
import com.yisen.module.system.dict.model.dto.DictItemQueryDTO;
import com.yisen.module.system.dict.model.dto.DictItemUpdateDTO;
import com.yisen.module.system.dict.model.po.SysDict;
import com.yisen.module.system.dict.model.po.SysDictItem;
import com.yisen.module.system.dict.model.vo.DictItemVO;
import com.yisen.module.system.dict.service.SysDictItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rainluo
 * @description 针对表【sys_dict_item(数据字典项)】的数据库操作Service实现
 * @createDate 2025-11-14 11:20:59
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem>
        implements SysDictItemService {

    private final SysDictMapper sysDictMapper;

    @Override
    public PageResult<DictItemVO> pageDictItems(PageRequest<DictItemQueryDTO> pageRequest) {
        // 构建分页对象
        Page<SysDictItem> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());

        // 构建查询条件
        DictItemQueryDTO queryDTO = pageRequest.getParams();
        LambdaQueryWrapper<SysDictItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictItem::getIsDeleted, 0);

        if (queryDTO != null) {
            if (StringUtils.isNotBlank(queryDTO.getDictId())) {
                queryWrapper.eq(SysDictItem::getDictId, queryDTO.getDictId());
            }
            if (StringUtils.isNotBlank(queryDTO.getItemName())) {
                queryWrapper.like(SysDictItem::getItemName, queryDTO.getItemName());
            }
            if (StringUtils.isNotBlank(queryDTO.getItemValue())) {
                queryWrapper.like(SysDictItem::getItemValue, queryDTO.getItemValue());
            }
            if (queryDTO.getStatus() != null) {
                queryWrapper.eq(SysDictItem::getStatus, queryDTO.getStatus());
            }
        }

        queryWrapper.orderByAsc(SysDictItem::getSort);

        // 查询分页数据
        IPage<SysDictItem> pageResult = this.page(page, queryWrapper);

        // 转换为VO
        List<DictItemVO> records = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(pageResult, records);
    }

    @Override
    public List<DictItemVO> getItemsByDictCode(String dictCode) {
        // 根据字典编码查询字典
        SysDict dict = sysDictMapper.selectOne(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getDictCode, dictCode)
                .eq(SysDict::getIsDeleted, 0)
                .eq(SysDict::getStatus, 1));

        if (dict == null) {
            throw new BusinessException(ResponseCodeEnum.DICT_NOT_EXISTS);
        }

        // 查询字典项
        return getItemsByDictId(dict.getId());
    }

    @Override
    public List<DictItemVO> getItemsByDictId(String dictId) {
        // 构建查询条件
        LambdaQueryWrapper<SysDictItem> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDictItem::getDictId, dictId)
                .eq(SysDictItem::getIsDeleted, 0)
                .eq(SysDictItem::getStatus, 1)
                .orderByAsc(SysDictItem::getSort);

        // 查询所有数据
        List<SysDictItem> list = this.list(queryWrapper);

        // 转换为VO
        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addDictItem(DictItemAddDTO dto) {
        // 检查字典是否存在
        SysDict dict = sysDictMapper.selectById(dto.getDictId());
        if (dict == null || dict.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.DICT_NOT_EXISTS);
        }

        // 检查字典项值是否已存在
        Long count = this.lambdaQuery()
                .eq(SysDictItem::getDictId, dto.getDictId())
                .eq(SysDictItem::getItemValue, dto.getItemValue())
                .eq(SysDictItem::getIsDeleted, 0)
                .count();
        if (count > 0) {
            throw new BusinessException(ResponseCodeEnum.DICT_ITEM_VALUE_EXISTS);
        }

        // 创建字典项
        SysDictItem dictItem = new SysDictItem();
        BeanUtils.copyProperties(dto, dictItem);
        dictItem.setIsDeleted(0);

        this.save(dictItem);
        return dictItem.getId();
    }

    @Override
    public DictItemVO getDictItemDetail(String id) {
        SysDictItem dictItem = this.getById(id);
        if (dictItem == null || dictItem.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.DICT_ITEM_NOT_EXISTS);
        }

        return convertToVO(dictItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDictItem(DictItemUpdateDTO dto) {
        // 检查字典项是否存在
        SysDictItem dictItem = this.getById(dto.getId());
        if (dictItem == null || dictItem.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.DICT_ITEM_NOT_EXISTS);
        }

        // 如果修改了值，检查是否重复
        if (StringUtils.isNotBlank(dto.getItemValue()) && !dto.getItemValue().equals(dictItem.getItemValue())) {
            Long count = this.lambdaQuery()
                    .eq(SysDictItem::getDictId, dictItem.getDictId())
                    .eq(SysDictItem::getItemValue, dto.getItemValue())
                    .eq(SysDictItem::getIsDeleted, 0)
                    .ne(SysDictItem::getId, dto.getId())
                    .count();
            if (count > 0) {
                throw new BusinessException(ResponseCodeEnum.DICT_ITEM_VALUE_EXISTS);
            }
        }

        // 更新字典项
        if (StringUtils.isNotBlank(dto.getItemName())) {
            dictItem.setItemName(dto.getItemName());
        }
        if (StringUtils.isNotBlank(dto.getItemValue())) {
            dictItem.setItemValue(dto.getItemValue());
        }
        if (dto.getSort() != null) {
            dictItem.setSort(dto.getSort());
        }
        if (dto.getStatus() != null) {
            dictItem.setStatus(dto.getStatus());
        }

        this.updateById(dictItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDictItem(String id) {
        // 检查字典项是否存在
        SysDictItem dictItem = this.getById(id);
        if (dictItem == null || dictItem.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.DICT_ITEM_NOT_EXISTS);
        }

        // 逻辑删除
        dictItem.setIsDeleted(1);
        this.updateById(dictItem);
    }

    /**
     * 转换为VO
     */
    private DictItemVO convertToVO(SysDictItem dictItem) {
        DictItemVO vo = new DictItemVO();
        BeanUtils.copyProperties(dictItem, vo);
        return vo;
    }

}




