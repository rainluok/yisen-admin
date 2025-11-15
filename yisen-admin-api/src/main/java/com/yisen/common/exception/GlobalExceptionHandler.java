package com.yisen.common.exception;

import com.yisen.common.enums.ResponseCodeEnum;
import com.yisen.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.sql.SQLException;
import java.util.stream.Collectors;

/**
 * 全局异常处理器
 *
 * @author yisen
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    public Result<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常: URI={}, Code={}, Message={}",
                request.getRequestURI(), e.getCode(), e.getMessage());
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(SystemException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleSystemException(SystemException e, HttpServletRequest request) {
        log.error("系统异常: URI={}, Code={}, Message={}",
                request.getRequestURI(), e.getCode(), e.getMessage(), e);
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常（@Valid）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("参数校验失败: URI={}, Message={}", request.getRequestURI(), errorMessage);
        return Result.fail(ResponseCodeEnum.VALIDATION_ERROR.getCode(), errorMessage);
    }

    /**
     * 处理参数绑定异常
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleBindException(BindException e, HttpServletRequest request) {
        String errorMessage = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("参数绑定失败: URI={}, Message={}", request.getRequestURI(), errorMessage);
        return Result.fail(ResponseCodeEnum.BAD_REQUEST.getCode(), errorMessage);
    }

    /**
     * 处理约束违反异常（@Validated）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        log.warn("约束违反: URI={}, Message={}", request.getRequestURI(), errorMessage);
        return Result.fail(ResponseCodeEnum.VALIDATION_ERROR.getCode(), errorMessage);
    }

    /**
     * 处理参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String errorMessage = String.format("参数 '%s' 类型错误，期望类型: %s",
                e.getName(), e.getRequiredType().getSimpleName());

        log.warn("参数类型不匹配: URI={}, Message={}", request.getRequestURI(), errorMessage);
        return Result.fail(ResponseCodeEnum.BAD_REQUEST.getCode(), errorMessage);
    }

    /**
     * 处理404异常
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Result<?> handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.warn("请求的资源不存在: URI={}", request.getRequestURI());
        return Result.fail(ResponseCodeEnum.NOT_FOUND);
    }

    /**
     * 处理SQL异常
     */
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleSQLException(SQLException e, HttpServletRequest request) {
        log.error("数据库异常: URI={}, SQLState={}, Message={}",
                request.getRequestURI(), e.getSQLState(), e.getMessage(), e);
        return Result.fail(ResponseCodeEnum.DATABASE_ERROR);
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        log.error("空指针异常: URI={}", request.getRequestURI(), e);
        return Result.fail(ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(), "系统内部错误");
    }

    /**
     * 处理IllegalArgumentException
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        log.warn("非法参数异常: URI={}, Message={}", request.getRequestURI(), e.getMessage());
        return Result.fail(ResponseCodeEnum.BAD_REQUEST.getCode(), e.getMessage());
    }

    /**
     * 处理IllegalStateException
     */
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleIllegalStateException(IllegalStateException e, HttpServletRequest request) {
        log.error("非法状态异常: URI={}, Message={}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(ResponseCodeEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("运行时异常: URI={}, Message={}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(ResponseCodeEnum.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理其他未知异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleException(Exception e, HttpServletRequest request) {
        log.error("未知异常: URI={}, Message={}", request.getRequestURI(), e.getMessage(), e);
        return Result.fail(ResponseCodeEnum.INTERNAL_SERVER_ERROR);
    }
}

