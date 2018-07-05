package com.sswdemo.beans;

import java.util.List;

public class RecommendedInfoBean {
	  private int res;  //结果状态常量
      private List<RecommendedInfoDataBean> data;  //login内容对象
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
	public List<RecommendedInfoDataBean> getData() {
		return data;
	}
	public void setData(List<RecommendedInfoDataBean> data) {
		this.data = data;
	}
	
}
