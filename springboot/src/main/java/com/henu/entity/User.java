package com.henu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.beans.Transient;
import java.util.Date;

@Data
public class User {
    private Integer id;
    private String name;
    private String username;
    private Integer age;
    private Integer score;
    private String sex;
    private String phone;
    private String address;
    private  boolean status;
    private Integer account;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createtime;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date updatetime;
//    private String account;
}
