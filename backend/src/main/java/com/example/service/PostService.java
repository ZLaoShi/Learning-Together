package com.example.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Post;
import com.example.entity.dto.PostDTO;
import com.example.entity.vo.PostVO;

public interface PostService extends IService<Post> {
    List<PostVO> listAllPosts();
    PostVO getPostById(Integer id);
    boolean createPost(PostDTO dto);
    boolean updatePost(PostDTO dto);
    boolean deletePost(Integer id);
}
