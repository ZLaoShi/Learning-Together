package com.example.entity;

import java.util.Date; 

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

@Data
@TableName("sys_log")
public class SysLog {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer accountId;
    private String operation;
    private String method;
    private String params;
    private Long time;
    private String ip;
    private Date createTime;
}