package com.dao;

import com.pojo.User;


public interface IUserDao {
    User selectUser(int id);
}
