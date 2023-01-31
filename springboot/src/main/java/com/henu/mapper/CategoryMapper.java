package com.henu.mapper;

import com.henu.controller.request.BaseRequest;
import com.henu.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CategoryMapper {
    List<Category>list();
    void save(Category user);
    List<Category> listByCondition(BaseRequest baseRequest);

    Category getById(Integer id);

    void update(Category category);

    void deleteById(Integer id);
}
