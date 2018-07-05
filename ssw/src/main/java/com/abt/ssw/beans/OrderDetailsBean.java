package com.abt.ssw.beans;

import java.util.List;

public class OrderDetailsBean {
	/* "order_id": 107,
	    "order_sn": "20130822014052471",
	    "confirm_time": "1377150052",
	    "postscript": "快点送到",
	    "consignee": "李白",
	    "mobile": "20130822014052471",
	    "address": "北京市朝阳区xx小区**单元XX",
	    "order_total": "110.00",
	    "order_status": "3",*/
	/* "action_info": "已签收，签收人张三",
	    "logtime": "1379954707",
	    "action_user_id": "2",
	    "phone": "13787654321"*/
	  private int order_id; 
	  private String order_sn;
	  private String confirm_time;
	  private String postscript;
	  private String consignee;
	  private String mobile;
	  private String address;
	  private String order_total;
	  private String order_status;
	  private String action_info;
	  private String logtime;
	  private String action_user_id;
	  private String phone;
      public String getAction_info() {
		return action_info;
	}
	public void setAction_info(String action_info) {
		this.action_info = action_info;
	}
	public String getLogtime() {
		return logtime;
	}
	public void setLogtime(String logtime) {
		this.logtime = logtime;
	}
	public String getAction_user_id() {
		return action_user_id;
	}
	public void setAction_user_id(String action_user_id) {
		this.action_user_id = action_user_id;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	private List<OrderDetailsListsBean> goods;  //login内容对象
      
	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
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
	public String getPostscript() {
		return postscript;
	}
	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getOrder_total() {
		return order_total;
	}
	public void setOrder_total(String order_total) {
		this.order_total = order_total;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public List<OrderDetailsListsBean> getGoods() {
		return goods;
	}
	public void setGoods(List<OrderDetailsListsBean> goods) {
		this.goods = goods;
	}
	
	
}
