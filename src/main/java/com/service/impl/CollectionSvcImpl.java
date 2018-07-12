package com.service.impl;

import com.common.cache.CacheClient;
import com.common.utils.SvcUtils;
import com.dao.mapper.CollectionMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pojo.Collection;
import com.pojo.User;
import com.service.ICollectionSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;
@Service
public class CollectionSvcImpl implements ICollectionSvc {
    private static final Logger logger = LoggerFactory.getLogger(CollectionSvcImpl.class);
    private Integer defaultPageSize=2;

    @Resource
    private CacheClient cacheClient;
    @Resource
    CollectionMapper collectionaaMapper;
    @Resource
    SvcUtils svcUtils;


    @Override
    public Integer countCollectionByCondition(com.pojo.Collection collection) {
        return collectionaaMapper.countCollectionByCondition(collection);
    }

    @Override
    public Integer countCollectionByEssayId(Integer essayId) {
        Collection collection=new Collection();
        collection.setEssayId(essayId);
        return countCollectionByCondition(collection);
    }

    @Override
    public PageInfo getCollectionByConditionAndPage(com.pojo.Collection collection, Integer pageNum, Integer pageSize) {
        if(pageNum==null)
            pageNum=1;
        if(pageSize==null||pageSize<=0)
            pageSize=defaultPageSize;
        PageHelper.startPage(pageNum,pageSize);
        List<com.pojo.Collection> collectionList=collectionaaMapper.getCollectionByCondition(collection);
        PageInfo pageInfo=new PageInfo(collectionList);
        return pageInfo;
    }

    @Override
    public PageInfo getCollectionByConditionAndPage(com.pojo.Collection collection, Integer pageNum) {
        return getCollectionByConditionAndPage(collection,pageNum,defaultPageSize);
    }

    @Override
    public Integer addCollection(User user, com.pojo.Collection collection) {
        if(user==null)
            return null;
        collection.setUserId(user.getUserId());
        return addCollection(collection);
    }

    @Override
    public Integer addCollection(User user, Integer essayId) {
        Collection collection=new Collection();
        collection.setEssayId(essayId);
        return addCollection(user,collection);
    }

    @Override
    public Integer updateCollection(User user, com.pojo.Collection collection) {
        if(user==null||user.getUserId()!=collection.getUserId())
            return null;
        return updateCollection(collection);
    }

    @Override
    public Integer deleteCollectionByCondition(User user, com.pojo.Collection collection) {
        Collection c=getCollectionByAccurateCondition(collection);
        if(collection.getEssayId()==null||user.getUserId()==null||user.getUserId()!=c.getUserId())
            return null;
        return deleteCollectionByCondition(collection);
    }

    @Override
    public Integer deleteCollectionByEssayId(int userId, int essayId) {
        Collection collection=new Collection();
        collection.setEssayId(essayId);
        collection.setUserId(userId);
        return deleteCollectionByCondition(collection);
    }

    @Override
    public Integer addCollection(com.pojo.Collection collection) {

        collection.setCreateTime(new Timestamp(System.currentTimeMillis()));
        collection.setCollectionTime(new Timestamp(System.currentTimeMillis()));
        int cNum=collectionaaMapper.addCollection(collection);
        logger.info("collectionMapper.addCollection结果为"+cNum);
        return cNum;
    }



    @Override
    public com.pojo.Collection getCollectionByAccurateCondition(com.pojo.Collection collection) {
        List<com.pojo.Collection> result=collectionaaMapper.getCollectionByCondition(collection);
        return svcUtils.judgeResultList(result);
    }

    @Override
    public Integer updateCollection(com.pojo.Collection collection) {
        return collectionaaMapper.updateCollection(collection);
    }

    @Override
    public Integer deleteCollectionByCondition(Collection collection) {
        return collectionaaMapper.deleteCollectionByCondition(collection);
    }

    public Integer getDefaultPageSize() {
        return defaultPageSize;
    }

    public void setDefaultPageSize(Integer defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    public CacheClient getCacheClient() {
        return cacheClient;
    }

    public void setCacheClient(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
    }

    public CollectionMapper getCollectionMapper() {
        return collectionaaMapper;
    }

    public void setCollectionMapper(CollectionMapper collectionMapper) {
        this.collectionaaMapper = collectionMapper;
    }
}
