package com.example.config;

import com.example.entity.Account;
import com.example.entity.RestBean;
import com.example.entity.SysLog;
import com.example.entity.vo.AuthorizeVO;
import com.example.filter.JwtAuthenticationFilter;
import com.example.service.AccountService;
import com.example.service.SysLogService;
import com.example.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@Configuration
public class SecurityConfiguration {

    @Resource
    JwtUtils utils;

    @Resource
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Resource
    AccountService accountService;

    @Resource
    private SysLogService sysLogService;  // 注入日志服务

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(conf -> conf
                        .requestMatchers("/api/auth/**", "/error").permitAll()
                        .requestMatchers("/uploads/**").permitAll() // 允许访问上传的文件
                        .requestMatchers(HttpMethod.GET, "/api/post/").permitAll()  // 放行帖子列表
                        .requestMatchers(HttpMethod.GET, "/api/post/{id}").permitAll()  // 放行帖子详情
                        .anyRequest()
                        .authenticated())
                .formLogin(conf -> conf
                        .loginProcessingUrl("/api/auth/login")
                        .successHandler(this::onAuthenticationSuccess)
                        .failureHandler(this::onAuthenticationFailure))
                .logout(conf -> conf
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessHandler(this::onLogoutSuccess)

                )
                .exceptionHandling(conf -> conf
                        .authenticationEntryPoint(this::onUnauthorized)
                        .accessDeniedHandler(this::onAccessDeny))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(conf -> conf
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    public void onAuthenticationSuccess(HttpServletRequest request,
                                  HttpServletResponse response,
                                  Authentication authentication) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        User user = (User) authentication.getPrincipal();
        Account account = accountService.findAccountByName(user.getUsername());
        String token = utils.createJwt(user, account.getId(), account.getUsername());
        
        AuthorizeVO vo = new AuthorizeVO();
        vo.setExpire(utils.expireTime());
        vo.setRole(account.getRole());
        vo.setToken(token);
        vo.setUsername(account.getUsername());
        vo.setId(account.getId());

        // 先构建响应数据
        RestBean<AuthorizeVO> restBean = RestBean.success(vo);
        String responseJson = restBean.asJsonString();
        
        // 记录登录日志，包含响应数据
        SysLog loginLog = new SysLog();
        loginLog.setAccountId(account.getId());
        loginLog.setOperation("用户登录");
        loginLog.setMethod("AuthenticationSuccess");
        loginLog.setIp(request.getRemoteAddr());
        loginLog.setCreateTime(new Date());
        loginLog.setResponse(responseJson); // 存储完整响应
        sysLogService.saveSysLog(loginLog);
        
        // 发送响应
        response.getWriter().write(responseJson);
    }

    

    public void onLogoutSuccess(HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication) throws IOException {
            
    response.setContentType("application/json;charset=utf-8");
    PrintWriter writer = response.getWriter();
    String authorization = request.getHeader("Authorization");
    if (utils.invalidateJwt(authorization)) {
    writer.write(RestBean.success().asJsonString());
    } else {
    writer.write(RestBean.failure(400, "退出登录失败").asJsonString());
    }

    }



    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {

        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.unauthorized(exception.getMessage()).asJsonString());

    }


    public void onAccessDeny(HttpServletRequest request,
                             HttpServletResponse response,
                             AccessDeniedException accessDeniedException) throws IOException{
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.forbidden(accessDeniedException.getMessage()).asJsonString());

    }

    public void onUnauthorized(HttpServletRequest request,
                               HttpServletResponse response,
                               AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(RestBean.unauthorized(authException.getMessage()).asJsonString());
    }

}
