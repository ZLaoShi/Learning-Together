package com.example.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AiChatDTO {
    @NotNull(message = "用户ID不能为空")
    private Integer accountId;
    
    @NotBlank(message = "问题不能为空")
    private String prompt;

    private String imagePath;   // 新增可选图片路径

}