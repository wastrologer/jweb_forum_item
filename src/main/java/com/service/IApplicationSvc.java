package com.service;

import com.github.pagehelper.PageInfo;
import com.pojo.Application;
import com.pojo.Gold;

public interface IApplicationSvc {
    public Integer countApplicationByCondition(Application application);
    public Application getApplicationByAccurateCondition(Application application);
    public Application getApplicationById(Integer id) ;

    public PageInfo getApplicationByConditionAndPage(Application application, Integer pageNum, Integer pageSize);
    public PageInfo getApplicationByConditionAndPage(Application application, Integer pageNum);

    public Integer addApplicationOnly(Integer userId, Integer goldNum,String alipayAccount,String alipayName) ;
    public Integer addApplicationWithUser(Integer userId, Integer goldNum,String alipayAccount,String alipayName);
    public Integer updateApplicationOnly(Integer applicationId,Integer auditorId,Integer applyState,String reason) ;
    public Integer updateApplicationWithGoldAndUser(Integer applicationId,Integer auditorId,Integer applyState,String reason) ;
    
}
