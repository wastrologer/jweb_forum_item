package com.service;

import com.github.pagehelper.PageInfo;
import com.pojo.Coin;
import com.pojo.Gold;
import com.pojo.User;

public interface IGoldAndCoinSvc {
    public Integer countCoinByCondition(Coin coin);
    public Coin getCoinByAccurateCondition(Coin coin);
    public PageInfo getCoinByConditionAndPage(Coin coin, Integer pageNum, Integer pageSize);
    public PageInfo getCoinByConditionAndPage(Coin coin, Integer pageNum);

    public Integer addCoinOnly(Integer userId, Integer coinNum,String reason);
    public Integer addCoinWithUser(Integer userId, Integer coinNum,String reason);
    public Integer updateCoin(Coin coin);

    public Integer countGoldByCondition(Gold gold);
    public Gold getGoldByAccurateCondition(Gold gold);
    public PageInfo getGoldByConditionAndPage(Gold gold, Integer pageNum, Integer pageSize);
    public PageInfo getGoldByConditionAndPage(Gold gold, Integer pageNum);

    public Integer addGoldOnly(Integer userId, Integer goldNum,String reason);
    public Integer addGoldWithUser(Integer userId, Integer goldNum,String reason);

    public Integer updateGold(Gold gold);
}
