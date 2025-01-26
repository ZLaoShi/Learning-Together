package com.example.service.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Account;
import com.example.entity.Place;
import com.example.entity.Post;
import com.example.entity.Subject;
import com.example.entity.dto.PostDTO;
import com.example.entity.vo.PostVO;
import com.example.mapper.PostMapper;
import com.example.service.AccountService;
import com.example.service.PlaceService;
import com.example.service.PostService;
import com.example.service.SubjectService;
import com.example.utils.JwtUtils;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Resource
    private SubjectService subjectService;
    
    @Resource
    private PlaceService placeService;

    @Resource
    private AccountService accountService;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private HttpServletRequest request;

    @Override
    public List<PostVO> listAllPosts() {
        return this.list().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public PostVO getPostById(Integer id) {
        Post post = this.getById(id);
        return post != null ? convertToVO(post) : null;
    }

    @Override
    public boolean createPost(PostDTO dto) {
        Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
        
        Post post = new Post();
        BeanUtils.copyProperties(dto, post);
        post.setUserId(userId);
        post.setStatus(1);
        post.setCreateTime(new Date());
        post.setUpdateTime(new Date());
        
        return this.save(post);
    }

    @Override
    public boolean updatePost(PostDTO dto) {
        Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
        Post post = this.getById(dto.getId());
        
        if(post == null) return false;

        // 获取用户角色
        String role = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();
        
        // 验证是管理员或发帖者本人
        if(!post.getUserId().equals(userId) && !"ROLE_admin".equals(role)) {
            return false;
        }
            
        BeanUtils.copyProperties(dto, post);
        post.setUpdateTime(new Date());
        return this.updateById(post);
    }

    @Override
    public boolean deletePost(Integer id) {
        Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
        Post post = this.getById(id);
        
        if(post == null) return false;

        String role = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();
        
        if(!post.getUserId().equals(userId) && !"ROLE_admin".equals(role)) {
            return false;
        }
            
        return this.removeById(id);
    }

    private PostVO convertToVO(Post post) {
        PostVO vo = new PostVO();
        BeanUtils.copyProperties(post, vo);
        
        // 填充关联信息
        Account account = accountService.getById(post.getUserId());
        if(account != null) {
            vo.setUsername(account.getUsername());
        }
        
        if(post.getSubjectId() != null) {
            Subject subject = subjectService.getById(post.getSubjectId());
            if(subject != null) {
                vo.setSubjectName(subject.getName());
            }
        }
        
        if(post.getPlaceId() != null) {
            Place place = placeService.getById(post.getPlaceId());
            if(place != null) {
                vo.setPlaceName(place.getName());
            }
        }
        
        return vo;
    }
}
