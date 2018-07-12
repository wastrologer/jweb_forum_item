package com.service.impl;

import com.common.cache.CacheClient;
import com.common.utils.SvcUtils;
import com.dao.mapper.CoinMapper;
import com.dao.mapper.GoldMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pojo.Coin;
import com.pojo.Gold;
import com.pojo.User;
import com.service.IGoldAndCoinSvc;
import com.service.IUserSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class GoldAndCoinSvcImpl implements IGoldAndCoinSvc {
    private static final Logger logger = LoggerFactory.getLogger(GoldAndCoinSvcImpl.class);
    private Integer defaultPageSize=99;

    @Resource
    private CacheClient cacheClient;
    @Resource
    GoldMapper goldMapper;    
    @Resource
    CoinMapper coinMapper;
    @Resource
    SvcUtils svcUtils;

    @Resource
    IUserSvc userSvcImpl;

    @Override
    public Integer countCoinByCondition(Coin coin) {
        return coinMapper.countCoinByCondition(coin);
    }

    @Override
    public PageInfo getCoinByConditionAndPage(Coin coin, Integer pageNum, Integer pageSize) {
        if(pageNum==null)
            pageNum=1;
        if(pageSize==null||pageSize<=0)
            pageSize=defaultPageSize;
        PageHelper.startPage(pageNum,pageSize);
        List<Coin> coinList= coinMapper.getCoinByCondition(coin);
        PageInfo pageInfo=new PageInfo(coinList);
        return pageInfo;
    }

    @Override
    public PageInfo getCoinByConditionAndPage(Coin coin, Integer pageNum) {
        return getCoinByConditionAndPage(coin,pageNum,defaultPageSize);
    }
    @Transactional
    @Override
    public Integer addCoinWithUser(Integer userId, Integer coinNum,String reason) {
        User user=new User();
        user.setUserId(userId);
        user.setUserCoin(coinNum);
        Integer i=userSvcImpl.updateUserOnly(user);
        logger.info("userSvcImpl.updateUserOnly(user)结果为:"+i);

        if(i!=null&&i==1) 
            return addCoinOnly(userId,coinNum,reason);
        else 
            return null;
    }



    @Override
    public Integer addCoinOnly(Integer userId, Integer coinNum,String reason) {
        Coin coin=new Coin();
        coin.setCoinRecordTime(new Timestamp(System.currentTimeMillis()));
        coin.setCoinUserId(userId);
        coin.setCoinNum(coinNum);
        coin.setCoinReason(reason);
        coin.setCreateTime(new Timestamp(System.currentTimeMillis()));
        int cNum= coinMapper.addCoin(coin);
        logger.info("coinMapper.addCoin结果为"+cNum);
        return cNum;
    }



    @Override
    public Coin getCoinByAccurateCondition(Coin coin) {
        List<Coin> result= coinMapper.getCoinByCondition(coin);
        return svcUtils.judgeResultList(result);
    }

    @Override
    public Integer updateCoin(Coin coin) {
        return coinMapper.updateCoin(coin);
    }
    
    
    @Override
    public Integer countGoldByCondition(Gold gold) {
        return goldMapper.countGoldByCondition(gold);
    }

    @Override
    public PageInfo getGoldByConditionAndPage(Gold gold, Integer pageNum, Integer pageSize) {
        if(pageNum==null)
            pageNum=1;
        if(pageSize==null||pageSize<=0)
            pageSize=defaultPageSize;
        PageHelper.startPage(pageNum,pageSize);
        List<Gold> goldList= goldMapper.getGoldByCondition(gold);
        PageInfo pageInfo=new PageInfo(goldList);
        return pageInfo;
    }

    @Override
    public PageInfo getGoldByConditionAndPage(Gold gold, Integer pageNum) {
        return getGoldByConditionAndPage(gold,pageNum,defaultPageSize);
    }
    @Transactional
    @Override
    public Integer addGoldWithUser(Integer userId, Integer goldNum,String reason) {
        User user=new User();
        user.setUserId(userId);
        user.setUserGold(goldNum);
        Integer i=userSvcImpl.updateUserOnly(user);
        logger.info("userSvcImpl.updateUserOnly(user)结果为:"+i);

        if(i!=null&&i==1)
            return addGoldOnly(userId,goldNum,reason);
        else
            return null;
    }



    @Override
    public Integer addGoldOnly(Integer userId, Integer goldNum,String reason) {
        Gold gold=new Gold();
        gold.setGoldRecordTime(new Timestamp(System.currentTimeMillis()));
        gold.setCreateTime(new Timestamp(System.currentTimeMillis()));
        gold.setGoldUserId(userId);
        gold.setGoldNum(goldNum);
        gold.setGoldReason(reason);
        int cNum= goldMapper.addGold(gold);
        logger.info("goldMapper.addGold结果为"+cNum);
        return cNum;
    }



    @Override
    public Gold getGoldByAccurateCondition(Gold gold) {
        List<Gold> result= goldMapper.getGoldByCondition(gold);
        return svcUtils.judgeResultList(result);
    }

    @Override
    public Integer updateGold(Gold gold) {
        return goldMapper.updateGold(gold);
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

    public GoldMapper getGoldMapper() {
        return goldMapper;
    }

    public void setGoldMapper(GoldMapper goldMapper) {
        this.goldMapper = goldMapper;
    }
}
