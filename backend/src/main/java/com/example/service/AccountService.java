package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Account;
import com.example.entity.vo.requst.EmailRegisterVO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface AccountService extends IService<Account>, UserDetailsService {
    Account findAccountByNameOrPhone(String text);

    String registerEmailAccount(EmailRegisterVO vo);
    String resetEmailAccountPassword(EmailRegisterVO vo);

    // 管理员方法
    List<Account> listAllAccounts();  // 获取所有用户账户
    boolean createAccount(Account account);  // 创建新账户
    boolean updateAccount(Account account);  // 更新账户信息
    boolean deleteAccount(Integer id);  // 删除账户
    boolean resetPassword(Integer id, String newPassword);  // 重置密码
}
