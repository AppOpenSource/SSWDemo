package com.abt.ssw.beans;

import java.util.List;

public class OrderBean {
	  private int res;  //结果状态常量
      private List<GoodListsDataBean> data;  //login内容对象
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
	public List<GoodListsDataBean> getData() {
		return data;
	}
	public void setData(List<GoodListsDataBean> data) {
		this.data = data;
	}
	
}
