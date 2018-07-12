package com.dao.mapper;

import com.pojo.Topic;

import java.util.List;

public interface TopicMapper {
    public List<Topic> getTopicByCondition(Topic topic);
    public Integer countTopicByCondition(Topic topic);
    public Integer addTopic(Topic topic);
    public Integer updateTopic(Topic topic);
    public Integer deleteTopicById(Integer id);
}
