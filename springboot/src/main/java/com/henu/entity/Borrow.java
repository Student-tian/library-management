package com.henu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class Borrow {

    private Integer id;
    private String bookName;
    private String bookNo;
    private String userNo;
    private String userPhone;
    private String userName;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate createtime;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate updatetime;
    private Integer score;
//    private Integer nums;
    private String status;
    private Integer days;
    private LocalDate returnDate;
    private String note;
}
