package com.example.entity.vo;

import lombok.Data;
import java.util.Date;

@Data
public class StudyRequestVO {
    private Integer id;
    private Integer postId;
    private String postTitle;  // 添加帖子标题
    private Integer fromUserId;
    private String fromUsername;  // 添加发起者用户名
    private Integer toUserId;
    private String toUsername;  // 添加接收者用户名
    private String message;
    private Integer placeId;
    private String placeName;  // 添加地点名称
    private TimeSlot timeSlot;
    private Integer status;
    private Date createTime;
    private Date handleTime;
}