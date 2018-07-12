package com.service.impl;

import com.common.cache.CacheClient;
import com.common.entity.ReturnMessage;
import com.common.utils.SvcUtils;
import com.common.utils.UserInfoUtil;
import com.dao.mapper.EssayMapper;
import com.dao.mapper.TopicMapper;
import com.dao.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pojo.*;
import com.service.IEssaySvc;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
@Service
public class EssaySvcImpl implements IEssaySvc {
    private Integer defaultPageSize=100;

    @Resource
    private CacheClient cacheClient;
    @Resource
    EssayMapper essayMapper;
    @Resource
    TopicMapper topicMapper;
    @Resource
    private SvcUtils svcUtils;
    @Resource
    private UserMapper userMapper;

    public User getUserById(Integer id) {
        User user=new User();
        user.setUserId(id);
        return getUserByAccurateCondition(1,user);
    }

    public User getUserByAccurateCondition(Integer mine,User user) {
        List<User> result=userMapper.getUserByAccurateCondition(user);
        if(mine!=1){
            UserInfoUtil.concealUserInfo(result);
        }
        return svcUtils.judgeResultList(result);
    }

    @Override
    public PageInfo getEssayByCollectionAndPage(Collection collection, Integer pageNum, Integer pageSize) {
        if(pageNum==null)
            pageNum=1;
        if(pageSize==null||pageSize<=0)
            pageSize=defaultPageSize;
        PageHelper.startPage(pageNum,pageSize);
        List<Essay> essayList=essayMapper.getEssayByCollectionCondition(collection);
        for(Essay e:essayList){
            User u=getUserById(e.getUserId());
            e.setUserPic(u.getUserPic());
        }
        PageInfo pageInfo=new PageInfo(essayList);
        return pageInfo;
    }

    @Override
    public PageInfo getEssayByConditionAndPage(EssayParam essayParam, Integer pageNum, Integer pageSize) {
        if(pageNum==null)
            pageNum=1;
        if(pageSize==null||pageSize<=0)
            pageSize=defaultPageSize;
        PageHelper.startPage(pageNum,pageSize);
        List<Essay> essayList=essayMapper.getEssayByCondition(essayParam);
        for(Essay e:essayList){
            User u=getUserById(e.getUserId());
            e.setUserPic(u.getUserPic());
        }
        PageInfo pageInfo=new PageInfo(essayList);
        if(essayParam.getHot()){
            List<Essay> es=(List<Essay>)pageInfo.getList();
            for(Essay e:es){
                e.setHot(true);
            }
        }
        return pageInfo;
    }

    @Override
    public PageInfo getEssayByConcernAndPage(Integer userId, Integer pageNum) {
        return getEssayByUserListAndPage(svcUtils.getUserIdByConcern(userId),1,pageNum);
    }

    @Override
    public PageInfo getEssayByConcernAndPage(Integer userId, Integer pageNum, Integer pageSize) {
        List <Integer> list=svcUtils.getUserIdByConcern(userId);
        return getEssayByUserListAndPage(list,1,pageNum,pageSize);
    }

    @Override
    public PageInfo getEssayByUserListAndPage(List<Integer> list,Integer isPublished, Integer pageNum) {
        return getEssayByUserListAndPage(list,isPublished,pageNum,defaultPageSize);
    }


    @Override
    public PageInfo getEssayByUserListAndPage(List<Integer> list, Integer isPublished, Integer pageNum, Integer pageSize) {
        if(list==null){
            return null;
        }else if(list.size()==0){
            return new PageInfo(new ArrayList<Essay>());
        }
        EssayParam essayParam=new EssayParam();
        essayParam.setIsPublished(isPublished);
        essayParam.setUserIdList(list);
        return getEssayByConditionAndPage(essayParam,pageNum,pageSize);
    }

    @Override
    public Integer countEssayByCondition(Essay essay) {
        return essayMapper.countEssayByCondition(essay);
    }

    @Override
    public PageInfo getEssayByFuzzyConditionAndPage(Essay essay, Integer pageNum, Integer pageSize) {
        if(pageNum==null)
            pageNum=1;
        if(pageSize==null||pageSize<=0)
            pageSize=defaultPageSize;
        PageHelper.startPage(pageNum,pageSize);
        List<Essay> essayList=essayMapper.getEssayByFuzzyCondition(essay);
        for(Essay e:essayList){
            User u=getUserById(e.getUserId());
            e.setUserPic(u.getUserPic());
        }
        PageInfo pageInfo=new PageInfo(essayList);
        return pageInfo;
    }

    @Override
    public PageInfo getEssayByFuzzyConditionAndPage(Essay essay, Integer pageNum) {
        return getEssayByFuzzyConditionAndPage(essay,pageNum,defaultPageSize);
    }

    @Override
    public Essay getEssayByAccurateCondition(Essay essay) {
        List<Essay> result=essayMapper.getEssayByAccurateCondition(essay);
        for(Essay e:result){
            User u=getUserById(e.getUserId());
            e.setUserPic(u.getUserPic());
        }
        return svcUtils.judgeResultList(result);
    }

    @Override
    public Essay getEssayByEssayId(Integer id) {
        Essay p=new Essay();
        p.setEssayId(id);
        return getEssayByAccurateCondition(p);
    }

    @Override
    public Integer addEssay(User user, Essay essay) {
        if(user==null)
            return null;
        essay.setUserId(user.getUserId());
        essay.setUserNickname(user.getNickname());
        essay.setCreateTime(new Timestamp(System.currentTimeMillis()));
        if(essay.getRegionId()==null){
            essay.setRegionId(user.getRegionId());
        }
        essay.setRecommendNum(0);
        essay.setClickNum(0);
        essay.setCommentNum(0);
        Integer isPublished=essay.getIsPublished();
        if(isPublished!=null&&isPublished==1){
            essay.setPublishTime(new Timestamp(System.currentTimeMillis()));
        }else{
            essay.setIsPublished(0);
            essay.setPublishTime(null);
        }
        return essayMapper.addEssay(essay);
    }

    @Override
    public Integer updateEssayClickNum(ReturnMessage msg) {
        Essay pe=new Essay();
        pe.setEssayId(msg.getEssayId());
        pe.setClickNum(1);
        return updateEssay(pe);
    }

    @Override
    public Integer updateEssayCommentNum(ReturnMessage msg) {
        Essay pe=new Essay();
        pe.setEssayId(msg.getEssayId());
        pe.setCommentNum(1);
        return updateEssay(pe);
    }

    @Override
    public Integer updateEssayRecommendNum(ReturnMessage msg) {
        Essay pe=new Essay();
        pe.setEssayId(msg.getEssayId());
        pe.setRecommendNum(msg.getAddRecommendNum());
        return updateEssay(pe);
    }

    @Override
    public Integer updateEssaySelf(User user, Essay essay){
        if(essay.getUserId()==null){
            Essay e=getEssayByEssayId(essay.getEssayId());
            essay.setUserId(e.getUserId());
        }
        obstructUpdationOfEssaySelf(essay);
        if(user.getUserId()==essay.getUserId()){
            return updateEssay(essay);
        }
        return null;
    }

    @Override
    public Integer updateEssayByOthers(Essay essay){
        Essay e=new Essay();
        e.setEssayId(essay.getEssayId());
        if(essay.getRecommendNum()!=null&&essay.getRecommendNum()==1){
            e.setRecommendNum(essay.getRecommendNum());
        }
        if(essay.getCommentNum()!=null&&essay.getCommentNum()==1){
            e.setCommentNum(essay.getCommentNum());
        }
        if(essay.getClickNum()!=null&&essay.getClickNum()==1){
            e.setClickNum(essay.getClickNum());
        }
        return updateEssay(e);
    }

    public static void obstructUpdationOfEssaySelf(Essay essay) {
        essay.setRecommendNum(null);
        essay.setCommentNum(null);
        essay.setClickNum(null);
        essay.setCreateTime(null);
    }
    public static void obstructUpdationOfEssayByOthers(Essay essay) {
        essay.setEssayPic(null);
        essay.setIsPublished(null);
        essay.setEssayTitle(null);
        essay.setEssayContent(null);
        essay.setEssayType(null);
        essay.setCreateTime(null);
        essay.setUserNickname(null);
        essay.setTopicId(null);
    }
//num数量为变化量
    @Override
    public Integer updateEssay(Essay essay) {
        Essay e=getEssayByEssayId(essay.getEssayId());
        Integer isPublished=essay.getIsPublished();

        if(isPublished!=null&&isPublished==1&&(e.getIsPublished()==0)){
            essay.setPublishTime(new Timestamp(System.currentTimeMillis()));
        }
        //

        //

        //

        return essayMapper.updateEssay(essay);
    }

    //update essay nickname batch
    @Override
    public Integer updateEssayBatch(User user) {
        return essayMapper.updateEssayBatch(user);
    }

    @Override
    public Integer deleteEssayById(Integer id) {
        return essayMapper.deleteEssayById(id);
    }

    @Override
    public PageInfo getTopicByConditionAndPage(Topic topic, Integer pageNum, Integer pageSize) {
        if(pageNum==null)
            pageNum=1;
        if(pageSize==null||pageSize<=0)
            pageSize=defaultPageSize;
        PageHelper.startPage(pageNum,pageSize);
        List<Topic> list=topicMapper.getTopicByCondition(topic);
        PageInfo pageInfo=new PageInfo(list);
        return pageInfo;
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

    public EssayMapper getEssayMapper() {
        return essayMapper;
    }

    public void setEssayMapper(EssayMapper essayMapper) {
        this.essayMapper = essayMapper;
    }
}
