package com.neil.cashbook.controller;

import java.util.HashMap;

import com.alibaba.fastjson.JSONObject;
import com.neil.cashbook.auth.AuthRequired;
import com.neil.cashbook.bo.GlobalResult;
import com.neil.cashbook.bo.LoginBo;
import com.neil.cashbook.dao.entity.User;
import com.neil.cashbook.enums.CommonStatus;
import com.neil.cashbook.exception.AuthException;
import com.neil.cashbook.exception.BizException;
import com.neil.cashbook.service.UserService;
import com.neil.cashbook.service.WechatServiceImpl;
import com.neil.cashbook.util.JwtUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public GlobalResult<String> login(@RequestBody LoginBo loginBo) {
        // JSONObject SessionKeyOpenId = wechatService.getSessionKeyOrOpenId(loginBo.getCode());
        // String openid = SessionKeyOpenId.getString("openid");
        // if (StringUtils.isBlank(openid)) {
        //     throw new AuthException("微信登录失败");
        // }
        String openid = "abc123";
        JwtUtil util = new JwtUtil();
        String jwtToken = util.encode(openid, 1000 * 60 * 60, new HashMap<>());
        userService.saveUser(openid, loginBo.getName(), loginBo.getAvatar());
        return GlobalResult.of(jwtToken);
    }

    @GetMapping("me")
    @AuthRequired
    public GlobalResult<User> me() {
        return GlobalResult.of(userService.getCurrentUser());
    }

    @PutMapping("user/{openId}")
    @AuthRequired
    public GlobalResult<CommonStatus> updateUserEntry(@PathVariable("openId") String openId, @RequestParam("entry") boolean entry) {
        userService.updateUserAllowEntry(openId, entry);
        return GlobalResult.of(CommonStatus.SUCCESS);
    }
}
