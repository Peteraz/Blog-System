package com.example.blogsystem.aop;

import com.example.blogsystem.common.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.net.BindException;

/**
 * 全局异常处理
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return JsonUtils.jsonPrint(-1, e.getMessage(), null);
    }

    @ExceptionHandler(ServiceException.class)
    public String handleServiceException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生业务异常{}.", requestURI, e.getMessage());
        return JsonUtils.jsonPrint(-1, e.getMessage(), null);
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return JsonUtils.jsonPrint(-1, e.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return JsonUtils.jsonPrint(-1, e.getMessage(), null);
    }

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生自定义验证异常.", requestURI, e);
        return JsonUtils.jsonPrint(-1, e.getMessage(), null);
    }

    @ExceptionHandler(MailAuthenticationException.class)
    public String handleMailAuthenticationException(MailAuthenticationException e) {
        log.error("发送邮件发生异常: ", e);
        return JsonUtils.jsonPrint(-1, e.getMessage(), null);
    }
}
