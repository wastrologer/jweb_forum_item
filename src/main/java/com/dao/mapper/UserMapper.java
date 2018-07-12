package com.dao.mapper;

import com.pojo.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


public interface UserMapper {
    /*test*/
    public User selectUser(int id);
    //count
    public int countAllUser();
    public int countUserByRegionName(String name);
    public int countUserByAuthName(String name);
    public int countUserByAuthType(String type);
    public int countUserByConcernFrom(int from_id);
    public int countUserByConcernTo(int to_id);
    //select list
    public List<User> getAllUser();
    public List<User> getUserByRegionName(String name);
    public List<User> getUserByAuthName(String name);
    public List<User> getUserByAuthType(String type);
    public List<User> getUserByConcernFrom(int from_id);
    public List<User> getUserByConcernTo(int to_id);
    public List<User> getUserByFuzzyCondition(User user);
    public List<User> getUserByAccurateCondition(User user);
    public List<User> getUserOrderByFans();
    public List<User> getUserByIds(@Param("userIds")Set<Integer> userIds);
    //select one
/*
    public User getUserById(int id);
    public User getUserByPhoneNumber(String num);
    public User getUserByUserName(String name);
    public User getUserByInviteCode(String code);
*/

    //write
    public int addUser(User user);
    public int updateUser(User user);
    public int deleteUserById(int id);

}
