package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.dto.Account;
import com.example.entity.vo.requst.EmailRegisterVO;
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
        Account account = this.findAccountByNameOrPhone(username);
        if (account == null)
            throw new UsernameNotFoundException("用户名或密码错误_test");
        return User
                .withUsername(username)
                .password(account.getPassword())
                .roles(account.getRole())
                .build();
    }

    public Account findAccountByNameOrPhone(String text) {
        return this.query()
                .eq("username", text).or()
                .eq("phone", text)
                .one();
    }

    @Override
    public String registerEmailAccount(EmailRegisterVO vo) { //注册逻辑
        log.info("账户信息:[{}]", vo.toString());
        String phone = vo.getPhone();
        String username = vo.getUsername();

        if(this.existsAccountByPhone(phone)) return "此手机号已被注册";
        if(this.existsAccountByUsername(username)) return "此用户名已被注册";

        String password = encoder.encode(vo.getPassword());
        Account account = new Account(null, username, password, phone, "user", new Date());

        if(this.save(account)) {
            return null;
        } else {
            return "内部错误，请联系管理员";
        }
    }

    @Override
    public String resetEmailAccountPassword(EmailRegisterVO vo) {
        String phone = vo.getPhone();
        String password = encoder.encode(vo.getPassword());
        boolean update = this.update().eq("phone", phone).set("password", password).update();
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
        wrapper.eq("username", account.getUsername())
                .or()
                .eq("phone", account.getPhone());
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
        // 验证目标账户是否存在
        Account exist = this.getById(account.getId());
        if(exist == null) return false;
        
        // 验证用户名和手机号是否与其他账户冲突
        QueryWrapper<Account> wrapper = new QueryWrapper<>();
        wrapper.ne("id", account.getId())
                .and(w -> w.eq("username", account.getUsername())
                        .or()
                        .eq("phone", account.getPhone()));
        if(this.count(wrapper) > 0)
            return false;

        // 保留原密码和注册时间
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

    private boolean existsAccountByPhone(String phone) {
        return this.baseMapper.exists(Wrappers.<Account>query().eq("phone", phone));
    }

    private boolean existsAccountByUsername(String username) {
        return this.baseMapper.exists(Wrappers.<Account>query().eq("username", username));
    }
}
