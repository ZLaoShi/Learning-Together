package com.example.entity.vo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class MatchVO {
    private Integer id;
    private Integer userId1;
    private String username1;
    private Integer userId2; 
    private String username2;
    private String matchType;
    private Integer subjectId;
    private String subjectName;
    private Integer placeId;
    private String placeName;
    private TimeSlot timeSlot;
    private BigDecimal matchScore;
    private Integer status;
    private Date createTime;
    private Date confirmTime;
    private Date completeTime;
}
