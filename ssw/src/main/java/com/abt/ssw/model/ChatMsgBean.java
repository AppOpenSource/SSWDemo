package com.abt.ssw.model;

import java.util.List;

public class ChatMsgBean {

	private int res;  //结果状态常量
	private List<ChatMsgBean> data;  //login内容对象
	
	public int getRes() {
		return res;
	}

	public void setRes(int res) {
		this.res = res;
	}

	public List<ChatMsgBean> getData() {
		return data;
	}

	public void setData(List<ChatMsgBean> data) {
		this.data = data;
	}

	public ChatMsgBean() {
		super();
	}

	public ChatMsgBean(int res, List<ChatMsgBean> data) {
		super();
		this.res = res;
		this.data = data;
	}

	@Override
	public String toString() {
		return "ServeListBean [res=" + res + ", data=" + data + "]";
	}
	
}
