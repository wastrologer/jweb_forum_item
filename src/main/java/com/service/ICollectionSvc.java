package com.service;

import com.github.pagehelper.PageInfo;
import com.pojo.Collection;
import com.pojo.User;

public interface ICollectionSvc {
    public Integer countCollectionByCondition(Collection collection);
    public Collection getCollectionByAccurateCondition(Collection collection);
    public PageInfo getCollectionByConditionAndPage(Collection collection, Integer pageNum, Integer pageSize);
    public PageInfo getCollectionByConditionAndPage(Collection collection, Integer pageNum);
    public Integer addCollection(User user, Collection collection);
    public Integer addCollection(User user, Integer essayId);
    public Integer countCollectionByEssayId(Integer essayId);

    public Integer updateCollection(User user, Collection collection);
    public Integer deleteCollectionByCondition(Collection collection);
    public Integer addCollection(Collection collection);
    public Integer updateCollection(Collection collection);
    public Integer deleteCollectionByCondition(User user, Collection collection);
    public Integer deleteCollectionByEssayId(int userId, int essayId);


}
