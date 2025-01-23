package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Account;
import com.example.entity.dto.RegisterDTO;
import com.example.mapper.AccountMapper;
import com.example.mapper.SysLogMapper;
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
        // 验证用户名和手机号是否已存在
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
        return this.removeById(id);
    } catch (Exception e) {
        log.error("删除用户失败", e);
        throw new RuntimeException("删除用户失败");
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
