package com.example.entity.vo;

import lombok.Data;
import java.util.Date;

@Data
public class SubjectVO {
    private Integer id;
    private String name;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}