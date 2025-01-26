package com.example.entity.dto;

import java.util.List;

import com.example.entity.vo.TimeSlot;

import lombok.Data;


@Data
public class UserProfileDTO {
    private Integer id;
    private Integer userId; 
    private String realName;
    private String className;
    private String phone;
    private List<Integer> preferredPlaces; // JSON存储
    private List<TimeSlot> availableTimes; // JSON存储
}
