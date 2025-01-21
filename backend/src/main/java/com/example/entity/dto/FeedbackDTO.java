package com.example.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedbackDTO {
    @NotNull(message = "用户ID不能为空")
    private Integer accountId;
    
    @NotBlank(message = "反馈内容不能为空")
    private String content;
}