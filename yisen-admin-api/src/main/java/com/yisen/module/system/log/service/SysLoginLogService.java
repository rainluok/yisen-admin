package com.yisen.module.system.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.module.system.log.model.dto.LoginLogQueryDTO;
import com.yisen.module.system.log.model.po.SysLoginLog;
import com.yisen.module.system.log.model.vo.LoginLogVO;

/**
 * @author rainluo
 * @description 针对表【sys_login_log(登录日志)】的数据库操作Service
 * @createDate 2025-11-14 13:36:46
 */
public interface SysLoginLogService extends IService<SysLoginLog> {

    /**
     * 分页查询登录日志列表
     */
    PageResult<LoginLogVO> pageLoginLogs(PageRequest<LoginLogQueryDTO> pageRequest);

    /**
     * 获取登录日志详情
     */
    LoginLogVO getLoginLogDetail(String id);

    /**
     * 删除登录日志
     */
    void deleteLoginLog(String id);

    /**
     * 批量删除登录日志
     */
    void deleteBatch(String[] ids);

    /**
     * 清空登录日志
     */
    void clearLoginLogs();

}
