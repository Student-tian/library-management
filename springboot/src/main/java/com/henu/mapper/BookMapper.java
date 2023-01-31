package com.henu.mapper;

import com.henu.controller.request.BaseRequest;
import com.henu.entity.Book;
import com.henu.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BookMapper {
    List<Book>list();
    void save(Book user);
    List<Book> listByCondition(BaseRequest baseRequest);

    Book getById(Integer id);

    void update(Book category);

    void deleteById(Integer id);

    Book getByNo(String bookNo);

    void updateNumber(String bookNo);
}
