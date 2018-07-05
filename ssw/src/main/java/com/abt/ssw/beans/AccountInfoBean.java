package com.abt.ssw.beans;

import java.util.List;

public class AccountInfoBean {
	  private int res;  //结果状态常量
      private List<AccountInfoDataBean> data;  //login内容对象
	public int getRes() {
		return res;
	}
	public void setRes(int res) {
		this.res = res;
	}
	public List<AccountInfoDataBean> getData() {
		return data;
	}
	public void setData(List<AccountInfoDataBean> data) {
		this.data = data;
	}
	
	
}
