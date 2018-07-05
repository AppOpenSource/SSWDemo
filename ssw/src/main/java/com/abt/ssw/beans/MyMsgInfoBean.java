package com.abt.ssw.beans;

import java.util.List;

public class MyMsgInfoBean {
	  private int res;  //结果状态常量
      private List<MyMsgInfoDataBean> data;  //login内容对象
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
	public List<MyMsgInfoDataBean> getData() {
		return data;
	}
	public void setData(List<MyMsgInfoDataBean> data) {
		this.data = data;
	}
	
	
}
