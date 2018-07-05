package com.sswdemo.model;


public class Order {
	private String sn;
	private float total;
	private String name;
	private long time;
	private String photo;
	
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public Order(String sn, float total, String name, long time, String photo) {
		super();
		this.sn = sn;
		this.total = total;
		this.name = name;
		this.time = time;
		this.photo = photo;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "sn: " + sn + "   name: " + name;
	}
}
