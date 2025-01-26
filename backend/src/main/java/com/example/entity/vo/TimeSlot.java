package com.example.entity.vo;

import lombok.Data;

@Data
public class TimeSlot {
    private Integer weekday; // 1-7
    private String time; // HH:mm-HH:mm    
}
