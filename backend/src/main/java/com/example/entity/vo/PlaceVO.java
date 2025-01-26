package com.example.entity.vo;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class PlaceVO {
    private Integer id;
    private String name;
    private String location;
    private List<TimeSlot> availableTime;
    private Integer status;
    private Date createTime;
    private Date updateTime;
}