package com.sswdemo.beans;

import java.util.List;

public class ProductDetailsBean {
	  private int res;  //结果状态常量
      private List<ProductDetailsDataBean> data;  //login内容对象

	public int getRes() {
		return res;
	}

	public void setRes(int res) {
		this.res = res;
	}

	public List<ProductDetailsDataBean> getData() {
		return data;
	}

	public void setData(List<ProductDetailsDataBean> data) {
		this.data = data;
	}
	
}
