package com.example.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.example.entity.vo.TimeSlot;

import lombok.Data;

@Data
@TableName("match_record")
public class MatchRecord {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("user_id_1")
    private Integer userId1;  // 发起匹配者
    @TableField("user_id_2")
    private Integer userId2;  // 被匹配者
    private String matchType; // subject/time/place/complement
    private Integer subjectId;
    private Integer placeId;
    
    @TableField(typeHandler = JacksonTypeHandler.class)
    private TimeSlot timeSlot;
    
    private BigDecimal matchScore;
    private Integer status;  // 0待确认,1已确认,2已拒绝,3已完成
    private Date createTime;
    private Date confirmTime;
    private Date completeTime;
}

