package com.dao.mapper;

import com.pojo.Application;

import java.util.List;

public interface ApplicationMapper {
    public List<Application> getApplicationByCondition(Application application);
    public Integer countApplicationByCondition(Application application);
    public Integer addApplication(Application application);
    public Integer updateApplication(Application application);
    public Integer deleteApplicationById(Integer id);
}
