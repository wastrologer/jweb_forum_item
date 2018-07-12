package com.service.impl;

import com.common.entity.ReturnMessage;
import com.common.utils.SortListUtil;
import com.common.utils.SvcUtils;
import com.common.utils.UserInfoUtil;
import com.dao.mapper.MessageMapper;
import com.dao.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.common.cache.CacheClient;
import com.common.entity.Constants;
import com.dao.mapper.CommentMapper;
import com.pojo.Comment;
import com.pojo.Essay;
import com.pojo.Message;
import com.pojo.User;
import com.service.ICommentSvc;
import com.service.IEssaySvc;
import net.rubyeye.xmemcached.GetsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
@Service
public class CommentSvcImpl implements ICommentSvc {
    protected static final Logger logger = LoggerFactory.getLogger(CommentSvcImpl.class);
    private Integer defaultPageSize=99;

    @Resource
    private CacheClient cacheClient;
    @Resource
    CommentMapper commentMapper;
    @Resource
    MessageMapper messageMapper;
    @Resource
    IEssaySvc essaySvcImpl;
    @Resource
    SvcUtils svcUtils;
    @Resource
    private UserMapper userMapper;

    public User getUserById(Integer id) {
        User user=new User();
        user.setUserId(id);
        return getUserByAccurateCondition(1,user);
    }

    public User getUserByAccurateCondition(Integer mine,User user) {
        List<User> result=userMapper.getUserByAccurateCondition(user);
        if(mine!=1){
            UserInfoUtil.concealUserInfo(result);
        }
        return svcUtils.judgeResultList(result);
    }


    @Override
    public Integer countCommentByCondition(Comment comment) {
        return commentMapper.countCommentByCondition(comment);
    }

    @Override
    public PageInfo getCommentByConditionAndPage(Comment comment, Integer pageNum, Integer pageSize) {
        if(pageNum==null)
            pageNum=1;
        if(pageSize==null||pageSize<=0)
            pageSize=defaultPageSize;
        PageHelper.startPage(pageNum,pageSize);
        List<Comment> commentList=commentMapper.getCommentByCondition(comment);
        for(Comment c:commentList){
            User u=getUserById(c.getUserId());
            c.setUserPic(u.getUserPic());
        }
        PageInfo pageInfo=new PageInfo(commentList);
        return pageInfo;
    }

    @Override
    public PageInfo getSeniorCommentByEssayIdAndPage(Integer id, Integer pageNum, Integer pageSize) {
        if(pageNum==null)
            pageNum=1;
        if(pageSize==null||pageSize<=0)
            pageSize=defaultPageSize;
        PageHelper.startPage(pageNum,pageSize);
        Comment comment=new Comment();
        comment.setEssayId(id);
        comment.setCommentGrade(1);
        List<Comment> commentList=commentMapper.getCommentByCondition(comment);
        for(Comment c:commentList){
            User u=getUserById(c.getUserId());
            c.setUserPic(u.getUserPic());
        }
        PageInfo pageInfo=new PageInfo(commentList);
        return pageInfo;
    }

    @Override
    public PageInfo getJuniorCommentByCommentIdAndPage(Integer id, Integer pageNum, Integer pageSize) {
        if(pageNum==null)
            pageNum=1;
        if(pageSize==null||pageSize<=0)
            pageSize=defaultPageSize;
        PageHelper.startPage(pageNum,pageSize);
        Comment comment=new Comment();
        comment.setSeniorCommentId(id);
        comment.setCommentGrade(2);
        List<Comment> commentList=commentMapper.getCommentByCondition(comment);
        for(Comment c:commentList){
            User u=getUserById(c.getUserId());
            c.setUserPic(u.getUserPic());
        }
        PageInfo pageInfo=new PageInfo(commentList);
        return pageInfo;
    }

    @Override
    public PageInfo getCommentByConditionAndPage(Comment comment, Integer pageNum) {
        return getCommentByConditionAndPage(comment,pageNum,defaultPageSize);
    }

    @Override
    public Integer addComment(User user, Comment comment) {
        if(user==null)
        return null;
        comment.setUserId(user.getUserId());
        comment.setUserNickname(user.getNickname());
        return addComment(comment);
    }

    @Override
    public Integer updateComment(User user, Comment comment) {
        if(user==null||user.getUserId()!=comment.getUserId())
            return null;
        return updateComment(comment);
    }

    @Override
    public Integer deleteCommentById(User user, Integer id) {
        if(user==null||user.getUserId()!=id)
            return null;
        return deleteCommentById(id);
    }

    @Override
    public Integer addComment(Comment comment) {
        if(comment.getSeniorCommentId()==null){
            comment.setCommentGrade(1);
        }else {
            comment.setCommentGrade(2);
        }
        //验证essay是否发布
        Integer essayId=comment.getEssayId();
        Essay essay=essaySvcImpl.getEssayByEssayId(essayId);
        if(essay.getIsPublished()!=1){
            return null;
        }
        comment.setTopicId(essay.getTopicId());
        comment.setCreateTime(new Timestamp(System.currentTimeMillis()));
        comment.setPublishTime(new Timestamp(System.currentTimeMillis()));
        int cNum=commentMapper.addComment(comment);
        logger.info("commentMapper.addComment结果为"+cNum);
        int mNum=-2;
        if(cNum==1) {
            mNum=addCommentMsg(comment);
        }
        return mNum;
    }

    public Integer addCommentMsg(Comment comment) {
        Essay pe=new Essay();
        pe.setEssayId(comment.getEssayId());
        Essay essay=essaySvcImpl.getEssayByAccurateCondition(pe);
        Message essayMsg=new Message();
        essayMsg.setCreateTime(new Timestamp(System.currentTimeMillis()));
        essayMsg.setIsRead(0);
        essayMsg.setMessageEssayId(comment.getEssayId());
        essayMsg.setMessageUserId(essay.getUserId());
        essayMsg.setSenderUserId(comment.getUserId());
        essayMsg.setMessageContent(comment.getCommentContent());
        essayMsg.setMessageType(essay.getEssayType());
        int eMsgNum = messageMapper.addMessage(essayMsg);
        logger.info("messageMapper.addMessage(essayMsg)----结果为----"+eMsgNum);
        if(eMsgNum==1){
            boolean eMsgCache=updateMsgInCache(essay,null,essayMsg);
            logger.info("updateMsgInCache(essay,essayMsg)----结果为-----"+eMsgCache);
            if(comment.getCommentGrade()==1)
                return eMsgNum;
        }

        int cMsgNum = -1;
        if(comment.getCommentGrade()==2) {
            Comment pc=new Comment();
            pc.setCommentId(comment.getSeniorCommentId());
            Comment seniorComment=getCommentByAccurateCondition(pc);
            Message commentMsg = new Message();
            commentMsg.setCreateTime(new Timestamp(System.currentTimeMillis()));
            commentMsg.setIsRead(0);
            commentMsg.setMessageEssayId(comment.getEssayId());
            commentMsg.setMessageUserId(seniorComment.getUserId());
            commentMsg.setSenderUserId(comment.getUserId());
            commentMsg.setMessageContent(comment.getCommentContent());
            commentMsg.setMessageType(3);
            cMsgNum = messageMapper.addMessage(commentMsg);
            logger.info("messageMapper.addMessage(commentMsg)----结果为----"+cMsgNum);
            if(cMsgNum==1){
                boolean cMsgCache=updateMsgInCache(essay,seniorComment,commentMsg);
                logger.info("updateMsgInCache(essay,commentMsg)----结果为----"+cMsgCache);
            }
        }
        return eMsgNum*cMsgNum;
    }

    public boolean updateMsgInCache(Essay essay,Comment seniorComment,Message essayMsg){
        String key=null;
        if(essayMsg.getMessageType()==5){
            key=Constants.MC_MSG_KEY_PREFIX_MAP.get(essayMsg.getMessageType())+seniorComment.getUserId();
        }else if(essayMsg.getMessageType()==1||essayMsg.getMessageType()==2||essayMsg.getMessageType()==3||essayMsg.getMessageType()==4){
            key=Constants.MC_MSG_KEY_PREFIX_MAP.get(essayMsg.getMessageType())+essay.getUserId();
        }
        GetsResponse<Object> getsResponse=cacheClient.gets(key);
        HashMap<String,Message> map=null;
        if(getsResponse!=null){
            map=(HashMap<String,Message>)getsResponse.getValue();
        }else{
            map=new HashMap<>();
        }
        String k=essay.getUserId()+"_"+essay.getEssayId();
        map.put(k,essayMsg);
        boolean res=cacheClient.set(key,Constants.REMIND_MSG_EXPIRE_SECOND,map);
        return res;
    }

    public static List<Comment> sortCommentsListByGrade(List<Comment> all){
        all= (List<Comment>) SortListUtil.sort(all,"publishTime",SortListUtil.ASC);
        List<Comment> senior=new ArrayList<>();
        List<Comment> junior=new ArrayList<>();
        for(Comment c:all){
            if(c.getCommentGrade()==1&&c.getSeniorCommentId()==null){
                senior.add(c);
            }else if(c.getCommentGrade()==2&&c.getSeniorCommentId()!=null){
                junior.add(c);
            }
        }
        for(Comment c:senior){
            Iterator<Comment> it=junior.iterator();
            Integer seniorId=c.getCommentId();
            while (it.hasNext()){
                Comment jc=it.next();
                if(jc.getSeniorCommentId()==seniorId){
                    if(c.getJuniorComments()==null){
                        c.setJuniorComments(new ArrayList<Comment>());
                    }
                    c.getJuniorComments().add(jc);
                    it.remove();
                }
            }
        }
        return  senior;
    }

    @Override
    public Comment getCommentByAccurateCondition(Comment comment) {
        List<Comment> result=commentMapper.getCommentByCondition(comment);
        for(Comment c:result){
            User u=getUserById(c.getUserId());
            c.setUserPic(u.getUserPic());
        }
        return svcUtils.judgeResultList(result);

    }

    @Override
    public Integer updateComment(Comment comment) {
        return commentMapper.updateComment(comment);
    }

    @Override
    public Integer updateCommentRecommendNum(ReturnMessage msg) {
        Comment pc=new Comment();
        pc.setCommentId(msg.getCommentId());
        pc.setRecommendNum(msg.getAddRecommendNum());
        return updateComment(pc);
    }

    @Override
    public Integer updateCommentBatch(User user) {
        return commentMapper.updateCommentBatch(user);
    }

    @Override
    public Integer deleteCommentById(Integer id) {
        return commentMapper.deleteCommentById(id);
    }

    public Integer getDefaultPageSize() {
        return defaultPageSize;
    }

    public void setDefaultPageSize(Integer defaultPageSize) {
        this.defaultPageSize = defaultPageSize;
    }

    public CacheClient getCacheClient() {
        return cacheClient;
    }

    public void setCacheClient(CacheClient cacheClient) {
        this.cacheClient = cacheClient;
    }

    public CommentMapper getCommentMapper() {
        return commentMapper;
    }

    public void setCommentMapper(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }
}
