package com.controller;

import com.common.cache.CacheClient;
import com.common.entity.Constants;
import com.common.mq.MsgProducer;
import com.common.utils.StringUtil;
import com.common.utils.SvcUtils;
import com.constant.ErrorCode;
import com.github.pagehelper.PageInfo;
import com.pojo.Collection;
import com.pojo.*;
import com.service.ICollectionSvc;
import com.service.IEssaySvc;
import com.service.IUserSvc;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.*;

@Controller
@RequestMapping("/essay")
public class EssayController extends BaseController {
    @Resource
    private IUserSvc userSvcImpl;

    @Resource
    private IEssaySvc essaySvcImpl;
    @Resource
    private ICollectionSvc collectionSvcImpl;
    @Resource
    private CacheClient cacheClient;
    @Resource
    private SvcUtils svcUtils;
    @Resource
    private MsgProducer msgProducer;

    private int defaultSize=10;


    /*获得topics*/
    @RequestMapping("/customer/getTopics")
    @ResponseBody
    public Map<String, Object> getTopics(@RequestParam(value="num", required=false)Integer num,
                                                @RequestParam(value="size", required=false)Integer size){
        try {
            Topic pt=new Topic();
            pt.setIsShow(1);
            PageInfo<Topic> pageInfo=essaySvcImpl.getTopicByConditionAndPage(pt,num,size);
            List<Topic> topicList=pageInfo.getList();
            Essay pe=new Essay();
            for(Topic t:topicList){
                pe.setTopicId(t.getTopicId());
                int c=essaySvcImpl.countEssayByCondition(pe);
                t.setCommentNum(c);
            }
            Map<String, Object> map;
            map = getStrMap(pageInfo);
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }
    }

    /*add essay*/
    @RequestMapping("/customer/addEssay")
    @ResponseBody
    public Map<String, Object> addEssay(Essay essay){
        try {
            UserToken uk=getUserToken();
            if(uk!=null){
                User user= userSvcImpl.getUserById((int)uk.getUserId());
                if(user!=null){
                    if(essay.getEssayType()!=null&&essay.getEssayType()==Constants.CLUB_ESSAY_ID&&!user.getAuthId().equals(Constants.MANAGER_AUTH_ID))
                        return  getErrorMap(ErrorCode.USER_AUTH_ERROR,"没有权限");
                    Integer res=essaySvcImpl.addEssay(user,essay);
                    if(res==1){
                        return getSuccessMap();
                    }
                return  getErrorMap(ErrorCode.ESSAY_CREATE_ERROR,"文章创建失败");
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

    /*updateEssay*/
    @RequestMapping("/customer/updateEssay")
    @ResponseBody
    public Map<String, Object> updateEssay(Essay essay){
        try {
            UserToken uk=getUserToken();
            if(uk!=null){
                User user= userSvcImpl.getUserById((int)uk.getUserId());
                if(user!=null){
                    Integer res=essaySvcImpl.updateEssaySelf(user,essay);
                    if(res!=null&&res==1)
                        return  getSuccessMap();
                    else
                        return  getErrorMap(ErrorCode.ESSAY_UPDATE_ERROR,"文章更新失败");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }
        Map<String, Object> map =getUnlogedErrorMap();
        return map;
    }

    /*getCollectionEssay*/
    @RequestMapping("/customer/getCollectionEssay")
    @ResponseBody
    public Map<String, Object> getCollectionEssay(@RequestParam(value = "num", required = false) Integer num,
                                                @RequestParam(value = "size", required = false) Integer size){
        try {
            UserToken uk=getUserToken();
            if(uk!=null){
                User user= userSvcImpl.getUserById((int)uk.getUserId());
                if(user!=null){
                    com.pojo.Collection pc=new Collection();
                    pc.setUserId(user.getUserId());
                    pc.setIsPublished(1);
                    PageInfo pageInfo=essaySvcImpl.getEssayByCollectionAndPage(pc,num,size);
                    svcUtils.judgeEssayListRecommendedAndConcerned(user.getUserId(),pageInfo.getList());

                    Map<String, Object> map = getStrMap(pageInfo);
                    return map;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }
        Map<String, Object> map =getUnlogedErrorMap();
        return map;
    }

    /*getEssayByPerson  type1新鲜事2文章*/
    @RequestMapping("/customer/getEssayByPerson")
    @ResponseBody
    public Map<String, Object> getEssayByPerson(@RequestParam(value = "num", required = false) Integer num,
                                                @RequestParam(value = "size", required = false) Integer size,
                                                @RequestParam(value = "userId", required = false) Integer id,
                                                @RequestParam(value = "essayType", required = false) Integer essayType,
                                                @RequestParam(value = "isPublished", required = false) Integer isPublished){
        try {
            UserToken uk=getUserToken();
            if(uk!=null){
                User user= userSvcImpl.getUserById((int)uk.getUserId());
                if(user!=null){
                    EssayParam essayParam=new EssayParam();
                    List<Integer> ids=new ArrayList<>();
                    Integer published=null;
                    //是否本人
                    if(id!=null&&user.getUserId()!=(int)id){
                        published=1;
                    }else{
                        published=isPublished;
                    }
                    essayParam.setIsHiden(1);
                    if(id==null){
                        ids.add(user.getUserId());
                    }else{
                        ids.add(id);
                    }
                    essayParam.setUserIdList(ids);
                    essayParam.setIsPublished(published);
                    List<Integer>typeList =new ArrayList<>();
                    typeList.add(essayType);

                    if(essayType!=null){
                        typeList.add(essayType);
                    }else{
                        typeList.add(1);
                        typeList.add(2);
                        typeList.add(3);
                    }
                    essayParam.setEssayTypeList(typeList);
                    PageInfo pageInfo=essaySvcImpl.getEssayByConditionAndPage(essayParam,num,size);

                    svcUtils.judgeEssayListRecommendedAndConcerned(user.getUserId(),pageInfo.getList());
                    Map<String, Object> map = getStrMap(pageInfo);
                    return map;
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


    /*getEssayDetails*/
    @RequestMapping("/customer/getEssayDetails")
    @ResponseBody
    public Map<String, Object> getEssayDetails(@RequestParam(value="essayId", required=true)Integer essayId){
        try {
            UserToken uk=getUserToken();
            if(uk!=null){
                User user= userSvcImpl.getUserById((int)uk.getUserId());
                if(user!=null){
                    List<Integer> ids=new ArrayList<>();
                    Integer isPublished=null;
                    //是否本人
                    Essay essay=essaySvcImpl.getEssayByEssayId(essayId);

                    if(user.getUserId()!=(int)essay.getUserId()&&essay.getIsPublished()!=1){
                        return getErrorMap(ErrorCode.ESSAY_NOT_PUBLISHED,"文章不存在");
                    }
                    if(essay.getIsHiden()!=1){
                        return getErrorMap(ErrorCode.ESSAY_NOT_PUBLISHED,"文章被隐藏");
                    }
                    Essay pe=new Essay();
                    pe.setEssayId(essayId);
                    pe.setIsPublished(isPublished);
                    //essay=essaySvcImpl.getEssayByAccurateCondition(pe);
                    //clicknum+1,已发布
                    if(essay.getIsPublished()!=null&&essay.getIsPublished()==1){
                        pe=new Essay();
                        pe.setEssayId(essayId);
                        pe.setClickNum(1);
                        int i=essaySvcImpl.updateEssayByOthers(pe);
/*                        ReturnMessage msg=new ReturnMessage();
                        msg.setEssayId(essayId);
                        msg.setAddClickNum(1);
                        msgProducer.send(MsgCommandId.FORUM_ESSAY_CLICKNUM_ID,msg);*/
                        logger.info("updateEssayClickNum(pe)");
                    }
                    essay.setUserPic(user.getUserPic());
                    essay.setCollected(svcUtils.judgeCollected(user.getUserId(),essay.getEssayId()));
                    essay.setRecommended(svcUtils.judgeEssayRecommended(user.getUserId(),essay.getEssayId()));
                    essay.setConcerned(svcUtils.judgeConcerned(user.getUserId(),essay.getUserId()));
                    essay.setCollectNum(collectionSvcImpl.countCollectionByEssayId(essayId));
                    Map<String, Object> map = getStrMap("essay",essay);
                    return map;
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

    /*deleteEssay*/
    @RequestMapping("/customer/deleteEssay")
    @ResponseBody
    public Map<String, Object> deleteEssay(@RequestParam(value="essayId", required=true)Integer id){
        try {
            UserToken uk=getUserToken();
            if(uk!=null){
                User user= userSvcImpl.getUserById((int)uk.getUserId());
                Essay pe=new Essay();
                pe.setIsHiden(null);
                pe.setEssayId(id);
                Essay essay=essaySvcImpl.getEssayByAccurateCondition(pe);
                if(user!=null&&essay!=null&&user.getUserId()==(int)essay.getUserId()){
                    Integer res=essaySvcImpl.deleteEssayById(id);
                    if(res==1){
                        return getSuccessMap();
                    }
                }
                return  getErrorMap(ErrorCode.ESSAY_DELETE_ERROR,"文章删除失败（文章不存在/非本人操作）");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }
        Map<String, Object> map =getUnlogedErrorMap();
        return map;
    }

    /*获得关注essay,关注的人的essay*/
    @RequestMapping("/customer/getConcernEssays")
    @ResponseBody
    public Map<String, Object> getConcernEssays(@RequestParam(value="num", required=false)Integer num,
                                                @RequestParam(value="size", required=false)Integer size){
        try {
            UserToken uk=getUserToken();
            if(uk!=null){
                User user= userSvcImpl.getUserById((int)uk.getUserId());
                PageInfo<Essay> pageInfo=essaySvcImpl.getEssayByConcernAndPage(user.getUserId(),num,size);
                Map<String, Object> map;
                for(Essay e:pageInfo.getList()){
                    e.setRecommended(svcUtils.judgeEssayRecommended(user.getUserId(),e.getEssayId()));
                }
                map = getStrMap(pageInfo);
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }
        Map<String, Object> map = getUnlogedErrorMap();
        return map;
    }
    /*获得系统推荐文章，不包括新鲜事*/
    @RequestMapping("/getRecommendEssays")
    @ResponseBody
    public Map<String, Object> getRecommendEssays(@RequestParam(value="num", required=false)Integer num,
                                               @RequestParam(value="size", required=false)Integer size){
        try {
            if(num==null){
                num=1;
            }
            if(size==null){
                size=defaultSize;
            }
            EssayParam param=new EssayParam();
            param.setIsPublished(Constants.IS_PUBLISHED);
            param.setRecommendNumFrom(Constants.RECOMMEND_NUM_FROM);
            List<Integer> list = Constants.ARTICLE_ESSAY_TYPES;
            param.setEssayTypeList(list);
            param.setOrderBy(Constants.ORDER_BY_RECOMMEND_NUM);
            PageInfo pageInfoTop=essaySvcImpl.getEssayByConditionAndPage(param,1,1);
            //get null
            if(pageInfoTop.getList().size()==0)
                return getStrMap("list",null,"totalAccount",0);
            Essay top=(Essay) pageInfoTop.getList().get(0);
            int id=top.getEssayId();
            param.setOrderBy(null);
            param.setExceptionIdList(Arrays.asList(id));
            PageInfo pageInfo=essaySvcImpl.getEssayByConditionAndPage(param,num,size );
            List<Essay> hots=(List<Essay>)pageInfo.getList();
            ArrayList<Essay> essays=new ArrayList<>();
            if(num==1)
                essays.add(top);
            essays.addAll(hots);

            UserToken uk=getUserToken();
            Integer userId=null;
            if(uk!=null){
                userId=(int)uk.getUserId();
            }
            svcUtils.judgeEssayListRecommendedAndConcerned(userId,essays);
            return getStrMap(essays, (int) (pageInfo.getTotal()+pageInfoTop.getTotal()));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }
    }


    /*获取各类essay，
    type：0系统，1日报，2会所，3新人学堂，4新鲜事，*/
    @RequestMapping("/getOneTypeOfEssay")
    @ResponseBody
    public Map<String, Object> getOneTypeOfEssay(@RequestParam(value="topicId", required=false)Integer topicId,
                                                 @RequestParam(value="regionId", required=true)Integer regionId,
                                                 @RequestParam(value = "regionType", required = false) Integer regionType,
                                                 @RequestParam(value="num", required=false)Integer num,
                                           @RequestParam(value="size", required=false)Integer size,
                                           @RequestParam(value="essayType", required=true)Integer essayType){
        try {
            if(essayType==4)//news
                return  getNewsListByTypeAndPage( topicId,regionId,  regionType,num,size,Arrays.asList(essayType));
            else //article
                return  getArticleListByTypeAndPage( topicId,regionId,  regionType,num,size,Arrays.asList(essayType));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }
    }
    public Map<String, Object> getNewsListByTypeAndPage(Integer topicId,Integer regionId,  Integer regionType,
                                                        Integer num, Integer size, List<Integer> essayType){
        List<Integer> regionIdList;
        UserToken uk=getUserToken();
        Integer userId=null;
        if(uk!=null){
            userId=(int)uk.getUserId();
        }
        if(regionType!=null&&regionType==1){
            regionIdList= new ArrayList<Integer>();
            regionIdList.add(regionId);
        } else {
            regionIdList = svcUtils.getCityRegionListByRegionId(regionId);
        }
        if(num==null){
            num=1;
        }
        if(size==null){
            size=defaultSize;
        }
        EssayParam totalParam=svcUtils.getNormalEssayParam(regionIdList,essayType);
        if(topicId!=null){
            totalParam.setTopicId(topicId);
        }
        PageInfo<Essay> totalPageInfo=essaySvcImpl.getEssayByConditionAndPage(totalParam,num,size);
        //topic==null
        if(topicId==null){
            for(Essay e:totalPageInfo.getList()){
                e.setRecommended(svcUtils.judgeEssayRecommended(userId,e.getEssayId()));
                e.setConcerned(svcUtils.judgeConcerned(userId,e.getUserId()));
            }
            return getStrMap(totalPageInfo);
        }
        //topic news

        Integer total=(int)totalPageInfo.getTotal();
        //comment news
        EssayParam param=svcUtils.getNormalEssayParam(regionIdList,essayType);
        param.setOrderBy(Constants.ORDER_BY_COMMENT_NUM);
        param.setTopicId(topicId);
        PageInfo<Essay> pageInfoComment=essaySvcImpl.getEssayByConditionAndPage(param,1,3);

        //recommend news
        List<Integer> exceptionList=new ArrayList<>();
        for(Essay e:pageInfoComment.getList()){
            exceptionList.add(e.getEssayId());
        }
        param.setOrderBy(Constants.ORDER_BY_RECOMMEND_NUM);
        param.setExceptionIdList(exceptionList);
        PageInfo<Essay> pageInfoRecommend=essaySvcImpl.getEssayByConditionAndPage(param,1,3);
        //rest in first page
        for(Essay e:pageInfoRecommend.getList()){
            exceptionList.add(e.getEssayId());
        }
        param.setOrderBy(null);
        param.setExceptionIdList(exceptionList);
        PageInfo<Essay> pageInfoRest=null;
        if(size>6){
            pageInfoRest=  essaySvcImpl.getEssayByConditionAndPage(param,1,size-6);
            for(Essay e:pageInfoRest.getList()){
                exceptionList.add(e.getEssayId());
            }
        }
        List<Essay> returnEssayList=new ArrayList<>();
        if(num==1){
            returnEssayList.addAll(pageInfoComment.getList());
            returnEssayList.addAll(pageInfoRecommend.getList());
            if(pageInfoRest!=null){
                returnEssayList.addAll(pageInfoRest.getList());
            }
            for(Essay e:returnEssayList){
                e.setRecommended(svcUtils.judgeEssayRecommended(userId,e.getEssayId()));
                e.setConcerned(svcUtils.judgeConcerned(userId,e.getUserId()));
            }
            return getStrMap(returnEssayList,total);
        }else{
            param.setExceptionIdList(exceptionList);
            PageInfo<Essay> pageInfoAfterFirstPage=essaySvcImpl.getEssayByConditionAndPage(param,num-1,size);
            for(Essay e:pageInfoAfterFirstPage.getList()){
                e.setRecommended(svcUtils.judgeEssayRecommended(userId,e.getEssayId()));
                e.setConcerned(svcUtils.judgeConcerned(userId,e.getUserId()));
            }
            return getStrMap(pageInfoAfterFirstPage.getList(),total);
        }


    }

    public Map<String, Object> getArticleListByTypeAndPage(Integer topicId,Integer regionId,  Integer regionType,Integer num, Integer size, List<Integer> essayType){
        List<Integer> regionIdList;
        UserToken uk=getUserToken();
        Integer userId=null;
        if(uk!=null){
            userId=(int)uk.getUserId();
        }
        if(regionType!=null&&regionType==1){
            regionIdList= new ArrayList<Integer>();
            regionIdList.add(regionId);
        } else {
            regionIdList = svcUtils.getCityRegionListByRegionId(regionId);
        }
        if(num==null){
            num=1;
        }
        if(size==null){
            size=defaultSize;
        }
        EssayParam totalParam=svcUtils.getNormalEssayParam(regionIdList,essayType);
        if(topicId!=null){
            totalParam.setTopicId(topicId);
        }
        Integer total=(int)essaySvcImpl.getEssayByConditionAndPage(totalParam,null,null).getTotal();
        EssayParam param=svcUtils.getHotEssayParam(regionIdList,essayType);
        if(topicId!=null){
            param.setTopicId(topicId);
        }
        PageInfo pageInfoTop=essaySvcImpl.getEssayByConditionAndPage(param,1,1);
        //get null
        int id=0;
        Essay   top = null;
        if(pageInfoTop.getList().size()!=0){
            top=(Essay) pageInfoTop.getList().get(0);
            id=top.getEssayId();
        }
        param.setOrderBy(null);
        List<Integer> exceptionList=new ArrayList<>();
        exceptionList.add(id);
        param.setExceptionIdList(exceptionList);
        PageInfo pageInfoHot=essaySvcImpl.getEssayByConditionAndPage(param,num,size);
        List<Essay> hots=(List<Essay>)pageInfoHot.getList();
        ArrayList<Essay> essays=new ArrayList<>();
        //判断页码
        int count=pageInfoHot.getSize();
        Integer hotPage=count/size+1;
        int secondSize=size-count%size;
        if(num==1&&top!=null){
            essays.add(top);
        }
        List<Essay> allhots = null;
        Integer lastPageNum = null;
        if(num>=hotPage){
            PageInfo<Essay> pageInfoAllHot=essaySvcImpl.getEssayByConditionAndPage(param,1,999);
            allhots=pageInfoAllHot.getList();
        }
        if(num<hotPage){
            essays.addAll(hots);
            for(Essay e:essays){
                e.setRecommended(svcUtils.judgeEssayRecommended(userId,e.getEssayId()));
                if(userId!=null&&essayType.contains(4))
                    e.setConcerned(svcUtils.judgeConcerned(userId,e.getUserId()));
            }
            return getStrMap(essays,total);
        }else if(num==hotPage){
            if(secondSize<size){
                essays.addAll(hots);
            }
            for(Essay e:allhots){
                exceptionList.add(e.getEssayId());
            }
            //普通文章except hot
            param=new EssayParam();
            param.setIsPublished(1);
            param.setExceptionIdList(exceptionList);
            param.setRegionIdList(regionIdList);
            param.setEssayTypeList(essayType);
            if(topicId!=null){
                param.setTopicId(topicId);
            }
            PageInfo<Essay> pageInfo=essaySvcImpl.getEssayByConditionAndPage(param,1,secondSize);
            List<Essay> normalList=pageInfo.getList();
            essays.addAll(normalList);
            for(Essay e:essays){
                e.setRecommended(svcUtils.judgeEssayRecommended(userId,e.getEssayId()));
                if(userId!=null&&essayType.contains(4))
                    e.setConcerned(svcUtils.judgeConcerned(userId,e.getUserId()));
            }
            return getStrMap(essays,total);
        }else{
            //get normal after hotPage
            //普通文章all
            param=svcUtils.getNormalEssayParam(regionIdList,essayType);
            if(topicId!=null){
                param.setTopicId(topicId);
            }
            PageInfo pageInfoAll=essaySvcImpl.getEssayByConditionAndPage(param,1,size);
            lastPageNum =pageInfoAll.getPages();
            if(num>lastPageNum){
                return getErrorMap(ErrorCode.PAGE_NOT_EXIST,"已到最后一页");
            }
            for(Essay e:allhots){
                exceptionList.add(e.getEssayId());
            }
            param.setExceptionIdList(exceptionList);
            PageInfo<Essay> pageInfoNormalInHotPage=essaySvcImpl.getEssayByConditionAndPage(param,1,secondSize);
            List<Essay> normalListInHotPage=pageInfoNormalInHotPage.getList();
            for(Essay e:normalListInHotPage){
                exceptionList.add(e.getEssayId());
            }
            param.setExceptionIdList(exceptionList);
            PageInfo<Essay> pageInfoResult=essaySvcImpl.getEssayByConditionAndPage(param,num-hotPage,size);
            List<Essay> result=pageInfoResult.getList();
            for(Essay e:result){
                e.setRecommended(svcUtils.judgeEssayRecommended(userId,e.getEssayId()));
                if(userId!=null&&essayType.contains(4))
                    e.setConcerned(svcUtils.judgeConcerned(userId,e.getUserId()));
            }
            return getStrMap(result,total);
        }
    }

    /*makeMsgIsRead*/
    @RequestMapping("/customer/makeMsgIsRead")
    @ResponseBody
    public Map<String, Object> makeMsgIsRead(@RequestParam(value="essayId", required=true)Integer id){
        try {
            UserToken uk=getUserToken();
            if(uk!=null){
                User user= userSvcImpl.getUserById((int)uk.getUserId());
                Essay pe=new Essay();
                pe.setEssayId(id);
                Essay essay=essaySvcImpl.getEssayByAccurateCondition(pe);
                if(essay!=null){
                    //messaage under essay not read
                    List<Message> messageList=svcUtils.getMessageByUserAndEssay(user.getUserId(),essay.getEssayId(),Constants.MSG_IS_NOT_READ);
                    if(messageList.size()>0){
                        int num=svcUtils.makeMessageListIsRead(messageList);
                        if(num==messageList.size()) {
                            Integer type = essay.getEssayType();
                            svcUtils.updateMsgInCache(user, type, messageList, cacheClient);
                            return getSuccessMap();
                        }else {
                            Map<String, Object> map = getErrorMap(ErrorCode.MSG_DELETE_FAILED,"消息删除失败。删除数量为："+num);
                            return map;
                        }
                    }else {
                        Map<String, Object> map = getErrorMap(ErrorCode.MSG_NOT_EXIST, "消息不存在");
                        return map;
                    }
                }else {
                    Map<String, Object> map = getErrorMap(ErrorCode.ESSAY_NOT_EXIST,"文章不存在");
                    return map;
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

    /*judgeMsgExit*/
    @RequestMapping("/customer/judgeMsgExit")
    @ResponseBody
    public Map<String, Object> judgeMsgExit(@RequestParam(value="essayId", required=false)Integer essayId,
                                            @RequestParam(value="essayType", required=true)Integer essayType){
        try {
            UserToken uk=getUserToken();
            if(uk!=null){
                User user= userSvcImpl.getUserById((int)uk.getUserId());
                if(user!=null){
                    HashMap<String,Message> typeList=SvcUtils.getMsgByTypeFromCache(user.getUserId(),essayType,cacheClient);
                    int s;
                    String str;
                    if(essayId==null){
                        if(typeList==null){
                            s=0;
                            str="不存在通知";
                        }else{
                            s=1;
                            str="存在通知";
                        }
                        Map<String, Object> map = getStrMap(Constants.NOTICE_EXIST,s,Constants.NOTICE_STATUS,str);
                        return map;
                    }else {
                        if(typeList!=null) {
                            String key = user.getUserId()+"_"+essayId;
                            Message value = typeList.get(key);
                            if (value != null) {
                                s = 1;
                                str = "存在通知";
                                Map<String, Object> map = makeMsgIsRead(essayId);
                                map.put(Constants.NOTICE_EXIST,s);
                                map.put(Constants.NOTICE_STATUS,str);
                                return map;
                            }
                        }
                        s=0;
                        str="不存在通知";
                        Map<String, Object> map = getStrMap(Constants.NOTICE_EXIST,s,Constants.NOTICE_STATUS,str);
                        return map;
                    }
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


    @RequestMapping("/customer/recommendEssay")
    @ResponseBody
    public Map<String, Object> recommendEssay(
                                             @RequestParam(value="essayId", required=true)Integer essayId){
        try {
            UserToken uk=getUserToken();
            if(uk!=null){
                User user= userSvcImpl.getUserById((int)uk.getUserId());
                if(user!=null){
                    Integer updateNum=-2;
                    Integer addNum=-2;
                    Boolean recommended=svcUtils.judgeEssayRecommended(user.getUserId(),essayId);
                    if(!recommended){
                        addNum=svcUtils.addRecommend(user.getUserId(),essayId,null);
                        if (addNum==1) {
                            Essay pe=new Essay();
                            pe.setEssayId(essayId);
                            pe.setRecommendNum(1);
                            updateNum = essaySvcImpl.updateEssay(pe);
/*                            ReturnMessage msg=new ReturnMessage();
                            msg.setEssayId(essayId);
                            msg.setAddRecommendNum(1);
                            msgProducer.send(MsgCommandId.FORUM_ESSAY_RECOMMENDNUM_ID,msg);
                            updateNum = 1;*/
                        }
                    }else {
                        addNum=svcUtils.deleteRecommend(user.getUserId(),essayId,null);
                        if (addNum==1) {
                            Essay pe=new Essay();
                            pe.setEssayId(essayId);
                            pe.setRecommendNum(-1);
                            updateNum = essaySvcImpl.updateEssay(pe);
/*                            ReturnMessage msg=new ReturnMessage();
                            msg.setEssayId(essayId);
                            msg.setAddRecommendNum(-1);
                            msgProducer.send(MsgCommandId.FORUM_ESSAY_RECOMMENDNUM_ID,msg);
                            updateNum = 1;*/
                        }
                    }
                    logger.info("recommendEssay:\n-------------add----------:"+addNum+"\n---------update-------:"+updateNum);
                    if(addNum==1&&updateNum==1){
                        return getStrMap("recommended",!recommended);
                    }
                    return getErrorMap(ErrorCode.RECOMMEND_ERROR,"文章推荐操作失败");
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


    /*searchEssayByTitles*/
    @RequestMapping("/searchEssayByTitles")
    @ResponseBody
    public Map<String, Object> searchEssayByTitles(@RequestParam(value = "num", required = false) Integer num,
                                                   @RequestParam(value = "size", required = false) Integer size,
                                                   @RequestParam(value = "title", required = true) String title){
        try {
            EssayParam pe=new EssayParam();
            List<String> keywords=Arrays.asList(StringUtil.split(title," "));
            pe.setKeywords(keywords);
            //Integer isPublished=1;
            PageInfo pageInfo=essaySvcImpl.getEssayByConditionAndPage(pe,num,size);
            UserToken uk=getUserToken();
            Integer userId=null;
            if(uk!=null){
                userId=(int)uk.getUserId();
            }
            svcUtils.judgeEssayListRecommendedAndConcerned(userId,pageInfo.getList());

            Map<String, Object> map = getStrMap(pageInfo);
            return map;

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }
    }

    @RequestMapping("/customer/getTailedRecommendEssay")
    @ResponseBody
    public Map<String, Object> getTailedRecommendEssay(@RequestParam(value="pageSize", required=true)Integer pageSize,
                                                       @RequestParam(value="essayId", required=true)Integer essayId,
                                                       @RequestParam(value = "regionId", required = true) Integer regionId){
        try {
            UserToken uk=getUserToken();
            if(pageSize==null||pageSize<1){
                pageSize=3;
            }
            if(uk!=null){
                User user= userSvcImpl.getUserById((int)uk.getUserId());
                if(user!=null){
                    Essay essay=essaySvcImpl.getEssayByEssayId(essayId);
                    Integer essayUserId=essay.getUserId();
                    List<Integer> regionIdList= svcUtils.getCityRegionListByRegionId(regionId);
                    EssayParam param=svcUtils.getNormalEssayParam(regionIdList,null);
                    param.setOrderBy(null);
                    List<Integer> essayUserList=new ArrayList<>();
                    essayUserList.add(essayUserId);
                    param.setUserIdList(essayUserList);
                    List<Integer> exceptionList=new ArrayList<>();
                    exceptionList.add(essayId);
                    param.setExceptionIdList(exceptionList);
                    List<Integer> list=new ArrayList<>();
                    list.add(1);
                    list.add(2);
                    list.add(3);
                    param.setEssayTypeList(list);
                    PageInfo<Essay> pageInfo=essaySvcImpl.getEssayByConditionAndPage(param,null,pageSize);
                    List<Essay> essays=pageInfo.getList();
                    if(essays.size()!=pageSize){
                        param.setUserIdList(null);
                        PageInfo<Essay> addPageInfo=essaySvcImpl.getEssayByConditionAndPage(param,null,pageSize-essays.size());
                        List<Essay>  addEssays=addPageInfo.getList();
                        essays.addAll(addEssays);
                    }
                    return getStrMap("list",essays);
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


    /*addAccusation*/
    @RequestMapping("/customer/addAccusation")
    @ResponseBody
    public Map<String, Object> addAccusation(@RequestParam(value = "essayId", required = false) Integer essayId,
                                                   @RequestParam(value = "accusationContent", required = true) String accusationContent){
        try {
            UserToken uk=getUserToken();
            if(uk!=null){//
                int i=svcUtils.addAccusation((int)uk.getUserId(),essayId,accusationContent);
                if(i==1){
                    return  getSuccessMap();
                }else
                    return  getErrorMap("添加失败",null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }
        return getUnlogedErrorMap();
    }



    /*    *//*日报essay*//*
    @RequestMapping("/journal")
    @ResponseBody
    public Map<String, Object> getMyInfo2(@RequestParam(value="num", required=false)Integer num,
                                          @RequestParam(value="size", required=false)Integer size){
        try {
            return  getListByTypeAndPage(principal,num,size,Arrays.asList(1));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }

    }
    *//*会所essay*//*/
    @RequestMapping("club")
    @ResponseBody
    public Map<String, Object> getMyInf21o(@RequestParam(value="num", required=false)Integer num,
                                           @RequestParam(value="size", required=false)Integer size){
        try {
            return  getListByTypeAndPage(principal,num,size,Arrays.asList(2));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }
    }

    *//*新人学堂essay*//*
    @RequestMapping("/newcomerCircle")
    @ResponseBody
    public Map<String, Object> getMy32Info(@RequestParam(value="num", required=false)Integer num,
                                           @RequestParam(value="size", required=false)Integer size){
        try {
            return  getListByTypeAndPage(principal,num,size,Arrays.asList(3));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }
    }

    *//*新鲜事essay*//*
    @RequestMapping("/novelty")
    @ResponseBody
    public Map<String, Object> getMyInfo(@RequestParam(value="num", required=false)Integer num,
                                         @RequestParam(value="size", required=false)Integer size){
        try {
            return  getListByTypeAndPage(principal,num,size,Arrays.asList(0));
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> map = getErrorMap(e.getClass().getName());
            return map;
        }
    }*/

}
