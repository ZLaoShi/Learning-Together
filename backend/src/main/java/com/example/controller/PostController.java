package com.example.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import com.example.entity.RestBean;
import com.example.entity.dto.PostDTO;
import com.example.entity.vo.PostVO;
import com.example.service.PostService;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/post")
public class PostController {
    
    @Resource
    private PostService service;

    @GetMapping("/")
    @SysLogger("获取帖子列表")
    public RestBean<List<PostVO>> listPosts() {
        return RestBean.success(service.listAllPosts());
    }

    @GetMapping("/{id}")
    @SysLogger("获取帖子详细")
    public RestBean<PostVO> getPost(@PathVariable Integer id) {
        PostVO post = service.getPostById(id);
        return post != null ? 
            RestBean.success(post) : 
            RestBean.failure(404, "帖子不存在");
    }

    @PostMapping("/")
    @SysLogger("发布帖子")
    public RestBean<Void> createPost(@RequestBody @Valid PostDTO dto) {
        return service.createPost(dto) ? 
            RestBean.success() : 
            RestBean.failure(400, "发布失败");
    }

    @PutMapping("/")
    @SysLogger("更新帖子")
    public RestBean<Void> updatePost(@RequestBody @Valid PostDTO dto) {
        return service.updatePost(dto) ? 
            RestBean.success() : 
            RestBean.failure(400, "更新失败");
    }

    @DeleteMapping("/{id}")
    @SysLogger("删除帖子")
    public RestBean<Void> deletePost(@PathVariable Integer id) {
        return service.deletePost(id) ?
            RestBean.success() :
            RestBean.failure(400, "删除失败");
    }

    @GetMapping("/admin/post")
    @SysLogger("管理员获取所有帖子")
    @PreAuthorize("hasRole('admin')")
    public RestBean<List<PostVO>> listAllPosts() {
        return RestBean.success(service.listAllPosts());
    }
    
    @PutMapping("/admin/status")
    @SysLogger("更新帖子状态")
    @PreAuthorize("hasRole('admin')") 
    public RestBean<Void> updatePostStatus(@RequestParam Integer id, @RequestParam Integer status) {
        return service.updatePostStatus(id, status) ?
            RestBean.success() :
            RestBean.failure(400, "更新失败");
    }

    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('admin')")
    @SysLogger("获取发帖统计")
    public RestBean<Map<String, Object>> getPostStats() {
        return RestBean.success(service.getPostStats());
    }

    @GetMapping("/admin/stats/export")
    @PreAuthorize("hasRole('admin')")
    @SysLogger("导出帖子统计")
    public void exportPostStats(HttpServletResponse response) throws IOException {
        service.exportPostStats(response);
    }
    
}
