package com.abt.ssw.utils;


import java.util.ArrayList;
import java.util.List;

import android.test.AndroidTestCase;

import com.abt.ssw.model.Order;
import com.abt.ssw.model.Product;
import com.abt.ssw.model.Shop;
import com.abt.ssw.model.ShoppingCart;
import com.abt.ssw.servcie.GetOrderHttpService;
import com.abt.ssw.servcie.ShoppingCartDBService;
//import com.promfy.util.WifiAdmin;

public class TestUtil extends AndroidTestCase{
	private static final String TAG = "test";
	
	public void testUtil(){
		ShoppingCartDBService scds = new ShoppingCartDBService(getContext());
		for(int i=0; i<10; i++){
			try {
				scds.insert(111, "aaa", 1111 + i, "http://img", "bbb", 123.6f, "bbb", 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		for(int i=0; i<10; i++){
			try {
				scds.insert(222, "aaa", 2222 + i, "http://img", "bbb", 123.6f, "bbb", 1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void testGetAll(){
		ShoppingCartDBService scds = new ShoppingCartDBService(getContext());
		try {
			List<ShoppingCart> scList = scds.getAll();
			for(ShoppingCart sc: scList){
				L.d(TAG, "shoppingCart: " + sc.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void testDel(){
		ShoppingCartDBService scds = new ShoppingCartDBService(getContext());
		List<Shop> shops = new ArrayList<Shop>();
		List<Product> pList = new ArrayList<Product>();
		for(int i = 0; i< 5; i++){
			Product p = new Product(1116+i, "", "",3f, 4, "");
			pList.add(p);
		}
		Shop shop = new Shop(111, "aaa", pList);
		shops.add(shop);
		try {
			scds.deleteByOrder(shops);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void test(){
		GetOrderHttpService gohs = new GetOrderHttpService();
		List<Order> orders = gohs.getOrders();
		for(Order order : orders){
			L.d(TAG, "order: " + order.toString());
		}
//		L.d(TAG, "time: " + System.currentTimeMillis());
	}
}
