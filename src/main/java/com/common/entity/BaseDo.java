package com.common.entity;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author Admin
 *
 */
public class BaseDo implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4360974749630002468L;
	
	/**
	 * 分表的后缀
	 */
	private String _sebTableIndex;
    private int orderBy;// 排序字段
    private int orderType;// 升降序

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

	public String get_sebTableIndex() {
		return _sebTableIndex;
	}

	public void set_sebTableIndex(String _sebTableIndex) {
		this._sebTableIndex = _sebTableIndex;
	}

    public int getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(int orderBy) {
        this.orderBy = orderBy;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }
}
