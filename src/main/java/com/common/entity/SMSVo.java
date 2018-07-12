package com.common.entity;

import java.io.Serializable;

public class SMSVo implements Serializable{
    
	private static final long serialVersionUID = 4194684388158985973L;
	
	private String content;
    
	private Long timestamp;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
