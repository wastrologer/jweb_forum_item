package com.dao.mapper;

import com.pojo.Auth;

import java.util.List;

public interface AuthMapper {
//    public Auth getAuthByAuthId(String id);
    public Auth getAuthByUserName(String name);
    public List<Auth> getAuthByAccurateCondition(Auth auth);

}
