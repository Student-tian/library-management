package com.henu.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.henu.controller.request.BaseRequest;
import com.henu.controller.request.LoginRequest;
import com.henu.controller.request.PasswordRequest;
import com.henu.dto.LoginDTO;
import com.henu.entity.Admin;
import com.henu.exception.ServiceException;
import com.henu.mapper.AdminMapper;
import com.henu.service.AdminService;
import com.henu.utils.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
    private static final String DEFAULT_PASS="123";
    private static final String PASS_SALT="qinge";
   @Autowired
   private AdminMapper adminMapper;
    public List<Admin> list() {
        return adminMapper.list();
    }

    public PageInfo<Admin> page(BaseRequest userPageRequest){
        PageHelper.startPage(userPageRequest.getPageNum(), userPageRequest.getPageSize());
        List<Admin> admins = adminMapper.listByCondition(userPageRequest);
        return new PageInfo<Admin>(admins);
    }

    @Override
    public void save(Admin admin) {
        if(StrUtil.isBlank(admin.getPassword())){
            admin.setPassword(DEFAULT_PASS);
        }
        admin.setPassword(SecureUtil.md5(admin.getPassword()+PASS_SALT));//md5加密加盐
        try{
//            Date date = new Date();
//            admin.setUsername(DateUtil.format(date,"yyyyMMdd")+Math.abs(IdUtil.fastSimpleUUID().hashCode()));
            adminMapper.save(admin);
        }catch (DuplicateKeyException e){
                log.error("数据插入失败，username:{}",admin.getUsername());
                throw new ServiceException("用户名重复");
        }
//        Date date = new Date();
//        admin.setUsername(DateUtil.format(date,"yyyyMMdd")+Math.abs(IdUtil.fastSimpleUUID().hashCode()));
//        adminMapper.save(admin);
    }

    @Override
    public Admin getById(Integer id) {
       return adminMapper.getById(id);
    }

    @Override
    public void update(Admin admin) {
        adminMapper.update(admin);
    }

    @Override
    public void deleteById(Integer id) {
        adminMapper.deleteById(id);
    }

    @Override
    public LoginDTO login(LoginRequest loginRequest) {
//        loginRequest.setPassword(SecureUtil.md5(loginRequest.getPassword()+PASS_SALT));
        LoginDTO loginDTO = null;
        Admin admin=null;
        try {
            admin= adminMapper.getByUsername(loginRequest.getUsername());
            throw  new ServiceException("用户名错误");
        }catch (Exception e){
            log.error("根据用户名{}查询出错",loginRequest.getUsername());
        }
//        Admin admin = adminMapper.getByUsernameAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
//        System.out.println(admin.getUsername());
        loginDTO = new LoginDTO();
       if(admin==null){
           throw new ServiceException("用户名或密码错误", "无token，请重新登录");
       }
        String securePass= SecureUtil.md5(loginRequest.getPassword()+PASS_SALT);
       if(!securePass.equals(admin.getPassword())){
           throw new ServiceException("用户名或密码错误");
       }
       if(!admin.isStatus()){
           throw  new ServiceException("当前用户处于禁用状态，请联系管理员");
       }
        String token=TokenUtils.genToken(String.valueOf(admin.getId()),admin.getPassword());
       loginDTO.setToken(token);
        BeanUtils.copyProperties(admin,loginDTO);
        return loginDTO;
    }

    @Override
    public void changePass(PasswordRequest passwordRequest) {
      passwordRequest.setNewPass(SecureUtil.md5(passwordRequest.getNewPass()+PASS_SALT));
      int count=adminMapper.updatePassword(passwordRequest);

      if(count<=0){
          throw new ServiceException("修改密码失败");
      }
    }
}
