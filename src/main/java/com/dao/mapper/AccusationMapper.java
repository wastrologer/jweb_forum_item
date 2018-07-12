package com.dao.mapper;

import com.pojo.Accusation;

import java.util.List;

public interface AccusationMapper {
    public List<Accusation> getAccusationByCondition(Accusation accusation);
    public Integer countAccusationByCondition(Accusation accusation);
    public Integer addAccusation(Accusation accusation);
    public Integer updateAccusation(Accusation accusation);
    public Integer deleteAccusationByCondition(Accusation accusation);
}
