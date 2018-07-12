package com.dao.mapper;

import com.pojo.Message;

import java.util.List;

public interface MessageMapper {
    public List<Message> getMessageByCondition(Message message);
    public Integer countMessageByCondition(Message message);
    public Integer addMessage(Message message);
    public Integer updateMessage(Message message);
    public Integer deleteMessageById(Integer id);
}
