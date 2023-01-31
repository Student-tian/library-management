package com.henu.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.henu.controller.request.BaseRequest;
import com.henu.entity.Book;
import com.henu.entity.Category;
import com.henu.exception.ServiceException;
import com.henu.mapper.BookMapper;
import com.henu.mapper.CategoryMapper;
import com.henu.service.BookService;
import com.henu.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class BookServiceImpl implements BookService {
   @Autowired
   private BookMapper bookMapper;

   @Override
    public List<Book> list() {
        return bookMapper.list();
    }
    @Override
    public PageInfo<Book> page(BaseRequest baseRequest){
        PageHelper.startPage(baseRequest.getPageNum(), baseRequest.getPageSize());
        List<Book> books = bookMapper.listByCondition(baseRequest);
        return new PageInfo<>(books);
    }

    @Override
    public void save(Book book) {
       try{
           List<String> categories = book.getCategories();
           StringBuilder stringBuilder = new StringBuilder();
           if(CollUtil.isNotEmpty(categories)){
               categories.forEach(v -> stringBuilder.append(v).append(">"));
               book.setCategory(stringBuilder.toString().substring(0,stringBuilder.lastIndexOf(">")));
           }
           bookMapper.save(book);
       }catch (Exception e) {
           log.error("数据插入错误",e);
           throw new ServiceException("数据插入错误");
       }
    }

    @Override
    public Book getById(Integer id) {
        return bookMapper.getById(id);
    }

    @Override
    public void update(Book book) {
       book.setCategory(category(book.getCategories()));
       book.setUpdatetime(LocalDate.now());
        bookMapper.update(book);
    }

    @Override
    public void deleteById(Integer id) {
        bookMapper.deleteById(id);
    }

    private String category(List<String>categories){
        StringBuilder stringBuilder = new StringBuilder();
        if(CollUtil.isNotEmpty(categories)){
            categories.forEach(v -> stringBuilder.append(v).append(">"));
          return stringBuilder.substring(0,stringBuilder.lastIndexOf(">"));
        }
        return stringBuilder.toString();
    }

}
