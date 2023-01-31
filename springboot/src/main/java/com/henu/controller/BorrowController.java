package com.henu.controller;

import com.henu.commom.Result;
import com.henu.controller.request.BookPageRequest;
import com.henu.controller.request.BorrowPageRequest;
import com.henu.entity.Borrow;
import com.henu.entity.Retur;
import com.henu.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/borrow")
public class BorrowController {
    @Autowired
    private BorrowService borrowService;

    @GetMapping("/list")
    public Result list(){
        List<Borrow> borrows = borrowService.list();
        return Result.success(borrows);
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id){
        Borrow borrow = borrowService.getById(id);
        return Result.success(borrow);
    }

    @GetMapping("/page")
    public Result page(BorrowPageRequest borrowPageRequest){
        return Result.success(borrowService.page(borrowPageRequest));
    }
    @PostMapping("/save")
    public Result save(@RequestBody Borrow borrow){
        borrowService.save(borrow);
        return Result.success();
    }

    @PostMapping("/saveRetur")
    public Result saveRetur(@RequestBody Retur borrow){
        borrowService.saveRetur(borrow);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Borrow borrow){
        borrowService.update(borrow);
        return Result.success();
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id){
        borrowService.deleteById(id);
        return Result.success();
    }
    @DeleteMapping("/deleteRetur/{id}")
    public Result deleteRetur(@PathVariable Integer id){
        borrowService.deleteReutrnById(id);
        return Result.success();
    }
    @GetMapping("/pageRetur")
    public Result pageRetur(BorrowPageRequest borrowPageRequest){
        return Result.success(borrowService.pageRetur(borrowPageRequest));
    }

    @GetMapping("/lineCharts/{timeRange}")
    public Result lineCharts(@PathVariable String timeRange){
        return Result.success(borrowService.getCountByTimeRange(timeRange));
    }
}
