package com.dao.mapper;

import com.pojo.Region;

import java.util.List;

public interface RegionMapper {
    public List<Region> getRegionByCondition(Region region);
    public Integer countRegionByCondition(Region region);
    public Integer addRegion(Region region);
    public Integer updateRegion(Region region);
    public Integer deleteRegionById(Integer id);
}
