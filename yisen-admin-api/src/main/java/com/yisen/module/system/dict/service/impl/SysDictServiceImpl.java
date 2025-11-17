package com.yisen.module.system.dict.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.exception.BusinessException;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.module.system.dict.mapper.SysDictMapper;
import com.yisen.module.system.dict.model.dto.DictAddDTO;
import com.yisen.module.system.dict.model.dto.DictQueryDTO;
import com.yisen.module.system.dict.model.dto.DictUpdateDTO;
import com.yisen.module.system.dict.model.po.SysDict;
import com.yisen.module.system.dict.model.vo.DictVO;
import com.yisen.module.system.dict.service.SysDictService;
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
 * @description 针对表【sys_dict(数据字典)】的数据库操作Service实现
 * @createDate 2025-11-14 11:20:59
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict>
        implements SysDictService {

    @Override
    public PageResult<DictVO> pageDicts(PageRequest<DictQueryDTO> pageRequest) {
        // 构建分页对象
        Page<SysDict> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());

        // 构建查询条件
        DictQueryDTO queryDTO = pageRequest.getParams();
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDict::getIsDeleted, 0);

        if (queryDTO != null) {
            if (StringUtils.isNotBlank(queryDTO.getDictName())) {
                queryWrapper.like(SysDict::getDictName, queryDTO.getDictName());
            }
            if (StringUtils.isNotBlank(queryDTO.getDictCode())) {
                queryWrapper.like(SysDict::getDictCode, queryDTO.getDictCode());
            }
            if (queryDTO.getStatus() != null) {
                queryWrapper.eq(SysDict::getStatus, queryDTO.getStatus());
            }
        }

        queryWrapper.orderByDesc(SysDict::getCreateTime);

        // 查询分页数据
        IPage<SysDict> pageResult = this.page(page, queryWrapper);

        // 转换为VO
        List<DictVO> records = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(pageResult, records);
    }

    @Override
    public List<DictVO> getAllDicts(DictQueryDTO queryDTO) {
        // 构建查询条件
        LambdaQueryWrapper<SysDict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDict::getIsDeleted, 0);

        if (queryDTO != null) {
            if (StringUtils.isNotBlank(queryDTO.getDictName())) {
                queryWrapper.like(SysDict::getDictName, queryDTO.getDictName());
            }
            if (StringUtils.isNotBlank(queryDTO.getDictCode())) {
                queryWrapper.like(SysDict::getDictCode, queryDTO.getDictCode());
            }
            if (queryDTO.getStatus() != null) {
                queryWrapper.eq(SysDict::getStatus, queryDTO.getStatus());
            }
        }

        queryWrapper.orderByDesc(SysDict::getCreateTime);

        // 查询所有数据
        List<SysDict> list = this.list(queryWrapper);

        // 转换为VO
        return list.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addDict(DictAddDTO dto) {
        // 检查字典编码是否已存在
        Long count = this.lambdaQuery()
                .eq(SysDict::getDictCode, dto.getDictCode())
                .eq(SysDict::getIsDeleted, 0)
                .count();
        if (count > 0) {
            throw new BusinessException(ResponseCodeEnum.DICT_CODE_EXISTS);
        }

        // 创建字典
        SysDict dict = new SysDict();
        BeanUtils.copyProperties(dto, dict);
        dict.setIsDeleted(0);

        this.save(dict);
        return dict.getId();
    }

    @Override
    public DictVO getDictDetail(String id) {
        SysDict dict = this.getById(id);
        if (dict == null || dict.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.DICT_NOT_EXISTS);
        }

        return convertToVO(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDict(DictUpdateDTO dto) {
        // 检查字典是否存在
        SysDict dict = this.getById(dto.getId());
        if (dict == null || dict.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.DICT_NOT_EXISTS);
        }

        // 更新字典
        if (StringUtils.isNotBlank(dto.getDictName())) {
            dict.setDictName(dto.getDictName());
        }
        if (StringUtils.isNotBlank(dto.getDescription())) {
            dict.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            dict.setStatus(dto.getStatus());
        }

        this.updateById(dict);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDict(String id) {
        // 检查字典是否存在
        SysDict dict = this.getById(id);
        if (dict == null || dict.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.DICT_NOT_EXISTS);
        }

        // 逻辑删除
        dict.setIsDeleted(1);
        this.updateById(dict);
    }

    /**
     * 转换为VO
     */
    private DictVO convertToVO(SysDict dict) {
        DictVO vo = new DictVO();
        BeanUtils.copyProperties(dict, vo);
        return vo;
    }

}




