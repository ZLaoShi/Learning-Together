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
import com.example.entity.vo.MatchVO;
import com.example.entity.vo.TimeSlot;
import com.example.service.MatchService;

import jakarta.annotation.Resource;

@RestController
@RequestMapping("/api/match")
public class MatchController {
    
    @Resource
    private MatchService service;

    // 获取用户的匹配记录
    @GetMapping("/user")
    @SysLogger("获取用户的匹配记录")
    public RestBean<List<MatchVO>> getUserMatches() {
        return RestBean.success(service.getUserMatches());
    }

    // 获取待确认的匹配请求
    @GetMapping("/pending")
    @SysLogger("获取待确认的匹配请求")
    public RestBean<List<MatchVO>> getPendingMatches() {
        return RestBean.success(service.getPendingMatches());
    }

    // 确认匹配
    @PostMapping("/confirm/{id}")
    @SysLogger("确认匹配")
    public RestBean<Void> confirmMatch(@PathVariable Integer id) {
        return service.confirmMatch(id) ? 
            RestBean.success() : 
            RestBean.failure(400, "确认失败");
    }

    // 拒绝匹配
    @PostMapping("/reject/{id}")
    @SysLogger("拒绝匹配")
    public RestBean<Void> rejectMatch(@PathVariable Integer id) {
        return service.rejectMatch(id) ? 
            RestBean.success() : 
            RestBean.failure(400, "拒绝失败");
    }

    // 完成匹配
    @PostMapping("/complete/{id}")
    @SysLogger("完成匹配")
    public RestBean<Void> completeMatch(@PathVariable Integer id) {
        return service.completeMatch(id) ?
            RestBean.success() :
            RestBean.failure(400, "更新失败");
    }


    @GetMapping("/subject")
    @SysLogger("结对匹配")
    public RestBean<List<MatchVO>> matchBySubject() {
        return RestBean.success(service.matchBySubject());
    }

    // 按时间匹配
    @GetMapping("/time")  
    @SysLogger("按时间匹配")
    public RestBean<List<MatchVO>> matchByTime() {
        return RestBean.success(service.matchByTime());
    }

    // 按地点匹配
    @GetMapping("/place")
    @SysLogger("按地点匹配")
    public RestBean<List<MatchVO>> matchByPlace() {
        return RestBean.success(service.matchByPlace());
    }

    // 互补匹配
    @GetMapping("/complement")
    @SysLogger("互补匹配")
    public RestBean<List<MatchVO>> matchByComplement() {
        return RestBean.success(service.matchByComplement());
    }
}