package com.common.entity;

import java.io.Serializable;

public class ReturnMessage implements Serializable {

	private static final long serialVersionUID = 264196459059554546L;
	
	private Object object;
	
	private Integer essayId;
	
	private Integer userId;

	private String commentContent;

	private Integer commentId;

	private Integer addClickNum;

	private Integer addRecommendNum;

	private Integer addCommentNum;


	public Integer getAddClickNum() {
		return addClickNum;
	}

	public void setAddClickNum(Integer addClickNum) {
		this.addClickNum = addClickNum;
	}

	public Integer getAddRecommendNum() {
		return addRecommendNum;
	}

	public void setAddRecommendNum(Integer addRecommendNum) {
		this.addRecommendNum = addRecommendNum;
	}

	public Integer getAddCommentNum() {
		return addCommentNum;
	}

	public void setAddCommentNum(Integer addCommentNum) {
		this.addCommentNum = addCommentNum;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	public Integer getEssayId() {
		return essayId;
	}

	public void setEssayId(Integer essayId) {
		this.essayId = essayId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCommentContent() {
		return commentContent;
	}

	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}
}
