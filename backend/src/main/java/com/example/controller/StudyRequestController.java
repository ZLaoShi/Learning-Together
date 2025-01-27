package com.example.controller;

import org.springframework.web.bind.annotation.*;
import com.example.entity.RestBean;
import com.example.entity.vo.StudyRequestVO;
import com.example.entity.dto.StudyRequestDTO;
import com.example.service.StudyRequestService;
import com.example.annotation.SysLogger;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/study-request")
public class StudyRequestController {

    @Resource
    private StudyRequestService service;

    @GetMapping("/")
    @SysLogger("获取学习请求列表")
    public RestBean<List<StudyRequestVO>> getUserRequests() {
        return RestBean.success(service.getUserRequests());
    }
    
    @GetMapping("/pending")
    @SysLogger("获取待处理的学习请求")
    public RestBean<List<StudyRequestVO>> getPendingRequests() {
        return RestBean.success(service.getPendingRequests());
    }
    
    @PostMapping("/")
    @SysLogger("发起学习请求")
    public RestBean<Void> createRequest(@RequestBody @Valid StudyRequestDTO dto) {
        return service.createRequest(dto) ? 
            RestBean.success() : 
            RestBean.failure(400, "创建失败");
    }
    
    @PostMapping("/accept/{id}")
    @SysLogger("接受学习请求")
    public RestBean<Void> acceptRequest(@PathVariable Integer id) {
        return service.acceptRequest(id) ? 
            RestBean.success() : 
            RestBean.failure(400, "操作失败");
    }
    
    @PostMapping("/reject/{id}")
    @SysLogger("拒绝学习请求")
    public RestBean<Void> rejectRequest(@PathVariable Integer id) {
        return service.rejectRequest(id) ? 
            RestBean.success() : 
            RestBean.failure(400, "操作失败");
    }
}