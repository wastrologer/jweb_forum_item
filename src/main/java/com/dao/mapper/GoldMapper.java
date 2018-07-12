package com.dao.mapper;

import com.pojo.Gold;

import java.util.List;

public interface GoldMapper {
    public List<Gold> getGoldByCondition(Gold gold);
    public Integer countGoldByCondition(Gold gold);
    public Integer addGold(Gold gold);
    public Integer updateGold(Gold gold);
    public Integer deleteGoldById(Integer id);
}
