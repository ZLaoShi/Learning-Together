package com.example.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.SysLog;

@Mapper
public interface SysLogMapper extends BaseMapper<SysLog> {
    // 获取某用户的操作日志
    @Select("SELECT * FROM sys_log WHERE account_id = #{accountId} ORDER BY create_time DESC")
    List<SysLog> getLogsByAccountId(Integer accountId);
    
    // 获取某类操作的日志统计
    @Select("SELECT operation, COUNT(*) as count FROM sys_log GROUP BY operation")
    List<Map<String, Object>> getOperationStats();
    
    @Select("SELECT account_id, COUNT(*) as count " +
            "FROM sys_log " +
            "GROUP BY account_id")
    List<Map<String, Object>> getUserActivityStats();
}