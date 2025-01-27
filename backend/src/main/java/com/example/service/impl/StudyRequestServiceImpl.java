package com.example.service.impl;

import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Account;
import com.example.entity.Place;
import com.example.entity.StudyRequest;
import com.example.entity.dto.StudyRequestDTO;
import com.example.entity.vo.PostVO;
import com.example.entity.vo.StudyRequestVO;
import com.example.service.AccountService;
import com.example.service.PlaceService;
import com.example.service.PostService;
import com.example.service.StudyRequestService;
import com.example.mapper.StudyRequestMapper;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;

@Service
public class StudyRequestServiceImpl extends ServiceImpl<StudyRequestMapper, StudyRequest> 
        implements StudyRequestService {

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private HttpServletRequest request;

    @Resource
    private AccountService accountService;

    @Resource 
    private PostService postService;

    @Resource
    private PlaceService placeService;

    @Override
    public List<StudyRequestVO> getUserRequests() {
        Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
        return this.baseMapper.getUserRequests(userId).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudyRequestVO> getPendingRequests() {
        Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
        return this.baseMapper.getPendingRequests(userId).stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean createRequest(StudyRequestDTO dto) {
        Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
        
        StudyRequest request = new StudyRequest();
        BeanUtils.copyProperties(dto, request);
        request.setFromUserId(userId);
        request.setStatus(0);
        request.setCreateTime(new Date());
        
        return this.save(request);
    }

    @Override
    public boolean acceptRequest(Integer id) {
        Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
        StudyRequest req = this.getById(id);
        
        // 检查:记录存在 + 是接收者 + 状态为待处理
        if(req == null || !req.getToUserId().equals(userId) || req.getStatus() != 0) {
            return false;
        }
        
        req.setStatus(1);
        req.setHandleTime(new Date());
        return this.updateById(req);
    }

    @Override
    public boolean rejectRequest(Integer id) {
        Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
        StudyRequest req = this.getById(id);
        
        // 检查:记录存在 + 是接收者 + 状态为待处理
        if(req == null || !req.getToUserId().equals(userId) || req.getStatus() != 0) {
            return false;
        }
        
        req.setStatus(2);
        req.setHandleTime(new Date());
        return this.updateById(req);
    }

    private StudyRequestVO convertToVO(StudyRequest request) {
        StudyRequestVO vo = new StudyRequestVO();
        BeanUtils.copyProperties(request, vo);
        
        // 填充关联信息
        if(request.getFromUserId() != null) {
            Account from = accountService.getById(request.getFromUserId());
            if(from != null) vo.setFromUsername(from.getUsername());
        }
        
        if(request.getToUserId() != null) {
            Account to = accountService.getById(request.getToUserId());
            if(to != null) vo.setToUsername(to.getUsername());
        }
        
        if(request.getPostId() != null) {
            PostVO post = postService.getPostById(request.getPostId());
            if(post != null) vo.setPostTitle(post.getTitle());
        }
        
        if(request.getPlaceId() != null) {
            Place place = placeService.getById(request.getPlaceId());
            if(place != null) vo.setPlaceName(place.getName());
        }
        
        return vo;
    }
}