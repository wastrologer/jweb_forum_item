package com.service;


import com.pojo.Material;

import java.util.List;

public interface IMaterialSvc {

	List<Material> getMaterial(String materialId) throws Exception;

	void insertMaterial(Material m) throws Exception;

}
