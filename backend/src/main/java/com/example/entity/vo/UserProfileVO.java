package com.example.entity.vo;

import lombok.Data;
import java.util.List;

@Data
public class UserProfileVO {
    private Integer id;
    private Integer userId;
    private String realName;
    private String className;
    private String phone;
    private List<Integer> preferredPlaces;
    private List<TimeSlot> availableTimes;
    private List<Integer> goodSubjects;  // 擅长科目
    private List<Integer> needSubjects;  // 需要帮助的科目
}
