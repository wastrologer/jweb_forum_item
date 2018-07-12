package com.dao.mapper;

import com.pojo.Comment;
import com.pojo.User;

import java.util.List;

public interface CommentMapper {
    public List<Comment> getCommentByCondition(Comment comment);
    public Integer countCommentByCondition(Comment comment);
    public Integer addComment(Comment comment);
    public Integer updateComment(Comment comment);
    public Integer updateCommentBatch(User user);
    public Integer deleteCommentById(Integer id);

}
