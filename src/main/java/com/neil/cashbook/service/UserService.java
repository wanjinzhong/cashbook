package com.neil.cashbook.service;
import java.util.List;

import com.neil.cashbook.bo.UserBo;
import com.neil.cashbook.dao.entity.User;

public interface UserService {
    boolean isOpenIdExist(String openId);

    void saveUser(String openId, String name, String avatar);

    User getCurrentUser();

    void updateUserAllowEntry(String openId, boolean entry);

    List<UserBo> getUsers();
}
