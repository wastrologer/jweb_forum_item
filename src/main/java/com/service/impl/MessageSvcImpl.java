package com.service.impl;

import com.common.cache.CacheClient;
import com.common.entity.SentenceConstants;
import com.common.utils.SvcUtils;
import com.dao.mapper.MessageMapper;
import com.dao.mapper.GoldMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.pojo.Message;
import com.pojo.Gold;
import com.pojo.User;
import com.service.IMessageSvc;
import com.service.IUserSvc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class MessageSvcImpl implements IMessageSvc {
    private static final Logger logger = LoggerFactory.getLogger(MessageSvcImpl.class);
    private Integer defaultPageSize=99;

    @Resource
    private CacheClient cacheClient;

    @Resource
    MessageMapper messageMapper;
    @Resource
    SvcUtils svcUtils;

    @Resource
    IUserSvc userSvcImpl;

    @Override
    public Integer countMessageByCondition(Message message) {
        return messageMapper.countMessageByCondition(message);
    }

    @Override
    public PageInfo getMessageByConditionAndPage(Message message, Integer pageNum, Integer pageSize) {
        if(pageNum==null)
            pageNum=1;
        if(pageSize==null||pageSize<=0)
            pageSize=defaultPageSize;
        PageHelper.startPage(pageNum,pageSize);
        List<Message> messageList= messageMapper.getMessageByCondition(message);
        PageInfo pageInfo=new PageInfo(messageList);
        return pageInfo;
    }

    @Override
    public PageInfo getMessageByConditionAndPage(Message message, Integer pageNum) {
        return getMessageByConditionAndPage(message,pageNum,defaultPageSize);
    }



    @Override
    public Integer addMessageOnly(Integer userId, Integer senderUserId,Integer messageType,Integer messageEssayId,String messageContent) {
        Message message=new Message();
        message.setMessageUserId(userId);
        //message.(new Timestamp(System.currentTimeMillis()));
        message.setCreateTime(new Timestamp(System.currentTimeMillis()));
        message.setSenderUserId(senderUserId);
        message.setIsRead(0);
        message.setMessageContent(messageContent);
        message.setMessageType(messageType);
        message.setMessageEssayId(messageEssayId);

        int cNum= messageMapper.addMessage(message);
        logger.info("messageMapper.addMessage结果为:"+cNum);
        return cNum;
    }



    @Override
    public Message getMessageByAccurateCondition(Message message) {
        List<Message> result= messageMapper.getMessageByCondition(message);
        return svcUtils.judgeResultList(result);
    }
    @Override
    public Message getMessageById(Integer id) {
        Message pa=new Message();
        pa.setMessageId(id);
        List<Message> result= messageMapper.getMessageByCondition(pa);
        return svcUtils.judgeResultList(result);
    }

    @Override
    public Integer makeMessageIsReadInDb(Integer messageId) {
        Message message=new Message();
        message.setMessageId(messageId);
        message.setIsRead(1);

        return messageMapper.updateMessage(message);
    }
}
