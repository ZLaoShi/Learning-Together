package com.example.controller;

import com.example.annotation.SysLogger;
import com.example.entity.RestBean;
import com.example.entity.vo.requst.EmailRegisterVO;
import com.example.service.AccountService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;
import java.util.function.Supplier;

@CrossOrigin
@Validated
@RestController
@RequestMapping("/api/auth")
public class AuthorizeController {
    @Resource
    AccountService service;

    @PostMapping("/register")
    @SysLogger("注册")
    public RestBean<Void> register(@RequestBody @Valid EmailRegisterVO vo) {
        return this.messageHandle(vo, service::registerEmailAccount);
    }

    @PostMapping("/reset-password")
    @SysLogger("修改密码")
    public RestBean<Void> resetPassword(@RequestBody @Valid EmailRegisterVO vo){
        return this.messageHandle(vo, service::resetEmailAccountPassword);
    }

    private <T> RestBean<Void> messageHandle(T vo, Function<T, String> function) {
        return messageHandle(() -> function.apply(vo));
    }

    private RestBean<Void> messageHandle(Supplier<String> action) {
        String message = action.get();
        return message == null ? RestBean.success() : RestBean.failure(400, message);
    }
}
