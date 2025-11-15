package com.yisen.module.system.dict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.module.system.dict.model.dto.DictAddDTO;
import com.yisen.module.system.dict.model.dto.DictQueryDTO;
import com.yisen.module.system.dict.model.dto.DictUpdateDTO;
import com.yisen.module.system.dict.model.po.SysDict;
import com.yisen.module.system.dict.model.vo.DictVO;

import java.util.List;

/**
 * @author rainluo
 * @description 针对表【sys_dict(数据字典)】的数据库操作Service
 * @createDate 2025-11-14 11:20:59
 */
public interface SysDictService extends IService<SysDict> {

    /**
     * 分页查询字典列表
     */
    PageResult<DictVO> pageDicts(PageRequest<DictQueryDTO> pageRequest);

    /**
     * 查询所有字典列表
     */
    List<DictVO> getAllDicts(DictQueryDTO queryDTO);

    /**
     * 新增字典
     */
    String addDict(DictAddDTO dto);

    /**
     * 获取字典详情
     */
    DictVO getDictDetail(String id);

    /**
     * 修改字典
     */
    void updateDict(DictUpdateDTO dto);

    /**
     * 删除字典
     */
    void deleteDict(String id);

}
