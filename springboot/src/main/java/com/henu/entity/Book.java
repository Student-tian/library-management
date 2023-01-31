package com.henu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.cache.annotation.SpringCacheAnnotationParser;

import java.time.LocalDate;
import java.util.List;

@Data
public class Book {
    private Integer id;
    private String name;
    private String description;
    private String category;
    private String bookNo;
    private String author;
    private String publishDate;
    private String publisher;
    private String cover;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate createtime;
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate updatetime;
    private List<String>categories;

    private Integer score;
    private Integer nums;
}
