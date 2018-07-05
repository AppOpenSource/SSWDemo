package com.abt.ssw.model;

import java.util.LinkedList;
import java.util.List;

import com.abt.ssw.activitys.R;

public class ShoppingCart {
	private boolean isTitle;
	private Shop shop;
	private Product product;
	private boolean isLastProductOfShop;
	private boolean isChecked;
	public boolean isTitle() {
		return isTitle;
	}

	public void setTitle(boolean isTitle) {
		this.isTitle = isTitle;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public boolean isLastProductOfShop() {
		return isLastProductOfShop;
	}

	public void setLastProductOfShop(boolean isLastProductOfShop) {
		this.isLastProductOfShop = isLastProductOfShop;
	}
	

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public ShoppingCart(boolean isTitle, Shop shop, Product product,
			boolean isLastProductOfShop) {
		super();
		this.isTitle = isTitle;
		this.shop = shop;
		this.product = product;
		this.isLastProductOfShop = isLastProductOfShop;
	}


	public static List<ShoppingCart> makeSampleList(List<Shop> shopList){
		List<ShoppingCart> shoppingCartList = new LinkedList<ShoppingCart>();
		for(Shop shop : shopList){
			ShoppingCart scShop = new ShoppingCart(true, shop, null, false);
			shoppingCartList.add(scShop);
			List<Product> productList = shop.getProductList();
			for(int i=0; i < productList.size(); i++){
				ShoppingCart scProduct = new ShoppingCart(false, shop, productList.get(i), i==productList.size()-1 ? true : false);
				shoppingCartList.add(scProduct);
			}
		}
		return shoppingCartList;
	}
	public static List<ShoppingCart> makeSampleList(){
		List<Shop> shopList = makeSample();
		List<ShoppingCart> shoppingCartList = new LinkedList<ShoppingCart>();
		for(Shop shop : shopList){
			ShoppingCart scShop = new ShoppingCart(true, shop, null, false);
			shoppingCartList.add(scShop);
			List<Product> productList = shop.getProductList();
			for(int i=0; i < productList.size(); i++){
				ShoppingCart scProduct = new ShoppingCart(false, shop, productList.get(i), i==productList.size()-1 ? true : false);
				shoppingCartList.add(scProduct);
			}
		}
		return shoppingCartList;
	}
	
	private static List<Shop> makeSample(){
		List<Shop> shopList = new LinkedList<Shop>();
		
//		//shop 1
//		List<Product> productList = new LinkedList<Product>();
//		Product p11 = new Product(1001,R.drawable.bacy, "太平鸟2012秋季新款   休闲裤九分裤直筒 82111226015", 139.00f, 1, "颜色: 黑色,尺码: XXL");
//		Product p12 = new Product(1002,R.drawable.bacy, "太平鸟2012新款  韩版休身小西服/休闲时装 80311226002", 209.00f, 1, "颜色: 绿灰格,尺码: S");
//		Product p13 = new Product(1003,R.drawable.bacy, "太平鸟2012新款 aaaa 311226002", 209.00f, 5, "颜色: 绿灰格,尺码: S");
//		Product p14 = new Product(1004,R.drawable.bacy, "太平鸟2012新款bbbbbbbbbb装 80311226002", 209.00f, 1, "颜色: 绿灰格,尺码: S");
//		productList.add(p11);
//		productList.add(p12);
//		productList.add(p13);
//		productList.add(p14);
//		Shop shop = new Shop(101,"物美大卖场", productList);
//		
//		//shop 2
//		List<Product> productList2 = new LinkedList<Product>();
//		Product p21 = new Product(1005,R.drawable.bacy, "康师傅冰红茶", 218.00f, 1, "容量: 300ml");
////		Product p22 = new Product(1006,R.drawable.bacy, "aaaaaaaaaaa外套修身女士大码短款韩版棉麻", 218.00f, 1, "颜色分类: 卡其,尺码: S");
//		productList2.add(p21);
////		productList2.add(p22);
//		Shop shop2 = new Shop(102,"恒源便利店", productList2);
//		
////		//shop 3
////		List<Product> productList3 = new LinkedList<Product>();
////		Product p31 = new Product(1007,R.drawable.bacy, "dfadfd", 218.00f, 1, "颜色分类: 卡其,尺码: S");
////		Product p32 = new Product(1008,R.drawable.bacy, "aaaafffffffdadffffff", 218.00f, 1, "颜色分类: 卡其,尺码: S");
////		Product p33 = new Product(1009,R.drawable.bacy, "aaaafffffffffwerwerffffffffffff", 218.00f, 1, "颜色分类: 卡其,尺码: S");
////		Product p34 = new Product(1010,R.drawable.bacy, "aaaaffffffferwerffffffffffffff", 218.00f, 1, "颜色分类: 卡其,尺码: S");
////		Product p35 = new Product(1011,R.drawable.bacy, "aaaafffffffffwerwqffffffffffff", 218.00f, 1, "颜色分类: 卡其,尺码: S");
////		Product p36 = new Product(1012,R.drawable.bacy, "aaaaffffffffferewffffffffffff", 218.00f, 1, "颜色分类: 卡其,尺码: S");
////		productList3.add(p31);
////		productList3.add(p32);
////		productList3.add(p33);
////		productList3.add(p34);
////		productList3.add(p35);
////		productList3.add(p36);
////		Shop shop3 = new Shop(103,"aaaaaaaaaa旗舰店", productList3);
//		
//		shopList.add(shop);
//		shopList.add(shop2);
//		shopList.add(shop3);
		return shopList;
	}
	
	public static int getShopCount(List<ShoppingCart> shoppingCartList){
		int sectionCount = 0;
		for(ShoppingCart sc : shoppingCartList){
			if(sc.isTitle()) sectionCount++;
		}
		return sectionCount;
	}
	
	public static ShoppingCart getShoppingCartParent(ShoppingCart shoppingCart,List<ShoppingCart> shoppingCartList){
		ShoppingCart shoppingCartTitle = null;
		for (ShoppingCart sc : shoppingCartList) {
			if (sc.isTitle()
					&& sc.getShop().getId() == shoppingCart.getShop().getId()) {
				shoppingCartTitle = sc;
			}
		}
		return shoppingCartTitle;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if(isTitle){
			return "shop: " + shop.getId();
		}else{
			return "product: " + product.getProductId();
		}
	}
}
