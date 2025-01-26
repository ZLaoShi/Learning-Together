package com.example.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.UserProfile;
import com.example.entity.dto.UserProfileDTO;
import com.example.entity.vo.UserProfileVO;
import com.example.entity.vo.UserSubjectsVO;

public interface UserProfileService extends IService<UserProfile> {
    UserProfileVO getCurrentUserProfile();
    boolean updateProfile(UserProfileDTO dto);
    UserProfile getUserProfile(Integer userId);
    boolean updateUserSubjects(Integer userId, List<Integer> goodSubjects, List<Integer> needSubjects);

    // UserSubjectsVO getUserSubjects(Integer userId);
}
