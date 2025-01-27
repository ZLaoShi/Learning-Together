package com.example.entity;

import lombok.Data;
import java.util.Date;
import com.example.entity.vo.TimeSlot;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

@Data
@TableName(value = "study_request", autoResultMap = true)
public class StudyRequest {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer postId;  // 关联帖子id
    private Integer fromUserId;  // 发起请求的用户id
    private Integer toUserId;  // 接收请求的用户id
    private String message;  // 请求留言 
    private Integer placeId;  // 期望学习地点
    @TableField(typeHandler = JacksonTypeHandler.class)
    private TimeSlot timeSlot; // 期望学习时间段
    private Integer status;  // 状态:0待处理,1已接受,2已拒绝
    private Date createTime;  // 发起时间
    private Date handleTime;  // 处理时间
}