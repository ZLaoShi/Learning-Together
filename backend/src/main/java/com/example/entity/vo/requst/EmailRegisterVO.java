package com.example.entity.vo.requst;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户注册表单信息
 */
@Data
public class EmailRegisterVO {
    @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    @Length(min = 10, max = 15)
    String phone;
    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$")
    @Length(min = 1, max = 10)
    String username;
    @Length(min = 6, max = 20)
    String password;
}
