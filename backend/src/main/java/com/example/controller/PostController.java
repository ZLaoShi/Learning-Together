package com.example.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.RestBean;
import com.example.entity.dto.PostDTO;
import com.example.entity.vo.PostVO;
import com.example.service.PostService;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/post")
public class PostController {
    
    @Resource
    private PostService service;

    @GetMapping("/")
    public RestBean<List<PostVO>> listPosts() {
        return RestBean.success(service.listAllPosts());
    }

    @GetMapping("/{id}")
    public RestBean<PostVO> getPost(@PathVariable Integer id) {
        PostVO post = service.getPostById(id);
        return post != null ? 
            RestBean.success(post) : 
            RestBean.failure(404, "帖子不存在");
    }

    @PostMapping("/")
    public RestBean<Void> createPost(@RequestBody @Valid PostDTO dto) {
        return service.createPost(dto) ? 
            RestBean.success() : 
            RestBean.failure(400, "发布失败");
    }

    @PutMapping("/")
    public RestBean<Void> updatePost(@RequestBody @Valid PostDTO dto) {
        return service.updatePost(dto) ? 
            RestBean.success() : 
            RestBean.failure(400, "更新失败");
    }

    @DeleteMapping("/{id}")
    public RestBean<Void> deletePost(@PathVariable Integer id) {
        return service.deletePost(id) ?
            RestBean.success() :
            RestBean.failure(400, "删除失败");
    }
}
