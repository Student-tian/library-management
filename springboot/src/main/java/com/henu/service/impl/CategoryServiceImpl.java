package com.henu.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.henu.controller.request.BaseRequest;
import com.henu.entity.Admin;
import com.henu.entity.Category;
import com.henu.exception.ServiceException;
import com.henu.mapper.CategoryMapper;
import com.henu.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {
   @Autowired
   private CategoryMapper categoryMapper;

   @Override
    public List<Category> list() {
        return categoryMapper.list();
    }
    @Override
    public PageInfo<Category> page(BaseRequest baseRequest){
        PageHelper.startPage(baseRequest.getPageNum(), baseRequest.getPageSize());
        List<Category> categories = categoryMapper.listByCondition(baseRequest);
        return new PageInfo<>(categories);
    }

    @Override
    public void save(Category category) {
       categoryMapper.save(category);
    }

    @Override
    public Category getById(Integer id) {
        return categoryMapper.getById(id);
    }

    @Override
    public void update(Category category) {
       category.setUpdatetime(LocalDate.now());
        categoryMapper.update(category);
    }

    @Override
    public void deleteById(Integer id) {
        categoryMapper.deleteById(id);
    }

}
