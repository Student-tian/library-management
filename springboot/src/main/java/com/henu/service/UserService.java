package com.henu.service;

import com.github.pagehelper.PageInfo;
import com.henu.controller.request.BaseRequest;
import com.henu.controller.request.UserPageRequest;
import com.henu.entity.User;

import java.util.List;

public interface UserService {
    List<User> list();
    PageInfo<User> page(BaseRequest baseRequest);
    User getById(Integer id);
    void save(User user);

    void update(User user);

    void deleteById(Integer id);

    void handleAccount(User user);
}
