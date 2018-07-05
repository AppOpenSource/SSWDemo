package com.abt.ssw.beans;

import java.util.List;

public class OrderbacklogBean {
	  private int res;  //结果状态常量
      private List<OrderbacklogDataBean> data;  //login内容对象
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
	public List<OrderbacklogDataBean> getData() {
		return data;
	}
	public void setData(List<OrderbacklogDataBean> data) {
		this.data = data;
	}
	
	
}
