package com.pojo;

import java.io.Serializable;

public class ImageCodeCount implements Serializable {

	private static final long serialVersionUID = 8492314860157058746L;

	private Integer count;
	
	private Long timestamp;

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
}
