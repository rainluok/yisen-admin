package com.yisen.module.system.depart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.exception.BusinessException;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.module.system.depart.mapper.SysDepartMapper;
import com.yisen.module.system.depart.model.dto.DepartAddDTO;
import com.yisen.module.system.depart.model.dto.DepartQueryDTO;
import com.yisen.module.system.depart.model.dto.DepartUpdateDTO;
import com.yisen.module.system.depart.model.po.SysDepart;
import com.yisen.module.system.depart.model.vo.DepartTreeVO;
import com.yisen.module.system.depart.service.SysDepartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rainluo
 * @description 针对表【sys_depart(系统部门表)】的数据库操作Service实现
 * @createDate 2025-11-14 20:52:59
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysDepartServiceImpl extends ServiceImpl<SysDepartMapper, SysDepart> implements SysDepartService {

    @Override
    public PageResult<DepartTreeVO> pageDeparts(PageRequest<DepartQueryDTO> pageRequest) {
        // 构建分页对象
        Page<SysDepart> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());

        // 构建查询条件
        DepartQueryDTO queryDTO = pageRequest.getParams();
        LambdaQueryWrapper<SysDepart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDepart::getIsDeleted, 0);

        if (queryDTO != null) {
            if (StringUtils.isNotBlank(queryDTO.getDepartName())) {
                queryWrapper.like(SysDepart::getDepartName, queryDTO.getDepartName());
            }
            if (StringUtils.isNotBlank(queryDTO.getDepartCode())) {
                queryWrapper.like(SysDepart::getDepartCode, queryDTO.getDepartCode());
            }
            if (StringUtils.isNotBlank(queryDTO.getLeader())) {
                queryWrapper.like(SysDepart::getLeader, queryDTO.getLeader());
            }
            if (queryDTO.getStatus() != null) {
                queryWrapper.eq(SysDepart::getStatus, queryDTO.getStatus());
            }
        }

        queryWrapper.orderByAsc(SysDepart::getSort);

        // 查询分页数据
        IPage<SysDepart> pageResult = this.page(page, queryWrapper);

        // 转换为VO
        List<DepartTreeVO> records = pageResult.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());
        PageResult<DepartTreeVO> result = new PageResult<>();
        result.setTotal(pageResult.getTotal());
        result.setRecords(records);
        result.setPageNum(pageResult.getCurrent());
        result.setPageSize(pageResult.getSize());
        result.setPages(pageResult.getPages());
        result.setHasPrevious(page.getCurrent() > 1);
        result.setHasNext(page.getCurrent() < page.getPages());
        return result;
    }

    @Override
    public List<DepartTreeVO> getAllDeparts(DepartQueryDTO queryDTO) {
        // 构建查询条件
        LambdaQueryWrapper<SysDepart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysDepart::getIsDeleted, 0);

        if (queryDTO != null) {
            if (StringUtils.isNotBlank(queryDTO.getDepartName())) {
                queryWrapper.like(SysDepart::getDepartName, queryDTO.getDepartName());
            }
            if (StringUtils.isNotBlank(queryDTO.getDepartCode())) {
                queryWrapper.like(SysDepart::getDepartCode, queryDTO.getDepartCode());
            }
            if (StringUtils.isNotBlank(queryDTO.getLeader())) {
                queryWrapper.like(SysDepart::getLeader, queryDTO.getLeader());
            }
            if (queryDTO.getStatus() != null) {
                queryWrapper.eq(SysDepart::getStatus, queryDTO.getStatus());
            }
        }

        queryWrapper.orderByAsc(SysDepart::getSort);

        // 查询所有数据
        List<SysDepart> list = this.list(queryWrapper);

        // 转换为VO
        return list.stream().map(this::convertToVO).collect(Collectors.toList());
    }

    @Override
    public List<DepartTreeVO> getDepartTree() {
        // 查询所有未删除的部门
        List<SysDepart> allDeparts = this.lambdaQuery().eq(SysDepart::getIsDeleted, 0).eq(SysDepart::getStatus, 1).orderByAsc(SysDepart::getSort).list();

        // 转换为VO
        List<DepartTreeVO> voList = allDeparts.stream().map(this::convertToVO).collect(Collectors.toList());

        // 构建树形结构
        return buildTree(voList, "0");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addDepart(DepartAddDTO dto) {
        // 检查部门编码是否已存在
        Long count = this.lambdaQuery().eq(SysDepart::getDepartCode, dto.getDepartCode()).eq(SysDepart::getIsDeleted, 0).count();
        if (count > 0) {
            throw new BusinessException(ResponseCodeEnum.DEPART_CODE_EXISTS);
        }

        // 如果有父部门，检查父部门是否存在
        if (StringUtils.isNotBlank(dto.getParentId()) && !"0".equals(dto.getParentId())) {
            SysDepart parentDepart = this.getById(dto.getParentId());
            if (parentDepart == null || parentDepart.getIsDeleted() == 1) {
                throw new BusinessException(ResponseCodeEnum.PARENT_DEPART_NOT_EXISTS);
            }
        }

        // 创建部门
        SysDepart depart = new SysDepart();
        BeanUtils.copyProperties(dto, depart);
        depart.setIsDeleted(0);

        this.save(depart);
        return depart.getId();
    }

    @Override
    public DepartTreeVO getDepartDetail(String id) {
        SysDepart depart = this.getById(id);
        if (depart == null || depart.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.DEPART_NOT_FOUND);
        }

        return convertToVO(depart);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDepart(DepartUpdateDTO dto) {
        // 检查部门是否存在
        SysDepart depart = this.getById(dto.getId());
        if (depart == null || depart.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.DEPART_NOT_FOUND);
        }

        // 如果修改了父部门，检查是否会造成循环引用
        if (StringUtils.isNotBlank(dto.getParentId()) && !dto.getParentId().equals(depart.getParentId())) {
            if (dto.getParentId().equals(dto.getId())) {
                throw new BusinessException(ResponseCodeEnum.DEPART_PARENT_CANNOT_BE_SELF);
            }
            // 检查父部门是否存在
            if (!"0".equals(dto.getParentId())) {
                SysDepart parentDepart = this.getById(dto.getParentId());
                if (parentDepart == null || parentDepart.getIsDeleted() == 1) {
                    throw new BusinessException(ResponseCodeEnum.PARENT_DEPART_NOT_EXISTS);
                }
            }
        }

        // 更新部门
        BeanUtils.copyProperties(dto, depart);

        this.updateById(depart);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDepart(String id) {
        // 检查部门是否存在
        SysDepart depart = this.getById(id);
        if (depart == null || depart.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.DEPART_NOT_FOUND);
        }

        // 检查是否有子部门
        Long childCount = this.lambdaQuery().eq(SysDepart::getParentId, id).eq(SysDepart::getIsDeleted, 0).count();
        if (childCount > 0) {
            throw new BusinessException(ResponseCodeEnum.DEPART_HAS_CHILDREN);
        }

        // 逻辑删除
        depart.setIsDeleted(1);
        this.updateById(depart);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDepartStatus(String id, Integer status) {
        // 检查部门是否存在
        SysDepart depart = this.getById(id);
        if (depart == null || depart.getIsDeleted() == 1) {
            throw new BusinessException(ResponseCodeEnum.DEPART_NOT_FOUND);
        }

        // 更新状态
        depart.setStatus(status);
        this.updateById(depart);
    }

    /**
     * 转换为VO
     */
    private DepartTreeVO convertToVO(SysDepart depart) {
        DepartTreeVO vo = new DepartTreeVO();
        BeanUtils.copyProperties(depart, vo);
        return vo;
    }

    /**
     * 构建树形结构
     */
    private List<DepartTreeVO> buildTree(List<DepartTreeVO> allDeparts, String parentId) {
        List<DepartTreeVO> tree = new ArrayList<>();

        for (DepartTreeVO depart : allDeparts) {
            if (parentId.equals(depart.getParentId())) {
                // 递归查找子节点
                List<DepartTreeVO> children = buildTree(allDeparts, depart.getId());
                depart.setChildren(children);
                tree.add(depart);
            }
        }

        return tree;
    }

}




