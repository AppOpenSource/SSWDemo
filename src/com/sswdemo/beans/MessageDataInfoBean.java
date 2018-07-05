package com.sswdemo.beans;

import java.util.List;

public class MessageDataInfoBean {
	/*"type": "1",
    "subtype": "0",
    "sendtype": "2",
    "usertype": "1",
    "sendid": "2",
    "content":["order_id":105,"content":"201308210543579428商户已接受"],
    "addtime": "1377079704"*/
	 private String type;
	 private String subtype;
	 private String sendtype;
	 private String usertype;
	 private String sendid;
	 private String addtime;
	 private List<MessageContentInfoBean> content;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubtype() {
		return subtype;
	}
	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}
	public String getSendtype() {
		return sendtype;
	}
	public void setSendtype(String sendtype) {
		this.sendtype = sendtype;
	}
	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}
	public String getSendid() {
		return sendid;
	}
	public void setSendid(String sendid) {
		this.sendid = sendid;
	}
	public String getAddtime() {
		return addtime;
	}
	public void setAddtime(String addtime) {
		this.addtime = addtime;
	}
	public List<MessageContentInfoBean> getContent() {
		return content;
	}
	public void setContent(List<MessageContentInfoBean> content) {
		this.content = content;
	}
	
}
