package com.henu.controller;

import com.henu.commom.Result;
import com.henu.controller.request.AdminPageRequest;
import com.henu.controller.request.LoginRequest;
import com.henu.controller.request.PasswordRequest;
import com.henu.controller.request.UserPageRequest;
import com.henu.dto.LoginDTO;
import com.henu.entity.Admin;
import com.henu.entity.User;
import com.henu.service.AdminService;
import com.henu.service.UserService;
import com.henu.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/list")
    public Result list(){
        List<Admin> admins = adminService.list();
        return Result.success(admins);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        Admin admin = adminService.getById(id);
        return Result.success(admin);
    }

    @GetMapping("/page")
    public Result page(AdminPageRequest adminPageRequest){
        return Result.success(adminService.page(adminPageRequest));
    }
    @PostMapping("/save")
    public Result save(@RequestBody Admin admin){
        adminService.save(admin);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody  Admin admin){
        adminService.update(admin);
        admin.setUpdatetime(new Date());
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        adminService.deleteById(id);
        return Result.success();
    }
    @PostMapping("/login")
    public Result login(@RequestBody LoginRequest loginRequest){
        LoginDTO login = adminService.login(loginRequest);
//        TokenUtils.genToken(String.valueOf(admin.get))
        return Result.success( login);
    }
    @PutMapping("/password")
    public Result password(@RequestBody PasswordRequest passwordRequest){
        adminService.changePass(passwordRequest);
        return Result.success();
    }

}
