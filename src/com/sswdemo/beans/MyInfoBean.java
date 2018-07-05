package com.sswdemo.beans;


public class MyInfoBean {
	/*"user_id": "3",
    "consignee": "李白",
    "address": "北京市朝阳区xx小区**单元XX",
    "tel": "1331901919",
    "mobile": "18688888778"*/
	private String user_id;  //结果状态常量
	private String consignee;
	private String address;
	private String tel;
	private String mobile;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


}
