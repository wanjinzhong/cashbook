package com.neil.cashbook.service;
import com.neil.cashbook.dao.entity.User;

public interface UserService {
    boolean isOpenIdExist(String openId);

    void saveUser(String openId, String name, String avatar);

    void setCurrentUser(String openId);

    User getCurrentUser();
}
