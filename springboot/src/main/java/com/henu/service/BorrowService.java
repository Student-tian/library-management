package com.henu.service;

import com.github.pagehelper.PageInfo;
import com.henu.controller.request.BaseRequest;
import com.henu.entity.Book;
import com.henu.entity.Borrow;
import com.henu.entity.Retur;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface BorrowService {
    List<Borrow> list();
    PageInfo<Borrow> page(BaseRequest baseRequest);
    PageInfo<Retur> pageRetur(BaseRequest baseRequest);
    Borrow getById(Integer id);
    void saveRetur(Retur obj);
    void save(Borrow obj);
    void update(Borrow obj);
    void deleteById(Integer id);
    void deleteReutrnById(Integer id);

    Map<String, Object> getCountByTimeRange(String timeRange);
}
