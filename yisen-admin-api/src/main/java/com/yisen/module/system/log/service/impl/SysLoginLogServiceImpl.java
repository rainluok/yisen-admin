package com.yisen.module.system.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.exception.BusinessException;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.module.system.log.mapper.SysLoginLogMapper;
import com.yisen.module.system.log.model.dto.LoginLogQueryDTO;
import com.yisen.module.system.log.model.po.SysLoginLog;
import com.yisen.module.system.log.model.vo.LoginLogVO;
import com.yisen.module.system.log.service.SysLoginLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rainluo
 * @description 针对表【sys_login_log(登录日志)】的数据库操作Service实现
 * @createDate 2025-11-14 13:36:46
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysLoginLogServiceImpl extends ServiceImpl<SysLoginLogMapper, SysLoginLog>
        implements SysLoginLogService {

    @Override
    public PageResult<LoginLogVO> pageLoginLogs(PageRequest<LoginLogQueryDTO> pageRequest) {
        // 构建分页对象
        Page<SysLoginLog> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());

        // 构建查询条件
        LoginLogQueryDTO queryDTO = pageRequest.getParams();
        LambdaQueryWrapper<SysLoginLog> queryWrapper = new LambdaQueryWrapper<>();

        if (queryDTO != null) {
            if (StringUtils.isNotBlank(queryDTO.getUsername())) {
                queryWrapper.like(SysLoginLog::getUsername, queryDTO.getUsername());
            }
            if (queryDTO.getStatus() != null) {
                queryWrapper.eq(SysLoginLog::getStatus, queryDTO.getStatus());
            }
            if (StringUtils.isNotBlank(queryDTO.getStartTime())) {
                queryWrapper.ge(SysLoginLog::getLoginTime, queryDTO.getStartTime());
            }
            if (StringUtils.isNotBlank(queryDTO.getEndTime())) {
                queryWrapper.le(SysLoginLog::getLoginTime, queryDTO.getEndTime());
            }
        }

        queryWrapper.orderByDesc(SysLoginLog::getLoginTime);

        // 查询分页数据
        IPage<SysLoginLog> pageResult = this.page(page, queryWrapper);

        // 转换为VO
        List<LoginLogVO> records = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(pageResult, records);
    }

    @Override
    public LoginLogVO getLoginLogDetail(String id) {
        SysLoginLog loginLog = this.getById(id);
        if (loginLog == null) {
            throw new BusinessException(ResponseCodeEnum.LOG_NOT_EXISTS);
        }

        return convertToVO(loginLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLoginLog(String id) {
        SysLoginLog loginLog = this.getById(id);
        if (loginLog == null) {
            throw new BusinessException(ResponseCodeEnum.LOG_NOT_EXISTS);
        }

        this.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(String[] ids) {
        if (ids == null || ids.length == 0) {
            throw new BusinessException(ResponseCodeEnum.BAD_REQUEST);
        }

        this.removeByIds(Arrays.asList(ids));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearLoginLogs() {
        this.remove(new LambdaQueryWrapper<>());
    }

    /**
     * 转换为VO
     */
    private LoginLogVO convertToVO(SysLoginLog loginLog) {
        LoginLogVO vo = new LoginLogVO();
        BeanUtils.copyProperties(loginLog, vo);
        return vo;
    }

}
