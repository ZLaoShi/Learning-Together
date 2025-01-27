package com.example.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Post;

@Mapper
public interface PostMapper extends BaseMapper<Post> {
    // 按类型统计
    @Select("SELECT type, COUNT(*) as count FROM post GROUP BY type")
    List<Map<String, Object>> getPostTypeStats();
    
    // 按状态统计
    @Select("SELECT status, COUNT(*) as count FROM post GROUP BY status")
    List<Map<String, Object>> getPostStatusStats();
    
    // 按科目统计
    @Select("SELECT s.name as subject_name, COUNT(p.id) as count " +
            "FROM subject s " +
            "LEFT JOIN post p ON s.id = p.subject_id " +
            "GROUP BY s.id, s.name")
    List<Map<String, Object>> getPostSubjectStats();
    
    // 按时间段统计(最近30天)
    @Select("SELECT DATE(create_time) as date, COUNT(*) as count " +
            "FROM post " +
            "WHERE create_time >= DATE_SUB(CURDATE(), INTERVAL 30 DAY) " +
            "GROUP BY DATE(create_time)")
    List<Map<String, Object>> getPostTrendStats();
}
