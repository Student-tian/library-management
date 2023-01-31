package com.henu.mapper;

import com.henu.controller.request.BaseRequest;
import com.henu.controller.request.LoginRequest;
import com.henu.controller.request.PasswordRequest;
import com.henu.entity.Admin;
import com.henu.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface AdminMapper {
//    @Select("select *from user")
    List<Admin>list();
    void save(Admin user);
    List<Admin> listByCondition(BaseRequest baseRequest);

    Admin getById(Integer id);

    void update(Admin admin);

    void deleteById(Integer id);
    @Select("select * from admin where username= #{username} and password= #{password}")
    Admin getByUsernameAndPassword(@Param("username") String username,@Param("password")String password);

    int updatePassword(PasswordRequest passwordRequest);

    Admin getByUsername(String username);
}
