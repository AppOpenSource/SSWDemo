package com.sswdemo.base;

import android.os.Environment;




public class AppParameters {
	
	private static AppParameters mInstance = null;
	
	public static AppParameters getInstance() {
		if (mInstance == null) {
			mInstance = new AppParameters();
		}
		return mInstance;
	}
	public static String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
	public static final String baseDir = SDCardRoot + "/data/sheshang";
	public static String userId = ""; // 用户ID
	public String webHost = "http://121.199.13.117/beta/index.php";

	public String baseURL() {
		return webHost + "/";
	}
	/**获取商家列表*/
	public String url_getLogin() {
		return baseURL() + "PhoneLogin/Login";
	}
	/**获取商家列表*/
	public String url_getServeList() {
		return baseURL() + "ShopAndroid/ShopType";
	}
	/**获取关注商家列表*/
	public String url_getFocusServeList() {
		return baseURL() + "ShopAndroid/MyAttention";
	}
	/**获取商家详情*/
	public String url_getMerchantDetails() {
		return baseURL() + "ShopAndroid/ShopDetail";
	}
	/**获取商家详情*/
	public String url_getBusinessConcern() {
		return baseURL() + "ShopAndroid/CollectShop";
	}
	/**获取商品列表*/
	public String url_getGoodLists() {
		return baseURL() + "ShopAndroid/GoodsList";
	}
	/**订单详情*/
	public String url_getOrderDetailsLists() {
		return baseURL() + "ShopAndroid/OrderDetail";
	}
	/**订单详情*/
	public String url_getDeliveryInfo() {
		return baseURL() + "ShopAndroid/Actionlog";
	}
	/**获未完成订单列表*/
	public String url_getOrderbacklog() {
		return baseURL() + "ShopAndroid/MyUndoneOrder";
	}
	/**获历史订单列表*/
	public String url_getCompletedOrder() {
		return baseURL() + "ShopAndroid/OrderHistory";
	}
	/**获历史订单列表*/
	public String url_getAccountInfo() {
		return baseURL() + "ShopAndroid/MyInfo";
	}
	/**获历史订单列表*/
	public String url_getMyMesInfo() {
		return baseURL() + "ShopAndroid/MyMsg";
	}
	/**收藏商品列表*/
	public String url_getCollectInfo() {
		return baseURL() + "ShopAndroid/GoodsCollect";
	}
	/**获历史订单列表*/
	public String url_getMessageInfo() {
		return baseURL() + "PhoneMessage/UserMsg";
	}
	/**获前消息列表*/
	public String url_getBeforeMessageInfo() {
		return baseURL() + "PhoneMessage/MsgBefore";
	}
	/**获前消息列表*/
	public String url_getAfterMessageInfo() {
		return baseURL() + "PhoneMessage/MsgAfter";
	}
	/**获历史订单列表*/
	public String url_getMessageAccountInfo() {
		return baseURL() + "PhoneMessage/MsgNum";
	}
	/**获订单提交后获取用户信息*/
	public String url_getMyInfo() {
		return baseURL() + "ShopAndroid/GetMyInfo";
	}
	/**获取商品推荐信息*/
	public String url_getRecommendeds() {
		return baseURL() + "ShopAndroid/GoodsType";
	}
	/**获取商品推荐信息*/
	public String url_getRecommendedInfoList() {
		return baseURL() + "ShopAndroid/GoodsLists";
	}
	// 商品详情
	public String url_get_product_details() {
//		return baseURL() + "ShopAndroid/GoodsDetail/goods_id/1";
		return baseURL() + "ShopAndroid/GoodsDetail";
	}
	

}