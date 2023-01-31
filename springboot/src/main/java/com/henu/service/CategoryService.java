package com.henu.service;

import com.github.pagehelper.PageInfo;
import com.henu.controller.request.BaseRequest;
import com.henu.entity.Admin;
import com.henu.entity.Category;

import java.util.List;

public interface CategoryService {
    List<Category> list();
    PageInfo<Category> page(BaseRequest baseRequest);
    Category getById(Integer id);
    void save(Category obj);
    void update(Category obj);
    void deleteById(Integer id);
}
