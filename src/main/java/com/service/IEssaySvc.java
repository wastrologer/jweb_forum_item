package com.service;

import com.common.entity.ReturnMessage;
import com.github.pagehelper.PageInfo;
import com.pojo.*;

import java.util.List;

public interface IEssaySvc {
    public PageInfo getEssayByCollectionAndPage(Collection collection, Integer pageNum, Integer pageSize);
    public PageInfo getEssayByConditionAndPage(EssayParam essayParam, Integer pageNum, Integer pageSize);
    public PageInfo getEssayByConcernAndPage(Integer userId, Integer pageNum);
    public PageInfo getEssayByConcernAndPage(Integer userId,Integer pageNum,Integer pageSize);
    public PageInfo getEssayByUserListAndPage(List<Integer> list,Integer isPublished, Integer pageNum);
    public Integer countEssayByCondition(Essay essay);
    public PageInfo getEssayByFuzzyConditionAndPage(Essay essay, Integer pageNum);
    public PageInfo getEssayByFuzzyConditionAndPage(Essay essay,Integer pageNum,Integer pageSize);
    public Essay getEssayByAccurateCondition(Essay essay);
    public Essay getEssayByEssayId(Integer id);

    public Integer addEssay(User user, Essay essay);
    public Integer updateEssayClickNum(ReturnMessage msg);
    public Integer updateEssayCommentNum(ReturnMessage msg);
    public Integer updateEssayRecommendNum(ReturnMessage msg);


    public Integer updateEssaySelf(User user, Essay essay);
    public Integer updateEssayByOthers( Essay essay);
    public Integer updateEssay(Essay essay);
    public Integer updateEssayBatch(User user);
    public Integer deleteEssayById(Integer id);
    public PageInfo getEssayByUserListAndPage(List<Integer> ids, Integer isPublished, Integer num, Integer size);

    public PageInfo getTopicByConditionAndPage(Topic topic, Integer pageNum, Integer pageSize);

}
