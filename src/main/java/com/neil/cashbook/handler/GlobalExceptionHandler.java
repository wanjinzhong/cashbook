package com.neil.cashbook.handler;

import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.neil.cashbook.bo.GlobalResult;
import com.neil.cashbook.exception.AuthException;
import lombok.extern.slf4j.Slf4j;
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
    @ExceptionHandler(AuthException.class)
    public GlobalResult<String> handle401(AuthException e) {
        log.error("ERROR: " + e.getMessage(), e);
        GlobalResult<String> jsonEntity = new GlobalResult<>();
        jsonEntity.setStatus(401);
        jsonEntity.setMsg(e.getMessage());
        return jsonEntity;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(TokenExpiredException.class)
    public GlobalResult<String> handleLoginExpiredException(TokenExpiredException e) {
        log.error("ERROR: " + e.getMessage(), e);
        GlobalResult<String> jsonEntity = new GlobalResult<>();
        jsonEntity.setStatus(499);
        jsonEntity.setMsg("登录过期");
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

