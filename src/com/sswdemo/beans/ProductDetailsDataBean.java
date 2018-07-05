package com.sswdemo.beans;

public class ProductDetailsDataBean {
	private String res;			//
	private String msg;			//
	private String data;		//

	private String goods_id;	//商品id
	private String shopid;		//商家id
	private String shopname;	//商家name
	private String goodsname;	//商品name
	private String goodsprice;	//商品价格
	private String maketprice;	//商品原价
	private String sellnum;		//销量
	private String total;		//库存
	private String brand; 		//品牌
	private String desc; 		//商品描述
	private String goodsimage;
	private String collectgoods;
	
	public String getCollectgoods() {
		return collectgoods;
	}
	public void setCollectgoods(String collectgoods) {
		this.collectgoods = collectgoods;
	}
	public String getGoodsimage() {
		return goodsimage;
	}
	public void setGoodsimage(String goodsimage) {
		this.goodsimage = goodsimage;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}
	public String getShopid() {
		return shopid;
	}
	public void setShopid(String shopid) {
		this.shopid = shopid;
	}
	public String getShopname() {
		return shopname;
	}
	public void setShopname(String shopname) {
		this.shopname = shopname;
	}
	public String getGoodsname() {
		return goodsname;
	}
	public void setGoodsname(String goodsname) {
		this.goodsname = goodsname;
	}
	public String getGoodsprice() {
		return goodsprice;
	}
	public void setGoodsprice(String goodsprice) {
		this.goodsprice = goodsprice;
	}
	public String getMaketprice() {
		return maketprice;
	}
	public void setMaketprice(String maketprice) {
		this.maketprice = maketprice;
	}
	public String getSellnum() {
		return sellnum;
	}
	public void setSellnum(String sellnum) {
		this.sellnum = sellnum;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}

}
