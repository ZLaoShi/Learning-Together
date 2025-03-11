package com.example.controller;

import com.example.annotation.SysLogger;
import com.example.entity.RestBean;
import com.example.entity.dto.SubjectDTO;
import com.example.entity.vo.SubjectVO;
import com.example.service.SubjectService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/subject")
public class SubjectController {
    
    @Resource
    private SubjectService service;

    @GetMapping("/")
    @SysLogger("获取科目列表")
    public RestBean<List<SubjectVO>> listSubjects() {
        return RestBean.success(service.listAllSubjects());
    }

    @GetMapping("/{id}")
    @SysLogger("获取科目详情")
    public RestBean<SubjectVO> getSubject(@PathVariable Integer id) {
        SubjectVO subject = service.getSubjectById(id);
        return subject != null ? 
            RestBean.success(subject) : 
            RestBean.failure(404, "科目不存在");
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('admin')")
    public RestBean<Void> createSubject(@RequestBody @Valid SubjectDTO dto) {
        return service.createSubject(dto) ? 
            RestBean.success() : 
            RestBean.failure(400, "创建失败");
    }

    @PutMapping("/")
    @PreAuthorize("hasRole('admin')")
    @SysLogger("更新科目状态")
    public RestBean<Void> updateSubject(@RequestBody @Valid SubjectDTO dto) {
        return service.updateSubject(dto) ? 
            RestBean.success() : 
            RestBean.failure(400, "更新失败");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    @SysLogger("禁用科目")
    public RestBean<Void> deleteSubject(@PathVariable Integer id) {
        return service.deleteSubject(id) ? 
            RestBean.success() : 
            RestBean.failure(400, "禁用失败");
    }
}