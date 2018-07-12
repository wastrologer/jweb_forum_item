package com.common.utils;

import com.common.cache.CacheClient;
import com.common.entity.Constants;
import com.dao.mapper.*;
import com.pojo.*;
import com.pojo.Collection;
import net.rubyeye.xmemcached.GetsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

@Service
public class SvcUtils {

    protected static final Logger logger = LoggerFactory.getLogger(SvcUtils.class);

    @Resource
    ConcernMapper concernMapper;

    @Resource
    EssayMapper essayMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    RegionMapper regionMapper;

    @Resource
    MessageMapper messageMapper;

    @Resource
    RecommendMapper recommendMapper;

    @Resource
    AccusationMapper accusationMapper;

    @Resource
    CollectionMapper collectionMapper;

/*    @PostConstruct
    *//*在方法上加上注解@PostConstruct，这个方法就会在Bean初始化之后被Spring容器执行
    （注：Bean初始化包括，实例化Bean，并装配Bean的属性（依赖注入））。
        顺序上Constructor >> @Autowired >> @PostConstruct*//*
    public void init(){
        userMapper=userMapper1;
        concernMapper=concernMapper1;
        regionMapper=regionMapper1;
        messageMapper=messageMapper1;
    }*/

    public  <T> T judgeResultList(List<T> result){
        if(result.size()==1){
            return result.get(0);
        }else if(result.size()==0){
            return null;
        }else{
            throw new RuntimeException("查询到的对象数量为："+result.size()+".不合逻辑");
        }
    }

    public EssayParam getRecommendEssayParam(List<Integer> regionIdList, List<Integer> type){
        EssayParam param=new EssayParam();
        param.setIsPublished(1);
        param.setRegionIdList(regionIdList);
        param.setRecommendNumFrom(Constants.RECOMMEND_NUM_FROM);
        param.setEssayTypeList(type);
        //param.setIsHot(true);
        param.setPublishTimeFrom(DateUtil.getSpecifiedDay(-2));
        param.setPublishTimeTo(new Date());
        param.setOrderBy(Constants.ORDER_BY_RECOMMEND_NUM);
        return param;
    }

    public EssayParam getHotEssayParam(List<Integer> regionIdList, List<Integer> type){
        EssayParam param=new EssayParam();
        param.setIsPublished(1);
        param.setRegionIdList(regionIdList);
        param.setCommentNumFrom(Constants.HOT_COMMENT_NUM_FROM);
        param.setEssayTypeList(type);
        param.setIsHot(true);
        param.setPublishTimeFrom(DateUtil.getSpecifiedDay(-2));
        param.setPublishTimeTo(new Date());
        param.setOrderBy(Constants.ORDER_BY_COMMENT_NUM);
        return param;
    }

    public  EssayParam getNormalEssayParam(List<Integer> regionIdList,List<Integer> type){
        EssayParam param=new EssayParam();
        param.setIsPublished(1);
        param.setRegionIdList(regionIdList);
        param.setEssayTypeList(type);
        param.setOrderBy(null);
        return param;
    }

    public  List<Integer> getCityRegionListByRegionId(Integer regionId){
        if(regionId!=null){
            Region pr=new Region();
            pr.setRegionId(regionId);
            List<Region> rs=regionMapper.getRegionByCondition(pr);
            String city=rs.get(0).getCityName();
            pr=new Region();
            pr.setCityName(city);
            rs=regionMapper.getRegionByCondition(pr);
            List<Integer> list=new ArrayList<>();
            for(Region r:rs){
                list.add(r.getRegionId());
            }
            return  list;
        }else{
            throw new RuntimeException("regionId："+regionId+".不合逻辑");
        }

    }

    public  List<Integer> getRegionListByUserId(Integer id){
        User pu=new User();
        pu.setUserId(id);
        List<User> result=userMapper.getUserByAccurateCondition(pu);
        User u=judgeResultList(result);
        Integer regionId=null;
        if(u!=null)
            regionId= u.getRegionId();
        return  getCityRegionListByRegionId(regionId);

    }

    public Integer addRecommend(Integer userId,Integer essayId,Integer commentId){
        Recommend rc=new Recommend();
        rc.setCommentId(commentId);
        rc.setEssayId(essayId);
        rc.setUserId(userId);
        rc.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return  recommendMapper.addRecommend(rc);
    }

    public Integer addAccusation(Integer userId,Integer essayId,String accusationContent){
        Accusation accusation=new Accusation();
        accusation.setAccusationContent(accusationContent);
        accusation.setEssayId(essayId);
        accusation.setUserId(userId);
        accusation.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return  accusationMapper.addAccusation(accusation);
    }

    public Integer deleteRecommend(Integer userId,Integer essayId,Integer commentId){
        Recommend rc=new Recommend();
        rc.setCommentId(commentId);
        rc.setEssayId(essayId);
        rc.setUserId(userId);
        List<Recommend> rcs=recommendMapper.getRecommendByCondition(rc);
        if(rcs.size()!=1){
            return  null;
        }
        return  recommendMapper.deleteRecommendById(rcs.get(0).getRecommendId());
    }

    public Integer addConcern(Integer userFromId,Integer userToId){
        Concern c=new Concern();
        c.setUserFromId(userFromId);
        c.setUserToId(userToId);
        c.setCreateTime(new Timestamp(System.currentTimeMillis()));
        return  concernMapper.addConcern(c);
    }

    public Integer deleteConcern(Integer userFromId,Integer userToId){
        Concern c=new Concern();
        c.setUserFromId(userFromId);
        c.setUserToId(userToId);
        List<Concern> cs=concernMapper.getConcernByCondition(c);
        if(cs.size()!=1){
            return  cs.size();
        }
        return  concernMapper.deleteConcernById(cs.get(0).getConcernId());
    }

    public  List<Integer> getUserIdByConcern(Integer id){
        Concern c=new Concern();
        c.setUserFromId(id);
        List<Concern> cs=concernMapper.getConcernByCondition(c);
        List<Integer> ids=new ArrayList<Integer>();
        for (Concern con:cs){
            ids.add(con.getUserToId());
        }
        return ids;
    }
    public  Integer getIdByUserName(String s){
        User p=new User();
        p.setUserName(s);
        List<User> result=userMapper.getUserByAccurateCondition(p);
        User u=judgeResultList(result);
        if(u!=null){
            return result.get(0).getUserId();
        }else{
            return null;
        }
    }

    public  List<Message> getMessageByUserId(Integer id){
        Message pm=new Message();
        pm.setMessageUserId(id);
        List<Message> res=messageMapper.getMessageByCondition(pm);
        return res;
    }

    public  List<Message> getMessageByUserAndEssay(Integer userId,Integer essayId,Integer isRead){
        Message pm=new Message();
        pm.setMessageUserId(userId);
        pm.setMessageEssayId(essayId);
        pm.setIsRead(isRead);
        List<Message> res=messageMapper.getMessageByCondition(pm);
        return res;
    }

    public int makeMessageListIsRead(List<Message> list){
        int num=0;
        for(Message m:list){
            m.setIsRead(1);
            int i=messageMapper.updateMessage(m);
            if(i==1)
            num+=i;
        }
        return num;
    }

    public static HashMap<String,Message> getMapByMsgList(List<Message> list){
        HashMap<String,Message> map=new HashMap<>();
        for(Message m:list){
            String k= m.getMessageUserId()+"_"+m.getMessageEssayId();
            map.put(k,m);
        }
        return  map;
    }
    public static HashMap<String,Message> getNoticeMapByMsgList(List<Message> list){
        HashMap<String,Message> map=new HashMap<>();
        for(Message m:list){
            String k= m.getMessageUserId()+"_"+m.getMessageEssayId();
            map.put(k,m);
        }
        return  map;
    }

    public static void cacheMsgByUserAndType(User user, List<Message> list, CacheClient cacheClient){
        String newsKey=Constants.MC_NEWS_MSG_KEY_PREFIX+user.getUserId();
        String essayKey=Constants.MC_ESSAY_MSG_KEY_PREFIX+user.getUserId();
        String remindKey=Constants.MC_REMIND_MSG_KEY_PREFIX+user.getUserId();

        List<Message> newsList=new ArrayList<>();
        List<Message> essayList=new ArrayList<>();
        List<Message> remindList=new ArrayList<>();
        for(Message m:list){
            Integer i=m.getMessageType();
            if(i==0||i==5){
                remindList.add(m);
            }else if(i==1||i==2||i==3){
                essayList.add(m);
            }else if(i==4){
                newsList.add(m);
            }
        }
        HashMap<String,Message> newsMap=getMapByMsgList(newsList);

        HashMap<String,Message> essayMap=getMapByMsgList(essayList);

        HashMap<String,Message> remindMap=getMapByMsgList(remindList);

        if(newsList.size()>0){
            cacheClient.set(newsKey,Constants.REMIND_MSG_EXPIRE_SECOND,newsMap);
        }
        if(essayList.size()>0){
            cacheClient.set(essayKey,Constants.REMIND_MSG_EXPIRE_SECOND,essayMap);
        }
        if(remindMap.size()>0){
            cacheClient.set(remindKey,Constants.REMIND_MSG_EXPIRE_SECOND,remindMap);
        }
    }
    public static void updateMsgInCache(User user, Integer type,List<Message> messageList, CacheClient cacheClient) {

        Integer i = type;
        String remindKey = Constants.MC_MSG_KEY_PREFIX_MAP.get(i) + user.getUserId();
        //msg in cache
        GetsResponse<Object> objectGetsResponse = cacheClient.gets(remindKey);
        if (objectGetsResponse != null) {
            //all the msg of this type under the person
            HashMap<String, Message> remindMap = (HashMap<String, Message>) objectGetsResponse.getValue();
            //msg will be read(reduce from cache)
            for (Message m : messageList) {
                String k = m.getMessageUserId() + "_" + m.getMessageEssayId();
                if (remindMap.get(k) != null) {
                    remindMap.remove(k);
                }
            }
            if (remindMap.size() > 0) {
                cacheClient.set(remindKey, Constants.REMIND_MSG_EXPIRE_SECOND, remindMap);
            } else {
                cacheClient.delete(remindKey);
            }
        }
    }


    public static HashMap<String, Message> getMsgByTypeFromCache (Integer userId,Integer type,CacheClient cacheClient){
        HashMap<String, Message> res=null;
        Integer i = type;
        String remindKey = Constants.MC_MSG_KEY_PREFIX_MAP.get(i) + userId;
        GetsResponse<Object> objectGetsResponse = cacheClient.gets(remindKey);
        if (objectGetsResponse != null) {
            HashMap<String, Message> remindMap = (HashMap<String, Message>) objectGetsResponse.getValue();
            if (remindMap!=null&&remindMap.size() > 0) {
                return remindMap;
            }
        }
        return null;
    }

    public Boolean judgeCollected(Integer userId,Integer essayId){
        if(userId==null)
            return null;
        Collection pc=new Collection();
        pc.setUserId(userId);
        pc.setEssayId(essayId);
        Integer r=collectionMapper.countCollectionByCondition(pc);
        if(r!=null&&r>0){
            return true;
        }else{
            return false;
        }
    }

    public Boolean judgeConcerned(Integer userFromId,Integer userToId){
        if(userFromId==null)
            return null;
        Concern pc=new Concern();
        pc.setUserFromId(userFromId);
        pc.setUserToId(userToId);
        Integer r=concernMapper.countConcernByCondition(pc);
        if(r!=null&&r>0){
            return true;
        }else{
            return false;
        }
    }

    public Boolean judgeEssayRecommended(Integer userId, Integer essayId){
        if(userId==null)
            return null;
        Recommend pc=new Recommend();
        pc.setUserId(userId);
        pc.setEssayId(essayId);
        Integer r=recommendMapper.countRecommendByCondition(pc);
        if(r!=null&&r>0){
            return true;
        }else{
            return false;
        }
    }

    public void judgeEssayListConcerned(Integer userFromId,List<Essay> essayList){
        for(Essay e:essayList){
            e.setConcerned(judgeConcerned(userFromId,e.getUserId()));
        }
    }


    public void judgeEssayListRecommendedAndConcerned(Integer userId, List<Essay> essayList){
        for(Essay e:essayList){
            e.setConcerned(judgeConcerned(userId,e.getUserId()));

            e.setRecommended(judgeEssayRecommended(userId,e.getEssayId()));
        }
    }

    public void judgeEssayListRecommended(Integer userId, List<Essay> essayList){
        for(Essay e:essayList){
            e.setRecommended(judgeEssayRecommended(userId,e.getEssayId()));
        }
    }

    public Boolean judgeCommentRecommended(Integer userId,Integer commentId){
        if(userId==null)
            return null;
        Recommend pc=new Recommend();
        pc.setUserId(userId);
        pc.setCommentId(commentId);
        Integer r=recommendMapper.countRecommendByCondition(pc);
        if(r!=null&&r>0){
            return true;
        }else{
            return false;
        }
    }

    public Identification getIdentificationByUser(User user){
        int fans=0;
        int pic=0;
        int nickname=0;
        int signature=0;
        int essays=0;
        int isIdentified=0;


        Integer fansNum=user.getFans();
        String picStr=user.getUserPic();
        String nickNameStr=user.getNickname();
        String signatureStr =user.getSignature();
        Integer essaysNum;
        String authStr =user.getAuthId();

        String str=StringUtil.concealPhoneNumber(user.getPhoneNumber());

        EssayParam pe=new EssayParam();
        pe.setUserIdList(Arrays.asList(user.getUserId()));
        pe.setRecommendNumFrom(Constants.INDENTIFICATION_RECOMMEND_NUM);
        List list=essayMapper.getEssayByCondition(pe);
        essaysNum=list.size();

        if(fansNum!=null&&fansNum>=Constants.INDENTIFICATION_FANS_NUM){
            fans=1;
        }
        if(!StringUtil.isEmpty(picStr)){
            pic=1;
        }
        if(!StringUtil.isEmpty(nickNameStr)&&!nickNameStr.equals(str)){
            nickname=1;
        }
        if(!StringUtil.isEmpty(signatureStr)){
            signature=1;
        }
        if(essaysNum>=Constants.INDENTIFICATION_RECOMMEND_ESSAY_NUM){
            essays=1;
        }
        int needIdentify=fans*pic*nickname*signature*essays;
        if(authStr!=null&&authStr.equals("ROLE_IDENTIFICATION")){
            isIdentified=1;
        }else if(authStr!=null&&authStr.equals("ROLE_USER")&&needIdentify==1){
            User newUser=new User();
            newUser.setUserId(user.getUserId());
            newUser.setAuthId("ROLE_IDENTIFICATION");
            int updateUser=userMapper.updateUser(newUser);
            logger.info("updateUser   result:  "+updateUser);
        }

        return new Identification(pic,nickname,essays,signature,fans,isIdentified);

    }
}