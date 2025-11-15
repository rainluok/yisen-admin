package com.yisen.module.system.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yisen.module.system.menu.model.po.SysMenu;
import com.yisen.module.system.menu.model.vo.MenuTreeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统菜单 Mapper
 *
 * @author rainluo
 * @date 2025-11-14
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    /**
     * 查询用户可用菜单列表（根据用户ID）
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<MenuTreeVO> selectMenuListByUserId(@Param("userId") String userId);

    /**
     * 查询所有菜单列表
     *
     * @return 菜单列表
     */
    List<MenuTreeVO> selectAllMenuList();

}





