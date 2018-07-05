package com.abt.ssw.model;

import java.io.Serializable;

public class Product implements Serializable{
	private int productId;
	private String icon;
	private String title;
	private float price;
	private int count;
	private String description;
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public Product(int productId, String icon, String title, float price,
			int count, String description) {
		super();
		this.productId = productId;
		this.icon = icon;
		this.title = title;
		this.price = price;
		this.count = count;
		this.description = description;
	}
	
	
	

}
