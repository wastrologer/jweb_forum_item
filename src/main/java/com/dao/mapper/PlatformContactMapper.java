package com.dao.mapper;


import com.pojo.PlatformContact;

import java.util.List;

public interface PlatformContactMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PlatformContact record);

    int insertSelective(PlatformContact record);

    PlatformContact selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PlatformContact record);

    int updateByPrimaryKey(PlatformContact record);

    public List<PlatformContact> selectAll();

}