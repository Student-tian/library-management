package com.henu.controller;

import cn.hutool.core.collection.CollUtil;
import com.henu.commom.Result;
import com.henu.controller.request.AdminPageRequest;
import com.henu.controller.request.CategoryPageRequest;
import com.henu.controller.request.LoginRequest;
import com.henu.controller.request.PasswordRequest;
import com.henu.dto.LoginDTO;
import com.henu.entity.Admin;
import com.henu.entity.Category;
import com.henu.service.AdminService;
import com.henu.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result list(){
        List<Category> categories = categoryService.list();
        return Result.success(categories);
    }

    @GetMapping("/tree")
    public Result tree(){
        List<Category> categories = categoryService.list();
//        List<Category> collect = categories.stream().filter(v -> v.getPid() == null).collect(Collectors.toList());

        return Result.success(createTree(null,categories));
    }

    private List<Category> createTree(Integer pid,List<Category>categories){
        List<Category> treeList = new ArrayList<>();
        for(Category category:categories) {
            if(pid==null ){
                if(category.getPid()==null){
                    treeList.add(category);
                    category.setChildren(createTree(category.getId(), categories));
                }
            }else{
                if (pid.equals(category.getPid())) {
                    treeList.add(category);
                    category.setChildren(createTree(category.getId(), categories));
                }
            }
            if(CollUtil.isEmpty(category.getChildren())){
                category.setChildren(null);
            }
        }
        return treeList;
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        Category category = categoryService.getById(id);
        return Result.success(category);
    }

    @GetMapping("/page")
    public Result page(CategoryPageRequest categoryPageRequest){
        return Result.success(categoryService.page(categoryPageRequest));
    }
    @PostMapping("/save")
    public Result save(@RequestBody Category category){
        categoryService.save(category);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody  Category category){
        categoryService.update(category);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        categoryService.deleteById(id);
        return Result.success();
    }

}
