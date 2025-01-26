package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("subject")
public class Subject {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}