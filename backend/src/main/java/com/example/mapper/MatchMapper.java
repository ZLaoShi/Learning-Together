package com.example.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.MatchRecord;

@Mapper
public interface MatchMapper extends BaseMapper<MatchRecord> {
    
    // 查询用户的匹配记录（包括作为发起方和被匹配方）
    @Select("SELECT * FROM match_record WHERE user_id_1 = #{userId} OR user_id_2 = #{userId} ORDER BY create_time DESC")
    List<MatchRecord> getUserMatches(Integer userId);
    
    // 查询待确认的匹配请求
    @Select("SELECT * FROM match_record WHERE user_id_2 = #{userId} AND status = 0")
    List<MatchRecord> getPendingMatches(Integer userId);
    
    // 确认匹配
    @Update("UPDATE match_record SET status = 1, confirm_time = NOW() WHERE id = #{id} AND user_id_2 = #{userId}")
    int confirmMatch(Integer id, Integer userId);
    
    // 拒绝匹配
    @Update("UPDATE match_record SET status = 2 WHERE id = #{id} AND user_id_2 = #{userId}")
    int rejectMatch(Integer id, Integer userId);
    
    // 完成匹配
    @Update("UPDATE match_record SET status = 3, complete_time = NOW() WHERE id = #{id} AND (user_id_1 = #{userId} OR user_id_2 = #{userId})")
    int completeMatch(Integer id, Integer userId);
}
