package com.sswdemo.model;

public class MoreListItem {

	private int logo;
	private String title;
	private int submenu;
	private String phoneNumber;
	
	public MoreListItem(int logo, String title,String phoneNumber, int submenu) {
		this.logo = logo;
		this.title = title;
		this.submenu = submenu;
		this.phoneNumber = phoneNumber;
	}
	
	public MoreListItem(int logo, String title, int submenu) {
		this.logo = logo;
		this.title = title;
		this.submenu = submenu;
		this.phoneNumber = "";
	}
	
	public int getLogo() {
		return logo;
	}
	public void setLogo(int logo) {
		this.logo = logo;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getSubmenu() {
		return submenu;
	}

	public void setSubmenu(int submenu) {
		this.submenu = submenu;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
}
