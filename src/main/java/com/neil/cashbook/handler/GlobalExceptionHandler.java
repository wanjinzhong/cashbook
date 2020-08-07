package com.neil.cashbook.handler;

import javax.servlet.http.HttpServletRequest;

import com.neil.cashbook.bo.GlobalResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 捕捉shiro的异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public GlobalResult<String> handle401(ShiroException e) {
        log.error("ERROR: " + e.getMessage(), e);
        GlobalResult<String> jsonEntity = new GlobalResult<>();
        jsonEntity.setStatus(401);
        jsonEntity.setMsg(e.getMessage());
        return jsonEntity;
    }

    /**
     * 捕捉UnauthorizedException
     * @return
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedException.class)
    public GlobalResult<String> handle403() {
        GlobalResult<String> jsonEntity = new GlobalResult<>();
        jsonEntity.setStatus(403);
        jsonEntity.setMsg("未授权");
        return jsonEntity;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnknownAccountException.class)
    public GlobalResult<String> handleUnknownAccountException() {
        GlobalResult<String> jsonEntity = new GlobalResult<>();
        jsonEntity.setStatus(403);
        jsonEntity.setMsg("账号异常");
        return jsonEntity;
    }

    /**
     * 捕捉AccountException
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(AccountException.class)
    public GlobalResult<String> handleAccountException(AccountException e) {
        GlobalResult<String> jsonEntity = new GlobalResult<>();
        jsonEntity.setStatus(401);
        jsonEntity.setMsg(e.getMessage());
        return jsonEntity;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException.class)
    public GlobalResult<String> handleUnauthenticatedException() {
        GlobalResult<String> jsonEntity = new GlobalResult<>();
        jsonEntity.setStatus(401);
        jsonEntity.setMsg("未登陆");
        return jsonEntity;
    }

    /**
     * 捕捉AccountException
     * @param request
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GlobalResult<String> globalException(HttpServletRequest request, Throwable e) {
        log.error("ERROR: " + e.getMessage(), e);
        // report(e);
        GlobalResult<String> jsonEntity = new GlobalResult<>();
        jsonEntity.setStatus(getStatus(request).value());
        jsonEntity.setMsg(e.getMessage());
        return jsonEntity;
    }

    private HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }
}

