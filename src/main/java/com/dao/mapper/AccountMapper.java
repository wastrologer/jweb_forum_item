package com.dao.mapper;

import com.pojo.Account;

import java.util.List;

public interface AccountMapper {
    public List<Account> getAccountByCondition(Account account);
    public Integer countAccountByCondition(Account account);
    public Integer addAccount(Account account);
    public Integer updateAccount(Account account);
    public Integer deleteAccountById(Integer id);
}
