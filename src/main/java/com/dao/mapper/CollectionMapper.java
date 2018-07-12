package com.dao.mapper;

import com.pojo.Collection;

import java.util.List;

public interface CollectionMapper {
    public List<Collection> getCollectionByCondition(Collection collection);
    public Integer countCollectionByCondition(Collection collection);
    public Integer addCollection(Collection collection);
    public Integer updateCollection(Collection collection);
    public Integer deleteCollectionByCondition(Collection collection);
}
