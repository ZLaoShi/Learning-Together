package com.example.entity.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class HealthRecordDTO {
    private Integer memberId;          // 家属ID
    private BigDecimal bloodOxygen;    // 血氧
    private String bloodPressure;      // 血压
    private Integer heartRate;         // 心率
    private BigDecimal bloodSugarFasting;    // 空腹血糖
    private BigDecimal bloodSugarAfterMeal;  // 餐后血糖
    private BigDecimal gait;          // 步伐
    private BigDecimal gaitSymmetry;  // 步态对称性
    private String notes;             // 备注
}