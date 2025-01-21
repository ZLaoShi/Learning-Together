package com.example.entity.dto;

import java.util.List;

import lombok.Data;

@Data
public class SignUpDTO {
    private Integer activityId;  // 活动ID
    private Integer accountId;   // 账户ID(主账号)
    private List<Integer> memberIds;    // 家属ID
}