package com.example.entity;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.example.entity.vo.TimeSlot;

import lombok.Data;

@Data
@TableName(value = "place", autoResultMap = true)
public class Place {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String location;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<TimeSlot> availableTime;
    
    private Integer status;
    private Date createTime;
    private Date updateTime;
}