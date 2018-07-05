package com.sswdemo.beans;

import java.util.List;

public class DeliveryInfoBean {
	private String order_sn;
	private List<DeliveryInfoDataBean> data;
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public List<DeliveryInfoDataBean> getData() {
		return data;
	}
	public void setData(List<DeliveryInfoDataBean> data) {
		this.data = data;
	}
	
}
