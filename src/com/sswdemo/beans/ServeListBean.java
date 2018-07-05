package com.sswdemo.beans;

import java.util.List;


public class ServeListBean {
	  private int res;  //结果状态常量
      private List<ServeListDataBean> data;  //login内容对象

	public int getRes() {
		return res;
	}

	public void setRes(int res) {
		this.res = res;
	}

	public List<ServeListDataBean> getData() {
		return data;
	}

	public void setData(List<ServeListDataBean> data) {
		this.data = data;
	}
	
}
