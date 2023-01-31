package com.henu.service;

import com.github.pagehelper.PageInfo;
import com.henu.controller.request.BaseRequest;
import com.henu.entity.Book;
import com.henu.entity.Category;

import java.util.List;

public interface BookService {
    List<Book> list();
    PageInfo<Book> page(BaseRequest baseRequest);
    Book getById(Integer id);
    void save(Book obj);
    void update(Book obj);
    void deleteById(Integer id);
}
