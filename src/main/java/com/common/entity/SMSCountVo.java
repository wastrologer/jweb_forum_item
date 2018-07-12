package com.common.entity;

import java.io.Serializable;

/**
 * User: zzx Date: 2015/9/21 Time: 14:43
 */
public class SMSCountVo implements Serializable {

	private static final long serialVersionUID = 1023816313769394389L;

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
