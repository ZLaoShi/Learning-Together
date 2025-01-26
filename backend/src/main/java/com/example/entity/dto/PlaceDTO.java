package com.example.entity.dto;

import java.util.List;

import com.example.entity.vo.TimeSlot;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PlaceDTO {
    private Integer id;
    @NotEmpty(message = "场地名称不能为空")
    private String name;
    private String location;
    private List<TimeSlot> availableTime;
    private Integer status;
}
