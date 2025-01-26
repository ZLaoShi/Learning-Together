package com.example.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.example.entity.vo.TimeSlot;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
@TableName(value = "user_profile", autoResultMap = true)
public class UserProfile {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String realName;
    private String className;
    private String phone;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<Integer> preferredPlaces;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<TimeSlot> availableTimes;

    // 添加科目相关字段
    @TableField(exist = false)  // 不映射到数据库
    private List<Integer> goodSubjects;  // 擅长的科目
    
    @TableField(exist = false)  // 不映射到数据库  
    private List<Integer> needSubjects;  // 需要帮助的科目
    
    private Date createTime;
    private Date updateTime;
}

