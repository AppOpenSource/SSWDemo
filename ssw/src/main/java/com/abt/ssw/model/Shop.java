package com.abt.ssw.model;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Shop implements Serializable{
	private int id;
	private String name;
	
	private List<Product> productList = new LinkedList<Product>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Product> getProductList() {
		return productList;
	}

	public void setProductList(List<Product> productList) {
		this.productList = productList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Shop(int id, String name, List<Product> productList) {
		super();
		this.id = id;
		this.name = name;
		this.productList = productList;
	}


	
	
}
