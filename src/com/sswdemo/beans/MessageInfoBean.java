package com.sswdemo.beans;

import java.util.List;

public class MessageInfoBean {
	  private int res;  //结果状态常量
      private List<MessageDataInfoBean> data;  //login内容对象
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
	public List<MessageDataInfoBean> getData() {
		return data;
	}
	public void setData(List<MessageDataInfoBean> data) {
		this.data = data;
	}
	
	
	
}
