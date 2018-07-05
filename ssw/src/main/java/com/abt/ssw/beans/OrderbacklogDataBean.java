package com.abt.ssw.beans;

public class OrderbacklogDataBean {
	/* "order_id": "36",
     "order_sn": "201308050511456112",
     "confirm_time": "1375693905",
     "order_status": "2"*/
	 private String order_id;
	 private String order_sn;
	 private String confirm_time;
	 private String order_status;
	 private String price;
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getOrder_sn() {
		return order_sn;
	}
	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}
	public String getConfirm_time() {
		return confirm_time;
	}
	public void setConfirm_time(String confirm_time) {
		this.confirm_time = confirm_time;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	 
}
