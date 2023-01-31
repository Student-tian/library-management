package com.henu.mapper;

import com.henu.controller.request.BaseRequest;
import com.henu.entity.Book;
import com.henu.entity.Borrow;
import com.henu.entity.Retur;
import com.henu.mapper.po.BorrowReturCountPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@Mapper
public interface BorrowMapper {
    List<Borrow> list();
    void save(Borrow user);
    void saveReturn(Retur user);
    List<Borrow> listByCondition(BaseRequest baseRequest);



    List<Retur> listReturnByCondition(BaseRequest baseRequest);

    Borrow getById(Integer id);

    void update(Borrow category);

    void deleteById(Integer id);

    void deleteReturnById(Integer id);

    void updateStatus(String status, Integer id);

    List<BorrowReturCountPO> getCountByTimeRange(@Param("timeRange") String timeRange, @Param("type") int type);

//    Book getByBookNo(String bookNo);

//    void updateStatus(String 已归还, String bookNo, String userNo);
}
