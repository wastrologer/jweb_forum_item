package com.dao.mapper;

import com.pojo.Coin;

import java.util.List;

public interface CoinMapper {
    public List<Coin> getCoinByCondition(Coin coin);
    public Integer countCoinByCondition(Coin coin);
    public Integer addCoin(Coin coin);
    public Integer updateCoin(Coin coin);
    public Integer deleteCoinById(Integer id);
}
