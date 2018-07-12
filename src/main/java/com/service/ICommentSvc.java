package com.service;

import com.common.entity.ReturnMessage;
import com.github.pagehelper.PageInfo;
import com.pojo.Comment;
import com.pojo.User;

public interface ICommentSvc {
    public Integer countCommentByCondition(Comment comment);
    public Comment getCommentByAccurateCondition(Comment comment);
    public PageInfo getCommentByConditionAndPage(Comment comment, Integer pageNum, Integer pageSize);
    public PageInfo getSeniorCommentByEssayIdAndPage(Integer id, Integer pageNum, Integer pageSize);
    public PageInfo getJuniorCommentByCommentIdAndPage(Integer id, Integer pageNum, Integer pageSize);

    public PageInfo getCommentByConditionAndPage(Comment comment, Integer pageNum);
    public Integer addComment(User user, Comment comment);
    public Integer updateComment(User user,Comment comment);
    public Integer deleteCommentById(User user,Integer id);
    public Integer addComment(Comment comment);
    public Integer updateComment(Comment comment);
    public Integer updateCommentRecommendNum(ReturnMessage msg);

    public Integer updateCommentBatch(User user);
    public Integer deleteCommentById(Integer id);
}
