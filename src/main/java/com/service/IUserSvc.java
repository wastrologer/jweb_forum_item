package com.service;

import com.github.pagehelper.PageInfo;
import com.pojo.Auth;
import com.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface IUserSvc {

    User selectUser(Integer id);
    public void addConcerned(Integer userFromId,List<User> users);
    //count
    public Integer countAllUser();
    public Integer countUserByRegionName(String name);
    public Integer countUserByAuthName(String name);
    public Integer countUserByAuthType(String type);
    public Integer countUserByConcernFrom(Integer from_id);
    public Integer countUserByConcernTo(Integer to_id);
    //select list by default page
    public PageInfo getAllUserByPage(Integer pageNum);
    public PageInfo getUserByRegionNameAndPage(String name, Integer pageNum);
    public PageInfo getUserByAuthNameAndPage(String name,Integer pageNum);
    public PageInfo getUserByAuthTypeAndPage(String type,Integer pageNum);
    public PageInfo getUserByConcernFromAndPage(Integer from_id,Integer pageNum);
    public PageInfo getUserByConcernToAndPage(Integer to_id,Integer pageNum);
    public PageInfo getUserByFuzzyConditionAndPage(User user,Integer pageNum);
    //select list by specific page
    public PageInfo getAllUserByPage(Integer pageNum,Integer pageSize);
    public PageInfo getUserByRegionNameAndPage(String name,Integer pageNum,Integer pageSize);
    public PageInfo getUserByAuthNameAndPage(String name,Integer pageNum,Integer pageSize);
    public PageInfo getUserByAuthTypeAndPage(String type,Integer pageNum,Integer pageSize);
    public PageInfo getUserByConcernFromAndPage(Integer from_id,Integer pageNum,Integer pageSize);
    public PageInfo getUserByConcernToAndPage(Integer to_id,Integer pageNum,Integer pageSize);
    public PageInfo getUserByFuzzyConditionAndPage(User user,Integer pageNum,Integer pageSize);
    public PageInfo getUserOrderByFans(Integer pageNum,Integer pageSize);
    public List<User> getUserByIds(Set<Integer> userIds);


    //select one
    public User getUserById(Integer id);
    public User getUserByPhoneNumber(String num);
    public User getUserByUserName(String name);
    public User getUserByInviteCode(String code);
    public User getUserById(Integer mine,Integer id);
    public User getUserByPhoneNumber(Integer mine,String num);
    public User getUserByUserName(Integer mine,String name);
    public User getUserByInviteCode(Integer mine,String code);
    public User getUserByToken(String token);
    public User getUserByAccurateCondition(User user);
    public User getUserByAccurateCondition(Integer mine,User user) ;

    public Auth getAuthByAuthId(String name);
    public Auth getAuthByAuthName(String name);
    public Auth getAuthByUserName(String name);
    public Auth getAuthByAccurateCondition(Auth auth);

    //is exist
    public boolean isExistById(Integer id);
    public boolean isExistByPhoneNumber(String num);
    public boolean isExistByUserName(String name);
    public boolean isExistByInviteCode(String code);
    //conform to standard
    public boolean conformToPhoneNumber(String num);
    public boolean conformToUserName(String name);
    //write
    public Integer addUser(User user);
    public Integer updateUserWithCoinAndGold(User user);
    public Integer updateUserOnly(User user);
    public Integer deleteUserById(Integer id);
    //
    public String generalToken(String userName, String platform, String signType)throws Exception;

}
