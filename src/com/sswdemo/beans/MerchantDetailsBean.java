package com.sswdemo.beans;

import java.util.List;

public class MerchantDetailsBean {
	  private int res;  //结果状态常量
      private List<MerchantDetailsDataBean> data;  
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
	public List<MerchantDetailsDataBean> getData() {
		return data;
	}
	public void setData(List<MerchantDetailsDataBean> data) {
		this.data = data;
	}
	

}
