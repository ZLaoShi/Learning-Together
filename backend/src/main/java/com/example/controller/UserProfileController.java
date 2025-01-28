package com.example.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.annotation.SysLogger;
import com.example.entity.RestBean;
import com.example.entity.dto.UserProfileDTO;
import com.example.entity.vo.UserProfileVO;
import com.example.service.UserProfileService;
import com.example.utils.JwtUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/profile")
public class UserProfileController {
    @Resource 
    private UserProfileService service;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private HttpServletRequest request;  // 添加request注入

    @GetMapping("/")
    @SysLogger("获取用户画像")
    public RestBean<UserProfileVO> getUserProfile() {
        return RestBean.success(service.getCurrentUserProfile());
    }

    @GetMapping("/{username}")
    @SysLogger("查询用户画像")
    public RestBean<UserProfileVO> getUserProfileByUsername(@PathVariable String username) {
        return RestBean.success(service.getUserProfileByUsername(username));
    }

    @PostMapping("/")
    @SysLogger("更新用户画像")
    public RestBean<Void> updateProfile(@RequestBody @Valid UserProfileDTO dto) {
        return service.updateProfile(dto) ? 
            RestBean.success() : 
            RestBean.failure(400, "更新失败");
    }

    // 添加新接口
    @PostMapping("/subjects")
    @SysLogger("更新用户科目绑定")
    public RestBean<Void> updateSubjects(@RequestParam List<Integer> goodSubjects, 
            @RequestParam List<Integer> needSubjects) {
            Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
            return service.updateUserSubjects(userId, goodSubjects, needSubjects) ? 
                RestBean.success() : 
                RestBean.failure(400, "更新失败"); 
        }
}
