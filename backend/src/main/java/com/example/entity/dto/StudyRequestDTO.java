package com.example.entity.dto;

import lombok.Data;
import com.example.entity.vo.TimeSlot;

@Data
public class StudyRequestDTO {
    private Integer postId;
    private Integer toUserId;
    private String message;
    private Integer placeId;
    private TimeSlot timeSlot;
}