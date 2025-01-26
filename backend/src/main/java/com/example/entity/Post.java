package com.example.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.example.entity.vo.TimeSlot;

import lombok.Data;

@Data
@TableName("post")
public class Post {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private String type;  // help求助/resource资源
    private Integer subjectId;
    private Integer placeId;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    private TimeSlot timeSlot;
    
    private Integer inviteEnabled;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
