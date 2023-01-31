package com.henu.mapper;

import com.henu.controller.request.BaseRequest;
import com.henu.controller.request.UserPageRequest;
import com.henu.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
@Mapper
public interface UserMapper{
//    @Select("select *from user")
    List<User>list();
    void save(User user);
    List<User> listByCondition(BaseRequest baseRequest);

    User getById(Integer id);

    void update(User user);

    void deleteById(Integer id);

    User getByUsername(String userNo);

//    void updateByUserNo(@Param("score")int score,@Param("status")int status,@Param("userNo") String userNo);
}
