package com.sswdemo.servcie;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.JsonArray;
import com.sswdemo.model.Order;
import com.sswdemo.model.Product;
import com.sswdemo.model.Shop;
import com.sswdemo.utils.HttpUtil;
import com.sswdemo.utils.L;



public class GetOrderHttpService {
	private static final String TAG = "ssw";
	public static final String uri = "http://121.199.13.117/beta/index.php/ShopAndroid/ShowOrder";

	/**
	 * get action list 
	 * @param date
	 * @param onRefresh if(onRefresh) get action list which date < ?
	 * @return
	 */
	public List<Order> getOrders(){
		try {
			int userId = 11;
			String result = HttpUtil.doGet(uri, "[[\"user_id\",\"" + userId + "\"]]");
			JSONObject jsonObj = new JSONObject(result);
			L.d(TAG, "getOrders res: " + jsonObj.toString());
//			{
//			    "res": 0,
//			    "data": [
//			        {
//			            "order_sn": "KGE54121566133",
//			            "ordertotal": "0.00",
//			            "confirm_time": "0",
//			            "goods_name": "赤霞珠",
//			            "goodsimg": "http://localhost/beta/Public/Uploads/goodsimgs/m_51d8fc543ddc3.jpg"
//			        }
//			     ]
//			}


			//parse
			int res = jsonObj.getInt("res");
			if(res == 0){
				List<Order> orders = new ArrayList<Order>();
				JSONArray ordersArray = (JSONArray) jsonObj.get("data");
				for(int i = 0; i < ordersArray.length(); i++){
					
					JSONObject orderObj = ordersArray.getJSONObject(i);
					Order order = new Order(orderObj.getString("order_sn"), 
							(float)(orderObj.getDouble("ordertotal")), 
							orderObj.getString("goods_name"), 
							orderObj.getLong("confirm_time"), 
							orderObj.getString("goodsimg"));
					orders.add(order);
				}
				return orders;
			}else {
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
