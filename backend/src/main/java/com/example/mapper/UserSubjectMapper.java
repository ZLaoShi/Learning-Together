package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.UserSubject;

@Mapper
public interface UserSubjectMapper extends BaseMapper<UserSubject> {
    @Select("SELECT * FROM user_subject WHERE user_id = #{userId}")
    List<UserSubject> getUserSubjects(Integer userId);

    @Delete("DELETE FROM user_subject WHERE user_id = #{userId}")
    int deleteUserSubjects(Integer userId);
}
