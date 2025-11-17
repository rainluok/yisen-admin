package com.yisen.common.aspect;

import com.yisen.common.annotation.OperationLog;
import com.yisen.common.util.BrowserUtil;
import com.yisen.common.util.IpUtil;
import com.yisen.core.context.LoginUserContext;
import com.yisen.module.system.log.model.po.SysLog;
import com.yisen.module.system.log.service.SysLogService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 操作日志切面（异步）
 * 使用 AOP 实现操作日志记录，替代 OperationLogInterceptor
 *
 * @author rainluo
 * @date 2025-11-14
 */
@Slf4j
@Aspect
@Component
public class OperationLogAspect {

    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();

    @Resource
    private SysLogService sysLogService;

    /**
     * 前置通知：记录开始时间
     */
    @Before("@annotation(operationLog)")
    public void doBefore(JoinPoint joinPoint, OperationLog operationLog) {
        log.debug("开始记录操作日志: {}", operationLog.value());
        START_TIME.set(System.currentTimeMillis());
    }

    /**
     * 后置通知：正常返回时记录日志
     */
    @AfterReturning(pointcut = "@annotation(operationLog)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, OperationLog operationLog, Object result) {
        try {
            saveLogAsync(joinPoint, operationLog, null, result);
        } finally {
            START_TIME.remove();
        }
    }

    /**
     * 异常通知：异常时记录日志
     */
    @AfterThrowing(pointcut = "@annotation(operationLog)", throwing = "ex")
    public void doAfterThrowing(JoinPoint joinPoint, OperationLog operationLog, Exception ex) {
        try {
            saveLogAsync(joinPoint, operationLog, ex, null);
        } finally {
            START_TIME.remove();
        }
    }

    /**
     * 异步保存日志
     */
    @Async("asyncExecutor")
    public void saveLogAsync(JoinPoint joinPoint, OperationLog operationLog, Exception ex, Object result) {
        try {
            // 获取请求信息
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes == null) {
                log.warn("无法获取请求上下文，跳过日志记录");
                return;
            }
            HttpServletRequest request = attributes.getRequest();

            SysLog sysLog = new SysLog();

            // 基本信息
            sysLog.setUserId(LoginUserContext.getUserId());
            sysLog.setUsername(LoginUserContext.getUsername());
            sysLog.setOperation(operationLog.value());
            sysLog.setOperationType(operationLog.type().getCode());  // 获取枚举的 code 值
            sysLog.setBizType(operationLog.bizType().getCode());      // 获取枚举的 code 值
            sysLog.setBizId(operationLog.bizId());

            // 请求信息
            sysLog.setRequestUrl(request.getRequestURI());
            sysLog.setRequestMethod(request.getMethod());

            // 获取请求参数（简单处理，可以根据需要增强）
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                // 简单转换为字符串，实际项目中可以使用 JSON 序列化
                sysLog.setRequestParams(buildRequestParams(args));
            }

            // 状态信息
            sysLog.setStatus(ex == null ? 1 : 0);
            sysLog.setErrorMessage(ex != null ? ex.getMessage() : null);

            // 客户端信息
            sysLog.setIp(IpUtil.getIpAddr(request));
            sysLog.setOs(BrowserUtil.getOS(request));
            sysLog.setBrowser(BrowserUtil.getBrowserInfo(request));

            // 耗时
            Long startTime = START_TIME.get();
            if (startTime != null) {
                sysLog.setSpendTime((int) (System.currentTimeMillis() - startTime));
            }

            // 保存日志
            sysLogService.save(sysLog);

            log.debug("操作日志保存成功: {}", operationLog.value());

        } catch (Exception e) {
            log.error("保存操作日志失败", e);
        }
    }

    /**
     * 构建请求参数字符串（简化版本）
     */
    private String buildRequestParams(Object[] args) {
        if (args == null || args.length == 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                continue;
            }
            // 跳过 HttpServletRequest 和 HttpServletResponse 等
            String argClassName = arg.getClass().getName();
            if (argClassName.contains("HttpServlet") || argClassName.contains("MultipartFile")) {
                continue;
            }

            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(arg.toString());

            // 限制长度
            if (sb.length() > 500) {
                sb.append("...");
                break;
            }
        }

        return sb.length() > 0 ? sb.toString() : null;
    }
}

