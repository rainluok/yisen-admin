package com.yisen.module.system.dict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.module.system.dict.model.dto.DictItemAddDTO;
import com.yisen.module.system.dict.model.dto.DictItemQueryDTO;
import com.yisen.module.system.dict.model.dto.DictItemUpdateDTO;
import com.yisen.module.system.dict.model.po.SysDictItem;
import com.yisen.module.system.dict.model.vo.DictItemVO;

import java.util.List;

/**
 * @author rainluo
 * @description 针对表【sys_dict_item(数据字典项)】的数据库操作Service
 * @createDate 2025-11-14 11:20:59
 */
public interface SysDictItemService extends IService<SysDictItem> {

    /**
     * 分页查询字典项列表
     */
    PageResult<DictItemVO> pageDictItems(PageRequest<DictItemQueryDTO> pageRequest);

    /**
     * 根据字典编码查询字典项列表
     */
    List<DictItemVO> getItemsByDictCode(String dictCode);

    /**
     * 根据字典ID查询字典项列表
     */
    List<DictItemVO> getItemsByDictId(String dictId);

    /**
     * 新增字典项
     */
    String addDictItem(DictItemAddDTO dto);

    /**
     * 获取字典项详情
     */
    DictItemVO getDictItemDetail(String id);

    /**
     * 修改字典项
     */
    void updateDictItem(DictItemUpdateDTO dto);

    /**
     * 删除字典项
     */
    void deleteDictItem(String id);

}
