package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Account;
import com.example.entity.dto.RegisterDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AccountService extends IService<Account>, UserDetailsService {
    Account findAccountByName(String text);

    String registerEmailAccount(RegisterDTO vo);
    String resetEmailAccountPassword(RegisterDTO vo);

    // 管理员方法
    List<Account> listAllAccounts();  // 获取所有用户账户
    boolean createAccount(Account account);  // 创建新账户
    boolean updateAccount(Account account);  // 更新账户信息
    boolean deleteAccount(Integer id);  // 删除账户
    boolean resetPassword(Integer id, String newPassword);  // 重置密码
}
