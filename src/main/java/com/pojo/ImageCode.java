package com.pojo;

import java.io.Serializable;

public class ImageCode implements Serializable{
    
	private static final long serialVersionUID = -5940036315689733832L;

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
