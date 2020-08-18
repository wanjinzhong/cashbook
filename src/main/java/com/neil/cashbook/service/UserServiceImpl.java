package com.neil.cashbook.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.neil.cashbook.auth.WebContext;
import com.neil.cashbook.bo.UserBo;
import com.neil.cashbook.dao.entity.User;
import com.neil.cashbook.dao.repository.UserRepository;
import com.neil.cashbook.exception.BizException;
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
        webContext.setUser(user);
    }

    @Override
    public User getCurrentUser() {
        return webContext.getUser();
    }

    @Override
    public void updateUserAllowEntry(String openId, boolean entry) {
        User user = userRepository.findByOpenId(openId);
        if (user == null) {
            throw new BizException("用户不存在");
        }
        if (user.getId().equals(webContext.getUser().getId())) {
            throw new BizException("不能修改自己的权限");
        }
        user.setAllowEntry(entry);
        userRepository.save(user);
    }

    @Override
    public List<UserBo> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::toUserBo).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String openId) {
        User user = userRepository.findByOpenId(openId);
        if (user != null) {
            if (user.getId().equals(webContext.getUser().getId())) {
                throw new BizException("不能修改自己的权限");
            }
            userRepository.delete(user);
        }
    }

    private UserBo toUserBo(User user) {
        UserBo userBo = new UserBo();
        userBo.setOpenId(user.getOpenId());
        userBo.setName(user.getName());
        userBo.setAvatar(user.getAvatar());
        userBo.setAllowEntry(user.isAllowEntry());
        return userBo;
    }
}
