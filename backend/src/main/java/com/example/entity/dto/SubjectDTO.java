package com.example.entity.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SubjectDTO {
    private Integer id;
    @NotEmpty(message = "科目名称不能为空")
    private String name;
    private Integer status;
}
