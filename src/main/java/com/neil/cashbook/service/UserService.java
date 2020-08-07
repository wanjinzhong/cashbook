package com.neil.cashbook.service;
import com.neil.cashbook.dao.entity.User;

public interface UserService {
    boolean isOpenIdExist(String openId);

    void saveUser(String openId);

    void setCurrentUser(String openId);

    User getCurrentUser();
}
