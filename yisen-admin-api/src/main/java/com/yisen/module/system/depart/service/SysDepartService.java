package com.yisen.module.system.depart.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.module.system.depart.model.dto.DepartAddDTO;
import com.yisen.module.system.depart.model.dto.DepartQueryDTO;
import com.yisen.module.system.depart.model.dto.DepartUpdateDTO;
import com.yisen.module.system.depart.model.po.SysDepart;
import com.yisen.module.system.depart.model.vo.DepartTreeVO;

import java.util.List;

/**
 * @author rainluo
 * @description 针对表【sys_depart(系统部门表)】的数据库操作Service
 * @createDate 2025-11-14 20:52:59
 */
public interface SysDepartService extends IService<SysDepart> {

    /**
     * 分页查询部门列表
     */
    PageResult<DepartTreeVO> pageDeparts(PageRequest<DepartQueryDTO> pageRequest);

    /**
     * 查询所有部门列表
     */
    List<DepartTreeVO> getAllDeparts(DepartQueryDTO queryDTO);

    /**
     * 获取部门树形结构
     */
    List<DepartTreeVO> getDepartTree();

    /**
     * 新增部门
     */
    String addDepart(DepartAddDTO dto);

    /**
     * 获取部门详情
     */
    DepartTreeVO getDepartDetail(String id);

    /**
     * 修改部门
     */
    void updateDepart(DepartUpdateDTO dto);

    /**
     * 删除部门
     */
    void deleteDepart(String id);

    /**
     * 启用/禁用部门
     */
    void updateDepartStatus(String id, Integer status);

}
