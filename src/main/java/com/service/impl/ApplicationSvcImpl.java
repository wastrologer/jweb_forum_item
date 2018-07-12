package com.service.impl;

import com.common.cache.CacheClient;
import com.common.entity.SentenceConstants;
import com.common.utils.SvcUtils;
import com.dao.mapper.ApplicationMapper;
import com.dao.mapper.GoldMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pojo.Application;
import com.pojo.Gold;
import com.pojo.User;
import com.service.IApplicationSvc;
import com.service.IUserSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class ApplicationSvcImpl implements IApplicationSvc {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationSvcImpl.class);
    private Integer defaultPageSize=99;

    @Resource
    private CacheClient cacheClient;
    @Resource
    GoldMapper goldMapper;    
    @Resource
    ApplicationMapper applicationMapper;
    @Resource
    SvcUtils svcUtils;

    @Resource
    IUserSvc userSvcImpl;

    @Override
    public Integer countApplicationByCondition(Application application) {
        return applicationMapper.countApplicationByCondition(application);
    }

    @Override
    public PageInfo getApplicationByConditionAndPage(Application application, Integer pageNum, Integer pageSize) {
        if(pageNum==null)
            pageNum=1;
        if(pageSize==null||pageSize<=0)
            pageSize=defaultPageSize;
        PageHelper.startPage(pageNum,pageSize);
        List<Application> applicationList= applicationMapper.getApplicationByCondition(application);
        PageInfo pageInfo=new PageInfo(applicationList);
        return pageInfo;
    }

    @Override
    public PageInfo getApplicationByConditionAndPage(Application application, Integer pageNum) {
        return getApplicationByConditionAndPage(application,pageNum,defaultPageSize);
    }
    @Transactional
    @Override
    public Integer addApplicationWithUser(Integer userId, Integer goldNum,String alipayAccount,String alipayName)  {
        User user=new User();
        user.setUserId(userId);
        user.setUserGold(-goldNum);
        Integer i=userSvcImpl.updateUserOnly(user);
        logger.info("userSvcImpl.updateUserOnly(user)结果为:"+i);

        if(i!=null&&i==1) 
            return addApplicationOnly(userId,goldNum,alipayAccount,alipayName);
        else 
            return null;
    }



    @Override
    public Integer addApplicationOnly(Integer userId, Integer goldNum,String alipayAccount,String alipayName) {
        Application application=new Application();
        application.setUserId(userId);
        application.setApplicationTime(new Timestamp(System.currentTimeMillis()));
        application.setGoldNum(goldNum);
        application.setApplyState(0);
        application.setAlipayAccount(alipayAccount);
        application.setAlipayName(alipayName);
        application.setCreateTime(new Timestamp(System.currentTimeMillis()));

        int cNum= applicationMapper.addApplication(application);
        logger.info("applicationMapper.addApplication结果为:"+cNum);
        return cNum;
    }



    @Override
    public Application getApplicationByAccurateCondition(Application application) {
        List<Application> result= applicationMapper.getApplicationByCondition(application);
        return svcUtils.judgeResultList(result);
    }
    @Override
    public Application getApplicationById(Integer id) {
        Application pa=new Application();
        pa.setApplicationId(id);
        List<Application> result= applicationMapper.getApplicationByCondition(pa);
        return svcUtils.judgeResultList(result);
    }

    @Override
    public Integer updateApplicationOnly(Integer applicationId,Integer auditorId,Integer applyState ,String reason) {
        Application application=new Application();
        application.setApplicationId(applicationId);
        application.setAuditorId(auditorId);
        application.setAuditTime(new Timestamp(System.currentTimeMillis()));
        application.setApplyState(applyState);
        application.setRefuseReason(reason);
        return applicationMapper.updateApplication(application);
    }
    @Transactional
    @Override
    public Integer updateApplicationWithGoldAndUser(Integer applicationId,Integer auditorId,Integer applyState,String reason) {
        if(applyState!=null){
            Application application=getApplicationById(applicationId);
            int result=-1;
            if(applyState==1){
                Gold gold=new Gold();
                gold.setGoldRecordTime(new Timestamp(System.currentTimeMillis()));
                gold.setGoldUserId(application.getUserId());
                gold.setGoldNum(-application.getGoldNum());
                gold.setGoldReason(SentenceConstants.GOLD_REDUCE_REASON_CASH);
                result= goldMapper.addGold(gold);
                logger.info("goldMapper.addGold结果为"+result);
            }else if(applyState==-1){
                User user=new User();
                user.setUserId(application.getUserId());
                user.setUserGold(application.getGoldNum());
                result=userSvcImpl.updateUserOnly(user);
                logger.info("userSvcImpl.updateUserOnly(user)结果为:"+result);
            }
            if(result==1) {
                return updateApplicationOnly(applicationId,auditorId,applyState,reason);
            }
        }
        return null;
    }
}
