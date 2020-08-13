package com.neil.cashbook.auth;

import java.lang.reflect.Modifier;

import javax.servlet.http.HttpServletRequest;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.neil.cashbook.exception.AuthException;
import com.neil.cashbook.exception.BizException;
import com.neil.cashbook.util.JwtUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class AuthAspect {

    @Autowired
    private WebContext webContext;

    @Pointcut("@within(AuthRequired) || @annotation(AuthRequired)")
    public void authAspect() {

    }

    @Before("authAspect()")
    public void beforeRequest(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        if (StringUtils.isEmpty(token)) {
            throw new BizException("未登录");
        }
        JwtUtil jwtUtil = new JwtUtil();
        try {
            jwtUtil.isVerify(token);
            String openId = jwtUtil.decode(token).getSubject();
            webContext.setUser(openId);
        } catch (TokenExpiredException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthException("登录信息有误", e);
        }
    }
}
