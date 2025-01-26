package com.example.entity.vo;

import java.util.Date;

import lombok.Data;

@Data
public class PostVO {
    private Integer id;
    private Integer userId;
    private String username; // 添加发布者用户\名
    private String title;
    private String content;
    private String type;
    private Integer subjectId;
    private String subjectName; // 添加科目名称
    private Integer placeId;
    private String placeName; // 添加场地名称
    private TimeSlot timeSlot;
    private Integer inviteEnabled;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}
