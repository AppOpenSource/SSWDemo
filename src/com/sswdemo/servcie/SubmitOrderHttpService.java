package com.sswdemo.servcie;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.sswdemo.beans.MyInfoData;
import com.sswdemo.model.Product;
import com.sswdemo.model.Shop;
import com.sswdemo.utils.HttpUtil;
import com.sswdemo.utils.L;



public class SubmitOrderHttpService {
	private static final String TAG = "ssw";
	public static final String uri = "http://121.199.13.117/beta/index.php/ShopAndroid/Order";
	private Context context;

	public SubmitOrderHttpService(Context context) {
		this.context = context;
	}

	/**
	 * get action list 
	 * @param date
	 * @param onRefresh if(onRefresh) get action list which date < ?
	 * @return
	 */
	public boolean submitOrder(List<Shop> shops){
		try {
			LoginManager lm = new LoginManager(context);
			MyInfoData myInfoData = MyInfoData.getInstance();
			JSONObject jObj = new JSONObject();
			jObj.put("user_id", lm.getUserId());
			jObj.put("consignee", myInfoData.getConsignee());
			jObj.put("address", myInfoData.getAddress());
			jObj.put("tel", myInfoData.getTel());
			jObj.put("mobile", myInfoData.getMobile());
			jObj.put("is_need_inv", myInfoData.getIs_need_inv());
			jObj.put("inv_payee", myInfoData.getInv_payee());
			jObj.put("shipping_id", myInfoData.getShipping_id());
			jObj.put("postscript", myInfoData.getPostscript());
			JSONArray jArray = new JSONArray();
			for(int j = 0 ; j < shops.size(); j++){
				Shop shop = shops.get(j);
				JSONObject shopJson = new JSONObject();
				
				shopJson.put("shopid", shop.getId());
				JSONArray shopArray = new JSONArray();
				List<Product> pList = shop.getProductList();
				for(int i =0; i< pList.size(); i++){
					Product p = pList.get(i);
					JSONObject productObj = new JSONObject();
					productObj.put("goods_id", p.getProductId());
					productObj.put("goodsname", p.getTitle());
					productObj.put("goodsprice", p.getPrice());
					productObj.put("goodsnumber", p.getCount());
					shopArray.put(i,productObj);
				}
				shopJson.put("order", shopArray);
				
				jArray.put(j, shopJson);
			}
			jObj.put("data", jArray);
			L.d(TAG, "submitOrder: " + jObj.toString());
			String result = HttpUtil.doPost(uri,"parm",jObj.toString());
			JSONObject jsonObj = new JSONObject(result);
			L.d(TAG, "submitOrder res: " + jsonObj.toString());
			//parse
			int res = jsonObj.getInt("res");
			if(res == 0){
				return true;
			}else 
				return false;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
