package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("user_subject")
public class UserSubject {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private Integer subjectId;
    private String type; // good/need
}
