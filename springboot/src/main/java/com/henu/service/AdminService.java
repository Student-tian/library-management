package com.henu.service;

import com.github.pagehelper.PageInfo;
import com.henu.controller.request.BaseRequest;
import com.henu.controller.request.LoginRequest;
import com.henu.controller.request.PasswordRequest;
import com.henu.dto.LoginDTO;
import com.henu.entity.Admin;
import com.henu.entity.User;

import java.util.List;

public interface AdminService {
    List<Admin> list();
    PageInfo<Admin>page(BaseRequest baseRequest);
    Admin getById(Integer id);
    void save(Admin admin);
    void update(Admin admin);
    void deleteById(Integer id);

    LoginDTO login(LoginRequest loginRequest);

    void changePass(PasswordRequest passwordRequest);
}
