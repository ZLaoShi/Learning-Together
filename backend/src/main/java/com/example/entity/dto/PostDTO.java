package com.example.entity.dto;

import com.example.entity.vo.TimeSlot;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PostDTO {
    private Integer id;
    @NotEmpty(message = "标题不能为空")
    private String title;
    @NotEmpty(message = "内容不能为空")
    private String content;
    @NotEmpty(message = "类型不能为空")
    private String type;  // help求助/resource资源
    private Integer subjectId;
    private Integer placeId;
    private TimeSlot timeSlot;
    private Integer inviteEnabled;
}

