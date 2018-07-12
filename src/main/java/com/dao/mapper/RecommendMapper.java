package com.dao.mapper;

import com.pojo.Recommend;

import java.util.List;

public interface RecommendMapper {
    public List<Recommend> getRecommendByCondition(Recommend recommend);
    public Integer countRecommendByCondition(Recommend recommend);
    public Integer addRecommend(Recommend recommend);
    public Integer updateRecommend(Recommend recommend);
    public Integer deleteRecommendById(Integer id);
}
