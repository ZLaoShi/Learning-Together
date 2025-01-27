package com.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.StudyRequest;
import java.util.List;

@Mapper
public interface StudyRequestMapper extends BaseMapper<StudyRequest> {
    
    @Select("SELECT * FROM study_request WHERE from_user_id = #{userId} OR to_user_id = #{userId}")
    List<StudyRequest> getUserRequests(Integer userId);
    
    @Select("SELECT * FROM study_request WHERE to_user_id = #{userId} AND status = 0")
    List<StudyRequest> getPendingRequests(Integer userId);
}