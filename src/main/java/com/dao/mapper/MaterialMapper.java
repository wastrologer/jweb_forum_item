package com.dao.mapper;


import com.pojo.Material;

import java.util.List;

public interface MaterialMapper{
    public List<Material> getMaterialByCondition(Material material);
    public Integer countMaterialByCondition(Material material);
    public Integer addMaterial(Material material);
    public Integer updateMaterial(Material material);
    public Integer deleteMaterialById(Integer id);
}