package com.example.entity.vo.response;

import lombok.Data;

import java.util.Date;

@Data
public class AuthorizeVO {
    String username;
    Integer id;     // 添加用户ID字段
    String role;
    String token;
    Date expire;
}
