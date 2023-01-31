package com.henu.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.henu.controller.request.BaseRequest;
import com.henu.controller.request.UserPageRequest;
import com.henu.entity.User;
import com.henu.mapper.UserMapper;
import com.henu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class UserServiceImpl implements UserService {
   @Autowired
   private UserMapper userMapper;
    public List<User> list() {
        return userMapper.list();
    }

    public PageInfo<User> page(BaseRequest userPageRequest){
        PageHelper.startPage(userPageRequest.getPageNum(), userPageRequest.getPageSize());
        List<User> users = userMapper.listByCondition(userPageRequest);
        return new PageInfo<>(users);
    }

    @Override
    public void save(User user) {
        Date date = new Date();
        user.setUsername(DateUtil.format(date,"yyyyMMdd")+Math.abs(IdUtil.fastSimpleUUID().hashCode()));
        userMapper.save(user);
    }

    @Override
    public User getById(Integer id) {
       return userMapper.getById(id);
    }

    @Override
    public void update(User user) {
        userMapper.update(user);
    }

    @Override
    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    @Override
    public void handleAccount(User user) {
        Integer score = user.getScore();
        if(score==null){
            return;
        }
        Integer id = user.getId();
        User dbUser = userMapper.getById(id);
        dbUser.setAccount(dbUser.getAccount()+score);
        userMapper.update(dbUser);

    }
}
