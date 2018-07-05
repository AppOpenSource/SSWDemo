package com.sswdemo.beans;

import java.util.List;

public class MessageContentInfoBean {
	  private String order_id;  //结果状态常量
	  private String notice_id; //通知的id
	  private String shopmsg_id;
	  
      public String getShopmsg_id() {
		return shopmsg_id;
	}
	public void setShopmsg_id(String shopmsg_id) {
		this.shopmsg_id = shopmsg_id;
	}
	public String getNotice_id() {
		return notice_id;
	}
	public void setNotice_id(String notice_id) {
		this.notice_id = notice_id;
	}
	private String content;  //login内容对象
      
      
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
