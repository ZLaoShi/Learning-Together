package com.example.entity.vo.response;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class HealthRecordVO {
    private Integer id;
    private Integer memberId;
    private String accountName;    // 新增账户名
    private String memberName;     // 新增家属姓名
    private String relationship;   // 新增关系
    private BigDecimal bloodOxygen;    
    private String bloodPressure;      
    private Integer heartRate;         
    private BigDecimal bloodSugarFasting;    
    private BigDecimal bloodSugarAfterMeal;  
    private BigDecimal gait;          
    private BigDecimal gaitSymmetry;  
    private Date recordTime;
    private String notes;
}