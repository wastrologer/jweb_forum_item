package com.service;

import com.github.pagehelper.PageInfo;
import com.pojo.Message;

public interface IMessageSvc {
    public Integer countMessageByCondition(Message message);
    public Message getMessageByAccurateCondition(Message message);
    public Message getMessageById(Integer id) ;

    public PageInfo getMessageByConditionAndPage(Message message, Integer pageNum, Integer pageSize);
    public PageInfo getMessageByConditionAndPage(Message message, Integer pageNum);

    public Integer addMessageOnly(Integer userId, Integer senderUserId,Integer messageType,Integer messageEssayId,String messageContent)  ;
    public Integer makeMessageIsReadInDb(Integer messageId) ;

}
