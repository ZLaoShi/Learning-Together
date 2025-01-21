package com.example.aspect;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson2.JSON;
import com.example.annotation.SysLogger;
import com.example.entity.SysLog;
import com.example.mapper.SysLogMapper;
import com.example.utils.JwtUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import com.auth0.jwt.interfaces.DecodedJWT;

@Aspect
@Component
@Slf4j
public class SysLogAspect {
    
    @Resource
    private SysLogMapper sysLogMapper;
    
    @Resource
    private HttpServletRequest request;

    @Resource
    private JwtUtils jwtUtils;  // 注入JwtUtils

    @Pointcut("@annotation(com.example.annotation.SysLogger)")
    public void logPointCut() {}

    @Around("logPointCut() && @annotation(sysLogger)")
    public Object around(ProceedingJoinPoint point, SysLogger sysLogger) throws Throwable {
        long beginTime = System.currentTimeMillis();
        
        Object result = point.proceed();
        
        // 记录日志
        saveSysLog(point, sysLogger, System.currentTimeMillis() - beginTime);
        
        return result;
    }

    private void saveSysLog(ProceedingJoinPoint joinPoint, SysLogger sysLogger, long time) {
        try {
            SysLog sysLog = new SysLog();
            
            // 获取当前登录用户
            String token = request.getHeader("Authorization"); 
            if(token != null) {
                sysLog.setAccountId(getUserIdFromToken(token));
            }
            
            sysLog.setOperation(sysLogger.value());
            sysLog.setMethod(joinPoint.getSignature().getDeclaringTypeName() + "." 
                    + joinPoint.getSignature().getName());
                    
            // 对于文件上传接口,不记录参数
            if(!joinPoint.getSignature().getName().contains("upload")) {
                sysLog.setParams(JSON.toJSONString(joinPoint.getArgs()));
            } else {
                sysLog.setParams("文件上传,参数未记录");
            }
            
            sysLog.setTime(time);
            sysLog.setIp(getIpAddr());
            sysLog.setCreateTime(new Date());
            
            sysLogMapper.insert(sysLog);
        } catch (Exception e) {
            log.error("记录系统日志失败", e);
        }
    }

    private String getIpAddr() {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private Integer getUserIdFromToken(String token) {
        DecodedJWT jwt = jwtUtils.resolveJwt(token);
        return jwt != null ? jwtUtils.toId(jwt) : null;
    }
}