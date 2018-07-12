package com.dao.mapper;

import com.pojo.Concern;

import java.util.List;

public interface ConcernMapper {
    public List<Concern> getConcernByCondition(Concern concern);
    public Integer countConcernByCondition(Concern concern);
    public Integer addConcern(Concern concern);
    public Integer updateConcern(Concern concern);
    public Integer deleteConcernById(Integer Id);
}
