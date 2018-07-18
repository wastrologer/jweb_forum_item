package com.controller;

import com.common.cache.CacheClient;
import com.common.utils.SvcUtils;
import com.github.pagehelper.PageInfo;
import com.pojo.Coin;
import com.pojo.Gold;
import com.pojo.User;
import com.pojo.UserToken;
import com.service.IGoldAndCoinSvc;
import com.service.IUserSvc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/goldAndCoin")
public class GoldAndCoinController extends BaseController {
    @Resource
    private IUserSvc userSvcImpl;
    @Resource
    private SvcUtils svcUtils;
    @Resource
    private CacheClient cacheClient;
    @Resource
    private IGoldAndCoinSvc goldAndCoinSvcImpl;

    /*getGoldRecord*/
    @RequestMapping("/customer/getGoldRecord")
    @ResponseBody
    public Map<String, Object> getGoldRecord(@RequestParam(value="num", required=false)Integer num,
                                             @RequestParam(value="size", required=false)Integer size){
        try {
            UserToken uk=getUserToken();
            if(uk!=null){
                User user= userSvcImpl.getUserById((int)uk.getUserId());
                if(user!=null){
                    Gold pg=new Gold();
                    pg.setGoldUserId(user.getUserId());
                    PageInfo pageInfo=goldAndCoinSvcImpl.getGoldByConditionAndPage(pg,num,size);
                    return getStrMap(pageInfo.getList(), (int) pageInfo.getTotal(),"totalGold",user.getUserGold());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }
        Map<String, Object> map = getUnlogedErrorMap();
        return map;
    }

    /*getCoinRecord*/
    @RequestMapping("/customer/getCoinRecord")
    @ResponseBody
    public Map<String, Object> getCoinRecord(@RequestParam(value="num", required=false)Integer num,
                                             @RequestParam(value="size", required=false)Integer size){
        try {
            UserToken uk=getUserToken();
            if(uk!=null){
                User user= userSvcImpl.getUserById((int)uk.getUserId());
                if(user!=null){
                    Coin pc=new Coin();
                    pc.setCoinUserId(user.getUserId());
                    PageInfo pageInfo=goldAndCoinSvcImpl.getCoinByConditionAndPage(pc,num,size);
                    return getStrMap(pageInfo.getList(), (int) pageInfo.getTotal(),"totalCoin",user.getUserCoin());
                    //return getStrMap(pageInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }
        Map<String, Object> map = getUnlogedErrorMap();
        return map;
    }

    /*spendGold*/
    @RequestMapping("/customer/spendGold")
    @ResponseBody
    public Map<String, Object> spendGold(@RequestParam(value="goldNum", required=true)Integer goldNum,
                                         @RequestParam(value="reason", required=true)String reason){
        try {
            UserToken uk=getUserToken();
            if(uk!=null){
                User user= userSvcImpl.getUserById((int)uk.getUserId());
                if(user!=null){
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }
        Map<String, Object> map = getUnlogedErrorMap();
        return map;
    }



}
