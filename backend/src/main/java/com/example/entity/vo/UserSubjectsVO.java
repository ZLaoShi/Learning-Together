package com.example.entity.vo;

import java.util.List;

import lombok.Data;

@Data
public class UserSubjectsVO {
    private List<Integer> goodSubjects;
    private List<Integer> needSubjects;
}
