package com.henu.controller;

import com.henu.commom.Result;
import com.henu.controller.request.UserPageRequest;
import com.henu.entity.User;
import com.henu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public Result list(){
        List<User> users = userService.list();
        return Result.success(users);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        User users = userService.getById(id);
        return Result.success(users);
    }

    @GetMapping("/page")
    public Result page(UserPageRequest userPageRequest){
        return Result.success(userService.page(userPageRequest));
    }
    @PostMapping("/save")
    public Result save(@RequestBody User user){
        userService.save(user);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody  User user){
        userService.update(user);
        user.setUpdatetime(new Date());
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        userService.deleteById(id);
        return Result.success();
    }
    @PostMapping("/account")
    public Result account(@RequestBody User user){
        userService.handleAccount(user);
        return Result.success();
    }

}
