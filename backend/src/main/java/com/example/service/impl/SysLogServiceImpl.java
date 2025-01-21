package com.example.service.impl;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.SysLog;
import com.example.mapper.SysLogMapper;
import com.example.service.SysLogService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {
    
    @Resource
    private SysLogMapper sysLogMapper;
    
    @Override
    public void saveSysLog(SysLog sysLog) {
        try {
            if(sysLog.getCreateTime() == null) {
                sysLog.setCreateTime(new Date());
            }
            this.save(sysLog);
        } catch(Exception e) {
            log.error("保存系统日志失败", e);
        }
    }
    
    @Override
    public List<SysLog> getUserLogs(Integer accountId) {
        return sysLogMapper.getLogsByAccountId(accountId);
    }
    
    @Override
    public Map<String, Long> getOperationStats() {
        try {
            List<Map<String, Object>> stats = sysLogMapper.getOperationStats();
            Map<String, Long> result = new HashMap<>();
            stats.forEach(map -> {
                String operation = (String)map.get("operation");
                Long count = ((Number)map.get("count")).longValue();
                result.put(operation, count);
            });
            return result;
        } catch(Exception e) {
            log.error("获取操作统计失败", e);
            return new HashMap<>();
        }
    }
    
    @Override
    public void cleanOldLogs() {
        // 删除30天前的日志
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        this.remove(new QueryWrapper<SysLog>()
            .lt("create_time", thirtyDaysAgo));
    }

    @Override
    public Map<String, Long> getUserActivityStats() {
        try {
            List<Map<String, Object>> stats = sysLogMapper.getUserActivityStats();
            Map<String, Long> result = new HashMap<>();
            
            // 转换统计结果
            stats.forEach(map -> {
                String userId = String.valueOf(map.get("account_id"));
                Long count = ((Number) map.get("count")).longValue();
                result.put("用户" + userId, count);
            });
            
            return result;
        } catch (Exception e) {
            log.error("获取用户活跃度统计失败", e);
            return new HashMap<>();
        }
    }
}