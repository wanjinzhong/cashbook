package com.neil.cashbook.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.neil.cashbook.bo.GlobalResult;
import com.neil.cashbook.dao.entity.User;
import com.neil.cashbook.service.UserService;
import com.neil.cashbook.service.WechatServiceImpl;
import com.neil.cashbook.util.JwtUtil;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/api")
public class UserApi {

    @Autowired
    private WechatServiceImpl wechatService;

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public GlobalResult<String> login(@RequestParam(value = "code", required = false) String code) {

        // JSONObject SessionKeyOpenId = wechatService.getSessionKeyOrOpenId(code);
        // String openid = SessionKeyOpenId.getString("openid");
        String openid = "1";
        if (StringUtils.isBlank(openid)) {
            // throw new UnauthenticatedException("微信登录失败");
        }
        JwtUtil util = new JwtUtil();
        String jwtToken = util.encode(openid, 300000, new HashMap<>());
        userService.saveUser(openid);

        // throw new UnknownAccountException("h");
        return GlobalResult.of(jwtToken);
    }

    @GetMapping("me")
    public GlobalResult<User> me() {
        return GlobalResult.of(userService.getCurrentUser());
    }
}
