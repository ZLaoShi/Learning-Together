package com.example.service.impl;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Account;
import com.example.entity.Place;
import com.example.entity.Post;
import com.example.entity.PostStatsExcel;
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
import jakarta.servlet.http.HttpServletResponse;
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

    @Resource
    private PostMapper postMapper;

    @Override
    public List<PostVO> listAllPosts() {
        // 获取当前用户角色
        String role = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();
                
        // 管理员可以看到所有帖子,普通用户只能看到未关闭的
        if("ROLE_admin".equals(role)) {
            return this.list().stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());
        } else {
            return this.lambdaQuery()
                    .ne(Post::getStatus, 2)  // 过滤掉状态为2(已关闭)的帖子
                    .ne(Post::getStatus, 0) // 过滤掉状态为0(待审核)的帖子
                    .list()
                    .stream()
                    .map(this::convertToVO)
                    .collect(Collectors.toList());
        }
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
        
        // 验证是管理员或发帖者本人
        if(!post.getUserId().equals(userId) && !"ROLE_admin".equals(role)) {
            return false;
        }
            
        post.setStatus(2);  // 设置为已关闭状态
        post.setUpdateTime(new Date());
        return this.updateById(post);
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

    @Override
    public boolean updatePostStatus(Integer id, Integer status) {
        Post post = this.getById(id);
        if(post == null) return false;
        
        post.setStatus(status);
        post.setUpdateTime(new Date());
        return this.updateById(post);
    }

    @Override
    public Map<String, Object> getPostStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 1. 类型统计
        stats.put("typeStats", postMapper.getPostTypeStats());
        
        // 2. 状态统计
        stats.put("statusStats", postMapper.getPostStatusStats());
        
        // 3. 科目统计
        stats.put("subjectStats", postMapper.getPostSubjectStats());
        
        // 4. 时间趋势
        stats.put("trendStats", postMapper.getPostTrendStats());
        
        return stats;
    }

    @Override
    public void exportPostStats(HttpServletResponse response) throws IOException {
        // 1. 获取统计数据
        Map<String, Object> stats = this.getPostStats();
        
        // 2. 组装Excel数据
        List<PostStatsExcel> typeStats = ((List<Map<String,Object>>)stats.get("typeStats")).stream()
            .map(map -> {
                PostStatsExcel excel = new PostStatsExcel();
                excel.setDimension("帖子类型-" + (map.get("type").equals("help") ? "求助" : "资源"));
                excel.setCount(((Number)map.get("count")).longValue());
                return excel;
            }).collect(Collectors.toList());
            
        List<PostStatsExcel> statusStats = ((List<Map<String,Object>>)stats.get("statusStats")).stream()
            .map(map -> {
                PostStatsExcel excel = new PostStatsExcel();
                String status = map.get("status").toString();
                String statusText = switch(status) {
                    case "0" -> "待审核";
                    case "1" -> "已发布";
                    case "2" -> "已关闭";
                    default -> "未知";
                };
                excel.setDimension("帖子状态-" + statusText);
                excel.setCount(((Number)map.get("count")).longValue());
                return excel;
            }).collect(Collectors.toList());
            
        List<PostStatsExcel> subjectStats = ((List<Map<String,Object>>)stats.get("subjectStats")).stream()
            .map(map -> {
                PostStatsExcel excel = new PostStatsExcel();
                excel.setDimension("科目-" + map.get("subject_name"));
                excel.setCount(((Number)map.get("count")).longValue());
                return excel;
            }).collect(Collectors.toList());
            
        // 3. 导出Excel
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("帖子统计", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        
        try {
            // 导出多个sheet
            ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream()).build();
            
            // 写入类型统计
            WriteSheet typeSheet = EasyExcel.writerSheet(0, "帖子类型统计")
                    .head(PostStatsExcel.class).build();
            excelWriter.write(typeStats, typeSheet);
            
            // 写入状态统计
            WriteSheet statusSheet = EasyExcel.writerSheet(1, "帖子状态统计")
                    .head(PostStatsExcel.class).build();
            excelWriter.write(statusStats, statusSheet);
            
            // 写入科目统计
            WriteSheet subjectSheet = EasyExcel.writerSheet(2, "帖子科目统计")
                    .head(PostStatsExcel.class).build();
            excelWriter.write(subjectStats, subjectSheet);
            
            excelWriter.finish();
        } catch (Exception e) {
            log.error("导出Excel失败", e);
            throw e;
        }
    }
}
