package com.handturn.bole.common.handler;

import com.handturn.bole.common.entity.FebsResponse;
import com.handturn.bole.common.exception.FebsException;
import com.handturn.bole.common.exception.FileDownloadException;
import com.handturn.bole.common.exception.LimitAccessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.ExpiredSessionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.Set;

/**
 * @author Eric
 */
@Slf4j
@RestControllerAdvice
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public FebsResponse handleException(Exception e) {
        log.error("系统内部异常，异常信息", e);
        return new FebsResponse().code(HttpStatus.INTERNAL_SERVER_ERROR).message("系统内部异常");
    }

    @ExceptionHandler(value = FebsException.class)
    public FebsResponse handleFebsException(FebsException e) {
        log.error("系统错误", e);
        return new FebsResponse().code(HttpStatus.INTERNAL_SERVER_ERROR).message(e.getMessage());
    }

    /**
     * 统一处理请求参数校验(实体对象传参)
     *
     * @param e BindException
     * @return FebsResponse
     */
    @ExceptionHandler(BindException.class)
    public FebsResponse validExceptionHandler(BindException e) {
        StringBuilder message = new StringBuilder();
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        for (FieldError error : fieldErrors) {
            message.append(error.getField()).append(error.getDefaultMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return new FebsResponse().code(HttpStatus.BAD_REQUEST).message(message.toString());
    }

    /**
     * 统一处理请求参数校验(普通传参)
     *
     * @param e ConstraintViolationException
     * @return FebsResponse
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public FebsResponse handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder message = new StringBuilder();
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> violation : violations) {
            Path path = violation.getPropertyPath();
            String[] pathArr = StringUtils.splitByWholeSeparatorPreserveAllTokens(path.toString(), ".");
            message.append(pathArr[1]).append(violation.getMessage()).append(",");
        }
        message = new StringBuilder(message.substring(0, message.length() - 1));
        return new FebsResponse().code(HttpStatus.BAD_REQUEST).message(message.toString());
    }

    @ExceptionHandler(value = LimitAccessException.class)
    public FebsResponse handleLimitAccessException(LimitAccessException e) {
        log.debug("LimitAccessException", e);
        return new FebsResponse().code(HttpStatus.TOO_MANY_REQUESTS).message(e.getMessage());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public FebsResponse handleUnauthorizedException(UnauthorizedException e) {
        log.debug("UnauthorizedException", e);
        return new FebsResponse().code(HttpStatus.FORBIDDEN).message(e.getMessage());
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public FebsResponse handleAuthenticationException(AuthenticationException e) {
        log.debug("AuthenticationException", e);
        return new FebsResponse().code(HttpStatus.INTERNAL_SERVER_ERROR).message(e.getMessage());
    }

    @ExceptionHandler(value = AuthorizationException.class)
    public FebsResponse handleAuthorizationException(AuthorizationException e){
        log.debug("AuthorizationException", e);
        return new FebsResponse().code(HttpStatus.UNAUTHORIZED).message(e.getMessage());
    }


    @ExceptionHandler(value = ExpiredSessionException.class)
    public FebsResponse handleExpiredSessionException(ExpiredSessionException e) {
        log.debug("ExpiredSessionException", e);
        return new FebsResponse().code(HttpStatus.UNAUTHORIZED).message(e.getMessage());
    }

    @ExceptionHandler(value = FileDownloadException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleFileDownloadException(FileDownloadException e) {
        log.error("FileDownloadException", e);
    }
}
