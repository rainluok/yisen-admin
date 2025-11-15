package com.yisen.module.system.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.exception.BusinessException;
import com.yisen.common.model.PageRequest;
import com.yisen.common.model.PageResult;
import com.yisen.module.system.log.mapper.SysLogMapper;
import com.yisen.module.system.log.model.dto.LogQueryDTO;
import com.yisen.module.system.log.model.po.SysLog;
import com.yisen.module.system.log.model.vo.LogVO;
import com.yisen.module.system.log.service.SysLogService;
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
 * @description 针对表【sys_log(操作日志)】的数据库操作Service实现
 * @createDate 2025-11-14 13:36:46
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog>
        implements SysLogService {

    @Override
    public PageResult<LogVO> pageLogs(PageRequest<LogQueryDTO> pageRequest) {
        // 构建分页对象
        Page<SysLog> page = new Page<>(pageRequest.getPageNum(), pageRequest.getPageSize());

        // 构建查询条件
        LogQueryDTO queryDTO = pageRequest.getParams();
        LambdaQueryWrapper<SysLog> queryWrapper = new LambdaQueryWrapper<>();

        if (queryDTO != null) {
            if (StringUtils.isNotBlank(queryDTO.getUsername())) {
                queryWrapper.like(SysLog::getUsername, queryDTO.getUsername());
            }
            if (queryDTO.getOperationType() != null) {
                queryWrapper.eq(SysLog::getOperationType, queryDTO.getOperationType());
            }
            if (StringUtils.isNotBlank(queryDTO.getBizType())) {
                queryWrapper.eq(SysLog::getBizType, queryDTO.getBizType());
            }
            if (queryDTO.getStatus() != null) {
                queryWrapper.eq(SysLog::getStatus, queryDTO.getStatus());
            }
            if (StringUtils.isNotBlank(queryDTO.getStartTime())) {
                queryWrapper.ge(SysLog::getCreateTime, queryDTO.getStartTime());
            }
            if (StringUtils.isNotBlank(queryDTO.getEndTime())) {
                queryWrapper.le(SysLog::getCreateTime, queryDTO.getEndTime());
            }
        }

        queryWrapper.orderByDesc(SysLog::getCreateTime);

        // 查询分页数据
        IPage<SysLog> pageResult = this.page(page, queryWrapper);

        // 转换为VO
        List<LogVO> records = pageResult.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return PageResult.of(pageResult, records);
    }

    @Override
    public LogVO getLogDetail(String id) {
        SysLog log = this.getById(id);
        if (log == null) {
            throw new BusinessException(ResponseCodeEnum.LOG_NOT_EXISTS);
        }

        return convertToVO(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteLog(String id) {
        SysLog log = this.getById(id);
        if (log == null) {
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
    public void clearLogs() {
        this.remove(new LambdaQueryWrapper<>());
    }

    /**
     * 转换为VO
     */
    private LogVO convertToVO(SysLog log) {
        LogVO vo = new LogVO();
        BeanUtils.copyProperties(log, vo);
        return vo;
    }

}
