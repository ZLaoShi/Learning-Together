package com.example.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.SysLog;

public interface SysLogService extends IService<SysLog> {
    // 保存日志
    void saveSysLog(SysLog sysLog);
    
    // 获取用户日志
    List<SysLog> getUserLogs(Integer accountId);

     // 获取用户活跃度统计
     Map<String, Long> getUserActivityStats();
    
    // 获取操作统计
    Map<String, Long> getOperationStats();
    
    // 清理过期日志
    void cleanOldLogs();
}