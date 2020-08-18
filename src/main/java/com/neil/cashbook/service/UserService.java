package com.neil.cashbook.service;
import java.util.List;

import com.neil.cashbook.bo.UserBo;
import com.neil.cashbook.dao.entity.User;

public interface UserService {

    void saveUser(String openId, String name, String avatar);

    User getCurrentUser();

    void updateUserAllowEntry(String openId, boolean entry);

    List<UserBo> getUsers();

    void deleteUser(String openId);
}
