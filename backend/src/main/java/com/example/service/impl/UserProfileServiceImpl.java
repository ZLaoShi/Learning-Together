package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Account;
import com.example.entity.UserProfile;
import com.example.entity.UserSubject;
import com.example.entity.dto.UserProfileDTO;
import com.example.entity.vo.UserProfileVO;
import com.example.mapper.AccountMapper;
import com.example.mapper.UserProfileMapper;
import com.example.mapper.UserSubjectMapper;
import com.example.service.UserProfileService;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserProfileServiceImpl extends ServiceImpl<UserProfileMapper, UserProfile> implements UserProfileService {
    
    @Resource
    private JwtUtils jwtUtils;

    @Resource 
    private HttpServletRequest request;

    @Resource
    private UserSubjectMapper userSubjectMapper;

    @Resource
    AccountMapper accountMapper;

    @Override
    public UserProfileVO getUserProfileByUsername(String username) {
        // 通过用户名找到用户ID
        Account account = accountMapper.selectOne(
            new QueryWrapper<Account>().eq("username", username)
        );
        if(account == null) {
            return new UserProfileVO();
        }

        // 查询用户画像
        UserProfile profile = this.lambdaQuery()
                .eq(UserProfile::getUserId, account.getId())
                .one();
                
        // 转换为VO
        UserProfileVO vo = new UserProfileVO();
        if(profile != null) {
            BeanUtils.copyProperties(profile, vo);
            
            // 查询用户的科目关联信息
            List<UserSubject> subjects = userSubjectMapper.selectList(
                new QueryWrapper<UserSubject>()
                    .eq("user_id", account.getId()));
            
            vo.setGoodSubjects(subjects.stream()
                    .filter(s -> "good".equals(s.getType()))
                    .map(UserSubject::getSubjectId)
                    .collect(Collectors.toList()));
                    
            vo.setNeedSubjects(subjects.stream()
                    .filter(s -> "need".equals(s.getType()))
                    .map(UserSubject::getSubjectId)
                    .collect(Collectors.toList()));
        }
        
        return vo;
    } // TO DO 待加入判断是否有同意的请求逻辑
    
    @Override
    public UserProfileVO getCurrentUserProfile() {
        // 获取当前登录用户ID
        Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
        UserProfile profile = this.lambdaQuery()
                .eq(UserProfile::getUserId, userId)
                .one();
        if(profile == null) {
            return new UserProfileVO();
        }

        // 转换为VO
        UserProfileVO vo = new UserProfileVO();
        BeanUtils.copyProperties(profile, vo);
        
        // 查询用户的科目关联信息
        List<UserSubject> subjects = userSubjectMapper.selectList(
            new QueryWrapper<UserSubject>()
                .eq("user_id", userId));
        
        // 分类处理科目
        vo.setGoodSubjects(subjects.stream()
                .filter(s -> "good".equals(s.getType()))
                .map(UserSubject::getSubjectId)
                .collect(Collectors.toList()));
                
        vo.setNeedSubjects(subjects.stream()
                .filter(s -> "need".equals(s.getType()))
                .map(UserSubject::getSubjectId)
                .collect(Collectors.toList()));
        
        return vo;
    }

    @Override
    public boolean updateProfile(UserProfileDTO dto) {
        // 1. 获取当前用户ID
        Integer userId = jwtUtils.toId(jwtUtils.resolveJwt(request.getHeader("Authorization")));
        
        // 2. 查找现有profile
        UserProfile profile = this.lambdaQuery()
                .eq(UserProfile::getUserId, userId)
                .one();
                
        if(profile == null) {
            // 3. 如果不存在则创建新的profile
            profile = new UserProfile();
            profile.setUserId(userId);  // 设置user_id
            profile.setCreateTime(new Date());
        }
        
        // 直接设置JSON字段
        profile.setRealName(dto.getRealName());
        profile.setClassName(dto.getClassName());
        profile.setPhone(dto.getPhone());
        profile.setPreferredPlaces(dto.getPreferredPlaces());
        profile.setAvailableTimes(dto.getAvailableTimes());
        profile.setUpdateTime(new Date());
        
        return this.saveOrUpdate(profile);
    }

    @Override
    public UserProfile getUserProfile(Integer userId) {
        UserProfile profile = this.lambdaQuery()
                .eq(UserProfile::getUserId, userId)
                .one();
                
        if(profile != null) {
            // 查询用户的科目关联信息
            List<UserSubject> subjects = userSubjectMapper.selectList(new QueryWrapper<UserSubject>()
                    .eq("user_id", userId));
            
            // 分类处理
            profile.setGoodSubjects(subjects.stream()
                    .filter(s -> "good".equals(s.getType()))
                    .map(UserSubject::getSubjectId)
                    .collect(Collectors.toList()));
                    
            profile.setNeedSubjects(subjects.stream()
                    .filter(s -> "need".equals(s.getType()))
                    .map(UserSubject::getSubjectId)
                    .collect(Collectors.toList()));
        }
        
        return profile;
    }

    @Override
    public boolean updateUserSubjects(Integer userId, List<Integer> goodSubjects, List<Integer> needSubjects) {
        try {
            // 1. 删除现有科目关联
            userSubjectMapper.deleteUserSubjects(userId);
            
            // 2. 添加擅长科目
            if(goodSubjects != null) {
                for(Integer subjectId : goodSubjects) {
                    UserSubject subject = new UserSubject();
                    subject.setUserId(userId);
                    subject.setSubjectId(subjectId);
                    subject.setType("good");
                    // 移除createTime设置
                    userSubjectMapper.insert(subject);
                }
            }
            
            // 3. 添加需要帮助的科目
            if(needSubjects != null) {
                for(Integer subjectId : needSubjects) {
                    UserSubject subject = new UserSubject();
                    subject.setUserId(userId);
                    subject.setSubjectId(subjectId);
                    subject.setType("need");
                    // 移除createTime设置
                    userSubjectMapper.insert(subject);
                }
            }
            
            return true;
        } catch(Exception e) {
            log.error("更新用户科目关联失败", e);
            return false;
        }
    }
}
