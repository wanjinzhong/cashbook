package com.neil.cashbook.service;

import com.neil.cashbook.auth.WebContext;
import com.neil.cashbook.dao.entity.User;
import com.neil.cashbook.dao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebContext webContext;

    @Override
    public boolean isOpenIdExist(String openId) {
        if (openId == null) {
            throw new RuntimeException("Open Id 未指定");
        }
        return userRepository.findByOpenId(openId) != null;
    }

    @Override
    @Transactional
    public void saveUser(String openId, String name, String avatar) {
        User user = userRepository.findByOpenId(openId);
        if (user == null) {
            user = new User();
            user.setOpenId(openId);
        }
        user.setName(name);
        user.setAvatar(avatar);
        userRepository.save(user);
    }

    @Override
    public User getCurrentUser() {
        return webContext.getUser();
    }
}
