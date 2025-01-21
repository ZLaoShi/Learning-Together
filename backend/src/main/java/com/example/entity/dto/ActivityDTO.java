package com.example.entity.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ActivityDTO {
    private Integer id;
    private String title;             // 活动标题
    private String description;       // 活动描述
    private Date startTime;          // 开始时间
    private Date endTime;            // 结束时间
    private Integer maxParticipants; // 最大参与人数
}