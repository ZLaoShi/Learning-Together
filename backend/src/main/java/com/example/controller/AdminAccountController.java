package com.example.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.annotation.SysLogger;
import com.example.entity.Account;
import com.example.entity.RestBean;
import com.example.service.AccountService;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/admin/account")
@PreAuthorize("hasRole('admin')")  // 要求管理员权限
public class AdminAccountController {
    
    @Resource
    private AccountService service;

    @GetMapping("/")
    @SysLogger("获取所有用户列表")
    public RestBean<List<Account>> listAllAccounts() {
        return RestBean.success(service.listAllAccounts());
    }

    @PostMapping("/")
    @SysLogger("创建用户")
    public RestBean<Void> createAccount(@RequestBody Account account) {
        return service.createAccount(account) ?
            RestBean.success() :
            RestBean.failure(400, "创建失败");
    }

    @PutMapping("/")
    @SysLogger("更新用户信息")
    public RestBean<Void> updateAccount(@RequestBody Account account) {
        return service.updateAccount(account) ?
            RestBean.success() :
            RestBean.failure(400, "更新失败");
    }

    @DeleteMapping("/{id}") 
    @SysLogger("删除用户")
    public RestBean<Void> deleteAccount(@PathVariable Integer id) {
        return service.deleteAccount(id) ?
            RestBean.success() :  
            RestBean.failure(400, "删除失败");
    }

    @PostMapping("/reset-password")
    @SysLogger("重置用户密码")
    public RestBean<Void> resetPassword(@RequestParam Integer id, @RequestParam String newPassword) {
        return service.resetPassword(id, newPassword) ?
            RestBean.success() :
            RestBean.failure(400, "重置失败");
    }
}