package com.example.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Post;
import com.example.entity.dto.PostDTO;
import com.example.entity.vo.PostVO;

import jakarta.servlet.http.HttpServletResponse;

public interface PostService extends IService<Post> {
    List<PostVO> listAllPosts();
    PostVO getPostById(Integer id);
    boolean createPost(PostDTO dto);
    boolean updatePost(PostDTO dto);
    boolean deletePost(Integer id);

    boolean updatePostStatus(Integer id, Integer status);
    Map<String, Object> getPostStats();
    // 导出统计数据到Excel
    void exportPostStats(HttpServletResponse response) throws IOException;
}
