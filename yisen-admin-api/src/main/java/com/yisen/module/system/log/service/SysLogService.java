package com.yisen.module.system.log.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.module.system.log.model.dto.LogQueryDTO;
import com.yisen.module.system.log.model.po.SysLog;
import com.yisen.module.system.log.model.vo.LogVO;

/**
 * @author rainluo
 * @description 针对表【sys_log(操作日志)】的数据库操作Service
 * @createDate 2025-11-14 13:36:46
 */
public interface SysLogService extends IService<SysLog> {

    /**
     * 分页查询操作日志列表
     */
    PageResult<LogVO> pageLogs(PageRequest<LogQueryDTO> pageRequest);

    /**
     * 获取操作日志详情
     */
    LogVO getLogDetail(String id);

    /**
     * 删除操作日志
     */
    void deleteLog(String id);

    /**
     * 批量删除操作日志
     */
    void deleteBatch(String[] ids);

    /**
     * 清空操作日志
     */
    void clearLogs();

}
