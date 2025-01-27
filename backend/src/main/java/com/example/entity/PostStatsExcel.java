package com.example.entity;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.Data;

@Data
public class PostStatsExcel {
    @ExcelProperty("统计维度")
    private String dimension;
    
    @ExcelProperty("数量")
    private Long count;
}
