package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Account;
import com.example.entity.MatchRecord;
import com.example.entity.StudyRequest;
import com.example.entity.UserProfile;
import com.example.entity.UserSubject;
import com.example.entity.dto.RegisterDTO;
import com.example.mapper.AccountMapper;
import com.example.mapper.MatchMapper;
import com.example.mapper.PostMapper;
import com.example.mapper.StudyRequestMapper;
import com.example.mapper.SysLogMapper;
import com.example.mapper.UserProfileMapper;
import com.example.mapper.UserSubjectMapper;
import com.example.service.AccountService;
import com.example.utils.FlowUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Resource
    FlowUtils flowUtils;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    PasswordEncoder encoder;

    @Resource
    private SysLogMapper sysLogMapper;

    @Resource
    private UserProfileMapper userProfileMapper;

    @Resource
    private UserSubjectMapper userSubjectMapper;

    @Resource
    private PostMapper postMapper;

    @Resource
    private MatchMapper matchMapper;

    @Resource
    private StudyRequestMapper studyRequestMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = this.findAccountByName(username);
        if (account == null)
            throw new UsernameNotFoundException("用户名或密码错误");
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    @Override
    public Account findAccountByName(String text) {
        return this.query()
                .eq("username", text)
                .one();
    }

    @Override
    public String registerEmailAccount(RegisterDTO vo) {
        String username = vo.getUsername();
        if(this.existsAccountByUsername(username)) 
            return "此用户名已被注册";

        String password = encoder.encode(vo.getPassword());
        Account account = new Account(null, username, password, "user", new Date());
        
        if(this.save(account)) {
            return null;
        } else {
            return "内部错误，请联系管理员";
        }
    }

    @Override
    public String resetEmailAccountPassword(RegisterDTO vo) {
        String password = encoder.encode(vo.getPassword());
        boolean update = this.update().set("password", password).update();
        return update ? null : "更新失败，请联系管理员";
    }

    @Override
    public List<Account> listAllAccounts() {
        return this.list();
    }

    @Override
    public boolean createAccount(Account account) {
        // 验证用户名否已存在
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.eq("username", account.getUsername());
        if(this.count(wrapper) > 0)
            return false;
            
        // 设置默认值
        account.setRegistertime(new Date());
        account.setPassword(encoder.encode(account.getPassword()));
        if(account.getRole() == null) {
            account.setRole("user");
        }
        
        return this.save(account);
    }

    @Override
    public boolean updateAccount(Account account) {
        Account exist = this.getById(account.getId());
        if(exist == null) return false;
        
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.ne("id", account.getId())
               .eq("username", account.getUsername());
        if(this.count(wrapper) > 0)
            return false;

        account.setPassword(exist.getPassword());
        account.setRegistertime(exist.getRegistertime());
        
        return this.updateById(account);
    }

    @Override
    @Transactional
    public boolean deleteAccount(Integer id) {
        try {
            // 1. 检查是否存在未处理的匹配
            Long pendingMatchCount = matchMapper.selectCount(
                new QueryWrapper<MatchRecord>()
                    .eq("status", 0)
                    .and(w -> w.eq("user_id_1", id).or().eq("user_id_2", id))
            );
            if(pendingMatchCount > 0) {
                throw new RuntimeException("存在未处理的匹配请求，无法删除用户");
            }

            // 2. 检查是否存在未处理的学习请求
            Long pendingRequestCount = studyRequestMapper.selectCount(
                new QueryWrapper<StudyRequest>()
                    .eq("status", 0)
                    .and(w -> w.eq("from_user_id", id).or().eq("to_user_id", id)) 
            );
            if(pendingRequestCount > 0) {
                throw new RuntimeException("存在未处理的学习请求，无法删除用户");
            }

            // 3. 删除用户画像
            userProfileMapper.delete(
                new QueryWrapper<UserProfile>().eq("user_id", id)
            );

            // 4. 删除用户科目关联
            userSubjectMapper.delete(
                new QueryWrapper<UserSubject>().eq("user_id", id)
            );

            // 5. 最后删除账号
            return this.removeById(id);
        } catch (Exception e) {
            throw new RuntimeException("删除用户失败: " + e.getMessage());
        }
    }

    @Override
    public boolean resetPassword(Integer id, String newPassword) {
        return this.update()
                .eq("id", id)
                .set("password", encoder.encode(newPassword))
                .update();
    }

    private boolean existsAccountByUsername(String username) {
        return this.baseMapper.exists(Wrappers.<Account>query()
                .eq("username", username));
    }
}
