package com.neil.cashbook.service;

import javax.security.auth.Subject;

import com.neil.cashbook.dao.entity.User;
import com.neil.cashbook.dao.repository.UserRepository;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    private ThreadLocal<String> user = new ThreadLocal<>();

    @Override
    public boolean isOpenIdExist(String openId) {
        if (openId == null) {
            throw new RuntimeException("Open Id 未指定");
        }
        return userRepository.findByOpenId(openId) != null;
    }

    @Override
    @Transactional
    public void saveUser(String openId) {
        User user = userRepository.findByOpenId(openId);
        if (user == null) {
            user = new User();
            user.setOpenId(openId);
        }
        userRepository.save(user);
    }

    @Override
    public void setCurrentUser(String openId) {
        user.set(openId);
    }

    @Override
    public User getCurrentUser() {
        String openId = user.get();
        if (StringUtils.isBlank(openId)) {
            throw new AuthenticationException("未登录");
        }
        User user = userRepository.findByOpenId(openId);
        if (user == null) {
            throw new AuthenticationException("账号不存在");
        }
        return user;
    }
}
