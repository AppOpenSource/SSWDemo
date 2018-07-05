package com.sswdemo.beans;

public class MyInfoData {
	private String consignee;//收货人
	private String address;//地址
	private String tel;//电话
	private String mobile;//手机
	private String is_need_inv;//发票
	private String inv_payee;//单位名称
	private String shipping_id;//配送方式
	private String postscript;//订单附言
    private static MyInfoData uniqueInstance = null;
    public static boolean isfromnotifity = false;
    public static boolean isform_message = false;
    public static boolean isnot_login = false;
    private MyInfoData() {
       // Exists only to defeat instantiation.
    }
 
    public static MyInfoData getInstance() {
       if (uniqueInstance == null) {
           uniqueInstance = new MyInfoData();
       }
       return uniqueInstance;
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
	public String getIs_need_inv() {
		return is_need_inv;
	}
	public void setIs_need_inv(String is_need_inv) {
		this.is_need_inv = is_need_inv;
	}
	public String getInv_payee() {
		return inv_payee;
	}
	public void setInv_payee(String inv_payee) {
		this.inv_payee = inv_payee;
	}
	public String getShipping_id() {
		return shipping_id;
	}
	public void setShipping_id(String shipping_id) {
		this.shipping_id = shipping_id;
	}
	public String getPostscript() {
		return postscript;
	}
	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}
	

}
