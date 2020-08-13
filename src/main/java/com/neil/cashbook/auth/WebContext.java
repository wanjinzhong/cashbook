package com.neil.cashbook.auth;

import com.neil.cashbook.dao.entity.User;
import com.neil.cashbook.dao.repository.UserRepository;
import com.neil.cashbook.exception.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebContext {

    private static final ThreadLocal<User> localUser = new ThreadLocal<>();

    @Autowired
    private UserRepository userRepository;

    public void setUser(String openId) {
        User user = userRepository.findByOpenId(openId);
        if (user == null) {
            throw new AuthException("用户不存在");
        }
        localUser.set(user);
    }

    public void setUser(User user) {
        localUser.set(user);
    }

    public User getUser() {
        if (localUser.get() == null) {
            throw new AuthException("未登录");
        }
        return localUser.get();
    }
}
