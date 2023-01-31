package com.henu.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.henu.controller.request.BaseRequest;
import com.henu.entity.Book;
import com.henu.entity.Borrow;
import com.henu.entity.Retur;
import com.henu.entity.User;
import com.henu.exception.ServiceException;
import com.henu.mapper.BookMapper;
import com.henu.mapper.BorrowMapper;
import com.henu.mapper.UserMapper;
import com.henu.mapper.po.BorrowReturCountPO;
import com.henu.service.BookService;
import com.henu.service.BorrowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class BorrowServiceImpl implements BorrowService {
   @Autowired
   private BorrowMapper borrowMapper;
   @Autowired
   private UserMapper userMapper;
   @Autowired
   private BookMapper bookMapper;

   @Override
    public List<Borrow> list() {
        return borrowMapper.list();
    }
    @Override
    public PageInfo<Borrow> page(BaseRequest baseRequest){
        PageHelper.startPage(baseRequest.getPageNum(), baseRequest.getPageSize());
        List<Borrow> borrows = borrowMapper.listByCondition(baseRequest);
        for (Borrow borrow :borrows) {
            LocalDate returnDate = borrow.getReturnDate();
            LocalDate now = LocalDate.now();
            if (now.isBefore(returnDate)) {  // 当前日期比归还的日期小一天
                borrow.setNote("即将到期");
            } else if (now.isEqual(returnDate)) {
                borrow.setNote("已到期");
            } else if (now.isAfter(returnDate)) {
                borrow.setNote("已过期");
            } else {
                borrow.setNote("正常");
            }
        }

        return new PageInfo<>(borrows);
    }

    @Override
    public void save(Borrow borrow) {
        String userNo = borrow.getUserNo();
        User user = userMapper.getByUsername(userNo);
        if(Objects.isNull(user)){
            throw new ServiceException("用户不存在");
        }
        Book book = bookMapper.getByNo(borrow.getBookNo());
        if(Objects.isNull(book)){
            throw new ServiceException("图书不存在");
        }
        if(book.getNums()<1){
            throw new ServiceException("书的数量不足");
        }
        Integer account = user.getAccount();
        Integer score = book.getScore()*borrow.getDays();
        borrow.setScore(score);
        if(score>account){
            throw new ServiceException("用户积分不足");
        }
        user.setAccount(user.getAccount()-score);
        userMapper.update(user);
        book.setNums(book.getNums()-1);
        bookMapper.update(book);
        borrow.setReturnDate(LocalDate.now().plus(borrow.getDays(), ChronoUnit.DAYS));
        borrow.setScore(score);
        borrowMapper.save(borrow);
    }

    @Override
    public Borrow getById(Integer id) {
        return borrowMapper.getById(id);
    }

    @Override
    public void update(Borrow borrow) {
//       borrow.setScore(borrow.getScore());

        borrow.setUpdatetime(LocalDate.now());
        borrowMapper.update(borrow);
    }

    @Override
    public void deleteById(Integer id) {
        borrowMapper.deleteById(id);
    }

    private String category(List<String>categories){
        StringBuilder stringBuilder = new StringBuilder();
        if(CollUtil.isNotEmpty(categories)){
            categories.forEach(v -> stringBuilder.append(v).append(">"));
          return stringBuilder.substring(0,stringBuilder.lastIndexOf(">"));
        }
        return stringBuilder.toString();
    }

    @Override
    public PageInfo<Retur> pageRetur(BaseRequest baseRequest) {
        PageHelper.startPage(baseRequest.getPageNum(), baseRequest.getPageSize());
      return new PageInfo<>(borrowMapper.listReturnByCondition(baseRequest));
    }
    @Transactional
    @Override
    public void saveRetur(Retur obj) {
       obj.setStatus("已归还");
       obj.setRealDate(LocalDate.now());
       borrowMapper.updateStatus("已归还",obj.getId());
//       obj.setId(null);

       borrowMapper.saveReturn(obj);
       bookMapper.updateNumber(obj.getBookNo());

        Book book = bookMapper.getByNo(obj.getBookNo());
        if(book!=null){
            long until=0;
            if(obj.getRealDate().isBefore(obj.getReturnDate())){
                until= obj.getRealDate().until(obj.getReturnDate(),ChronoUnit.DAYS);
            }else if(obj.getRealDate().isAfter(obj.getReturnDate())){
                until=-obj.getReturnDate().until(obj.getRealDate(),ChronoUnit.DAYS);
            }
            int score =(int) until * book.getScore();
            User user = userMapper.getByUsername(obj.getUserNo());
            int account = user.getAccount() + score;
            user.setAccount(account);
            if(score<0&&account < 0) {
                    user.setStatus(false);
            }
            userMapper.update(user);
        }
    }

    @Override
    public void deleteReutrnById(Integer id) {
        borrowMapper.deleteReturnById(id);
    }




    @Override
    public Map<String, Object> getCountByTimeRange(String timeRange) {
        Map<String, Object> map = new HashMap<>();
        Date today = new Date();
        List<DateTime> dateRange;
        switch (timeRange) {
            case "week":
                // offsetDay 计算时间的一个工具方法
                // rangeToList 返回从开始时间到结束时间的一个时间范围
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -6), today, DateField.DAY_OF_WEEK);
                break;
            case "month":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -29), today, DateField.DAY_OF_MONTH);
                break;
            case "month2":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -59), today, DateField.DAY_OF_MONTH);
                break;
            case "month3":
                dateRange = DateUtil.rangeToList(DateUtil.offsetDay(today, -89), today, DateField.DAY_OF_MONTH);
                break;
            default:
                dateRange = new ArrayList<>();
        }
        //  datetimeToDateStr() 就是一个处理的方法， 把 DateTime类型的List转换成一个 String的List
        List<String> dateStrRange = datetimeToDateStr(dateRange);
        map.put("date", dateStrRange);  // x轴的日期数据生产完毕

        //  timeRange = week  month
        // getCountByTimeRange 不会统计数据库没有的日期，比如 数据库 11.4 这一天数据没有，他不会返回 date=2022-11-04,count=0 这个数据
        List<BorrowReturCountPO> borrowCount = borrowMapper.getCountByTimeRange(timeRange, 1);   // 1 borrow  2 return
        List<BorrowReturCountPO> returnCount = borrowMapper.getCountByTimeRange(timeRange, 2);
        map.put("borrow", countList(borrowCount, dateStrRange));
        map.put("retur", countList(returnCount, dateStrRange));
        return map;
    }

    // 把 DateTime类型的List转换成一个 String的List
    private List<String> datetimeToDateStr(List<DateTime> dateTimeList) {
        List<String> list = CollUtil.newArrayList();
        if (CollUtil.isEmpty(dateTimeList)) {
            return list;
        }
        for (DateTime dateTime : dateTimeList) {
            String date = DateUtil.formatDate(dateTime);
            list.add(date);
        }
        return list;
    }

    // 对数据库未统计的时间进行处理
    private List<Integer> countList(List<BorrowReturCountPO> countPOList, List<String> dateRange) {
        List<Integer> list = CollUtil.newArrayList();
        if (CollUtil.isEmpty(countPOList)) {
            return list;
        }
        for (String date : dateRange) {
            // .map(BorrowReturCountPO::getCount) 取出 对象里的 count值
            // orElse(0) 对没匹配的数据返回0
            // "2022-11-04" 没有的话 就返回0
            Integer count = countPOList.stream().filter(countPO -> date.equals(countPO.getDate()))
                    .map(BorrowReturCountPO::getCount).findFirst().orElse(0);
            list.add(count);
        }
        // 最后返回的list的元素个数会跟 dateRange 的元素个数完全一样
        // dateRange: [
        //            "2022-10-30",
        //            "2022-10-31",
        //            "2022-11-01",
        //            "2022-11-02",
        //            "2022-11-03",
        //            "2022-11-04",
        //            "2022-11-05"
        //        ],
        // borrow: [
        //            0,
        //            0,
        //            2,
        //            1,
        //            0,
        //            1,
        //            3
        //        ]
        return list;
    }

}
