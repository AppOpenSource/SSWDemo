package com.sswdemo.activitys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.sswdemo.base.AppParameters;
import com.sswdemo.beans.AccountInfoBean;
import com.sswdemo.beans.CollectProductBean;
import com.sswdemo.beans.ProductDetailsBean;
import com.sswdemo.beans.ProductDetailsDataBean;
import com.sswdemo.helper.HttpTools;
import com.sswdemo.helper.QHttpClient;
import com.sswdemo.model.Product;
import com.sswdemo.model.Shop;
import com.sswdemo.servcie.LoginManager;
import com.sswdemo.servcie.ShoppingCartDBService;
/*********************************************************************
 * 商品详情
 * @author nixuena
 *
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class ProductDetailsActivity extends Activity implements OnClickListener{
	private ImageButton backBtn;
	private ViewFlipper mViewFlipper;
	private LinearLayout ClassifyBtn;  
	private LinearLayout SortBtn; 
	private Button Guanzhu_btn;

	private TextView shangPinMing;
	private TextView shangpinSrc;
	private TextView yunFei;
	private TextView yueXiaoLiang;
	private TextView huoHao;
	private TextView fengGe;
	private TextView zhuTuLaiYuan;
	private TextView houHao2;
	private Dialog mProgressDialog;
	private String goodsId;
	private String shopid;
	private String shopName;
	private String goodsName;
	private String goodsPrice;
	private String maketPrice;
	private String sellNum;
	private String Total;
	private String Brand;
	private String Desc;
	private String goodsimage;
	private List<ProductDetailsDataBean> listData;
	private LoginManager lm;
	private String goods_id;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.productdetails);
		initView();
		goods_id = getIntent().getStringExtra("goods_id");
		String goods_image = getIntent().getStringExtra("goods_image");
		Log.d("TAG", "goods_id : " + goods_id);
		lm = new LoginManager(this);

		//请求商品详情的数据
		new ProductDetailsAsyncTask(String.valueOf(lm.getUserId()),goods_id).execute();

		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()       
		.detectDiskReads()       
		.detectDiskWrites()       
		.detectNetwork()         
		.penaltyLog()       
		.build());       
		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()       
		.detectLeakedSqlLiteObjects()    
		.penaltyLog()       
		.penaltyDeath()       
		.build());   

	}

	public void initView(){
		mViewFlipper=(ViewFlipper) findViewById(R.id.details);
		mViewFlipper.startFlipping(); 

		backBtn = (ImageButton)findViewById(R.id.back_btn);
		backBtn.setOnClickListener(this);

		ClassifyBtn = (LinearLayout)findViewById(R.id.left_relative);	
		ClassifyBtn.setOnClickListener(this);

		SortBtn = (LinearLayout)findViewById(R.id.right_relative);
		SortBtn.setOnClickListener(this);
		findViewById(R.id.welcom_text).setOnClickListener(this);

		shangPinMing = (TextView) findViewById(R.id.zoujinwomen_text);
		shangpinSrc = (TextView) findViewById(R.id.shangpin_src);
		yunFei = (TextView) findViewById(R.id.fare);
		yueXiaoLiang = (TextView) findViewById(R.id.total_month);
		huoHao = (TextView) findViewById(R.id.huohao);
		fengGe = (TextView) findViewById(R.id.fengge);
		zhuTuLaiYuan = (TextView) findViewById(R.id.pic_src);
		houHao2 = (TextView) findViewById(R.id.huohao2);

		Guanzhu_btn = (Button)findViewById(R.id.guanzhu_btn);
		Guanzhu_btn.setOnClickListener(this);

	}

	protected void onResume() {
		super.onResume();
	}


	private void jumpToSubmitActivity() {
		Intent intent = new Intent(this, SubmitOrdersActivity.class);
		List<Shop> shopList = new ArrayList<Shop>();
		Shop shop = null;
		List<Product> productList = new ArrayList<Product>();
		shop = new Shop(Integer.parseInt(listData.get(0).getShopid()), listData.get(0).getShopname(), productList);
		shopList.add(shop);
		Product product = new Product(Integer.parseInt(listData.get(0).getGoods_id()), 
				listData.get(0).getGoodsimage(), 
				listData.get(0).getGoodsname(), 
				Float.parseFloat(listData.get(0).getGoodsprice()), 
				1,
				listData.get(0).getDesc());
		productList.add(product);
		/*		for(ShoppingCart sc: shoppingCarts){
			if(sc.isChecked()){
				if(sc.isTitle()){
					productList = new ArrayList<Product>();
					shop = new Shop(sc.getShop().getId(), sc.getShop().getName(), productList);
					shopList.add(shop);
				}else{
					if(shop == null || (shop != null && shop.getId() != sc.getShop().getId())){
						productList = new ArrayList<Product>();
						shop = new Shop(sc.getShop().getId(), sc.getShop().getName(), productList);
						shopList.add(shop);
					}
					Product product = new Product(sc.getProduct().getProductId(), 
							sc.getProduct().getIcon(), 
							sc.getProduct().getTitle(), 
							sc.getProduct().getPrice(), 
							sc.getProduct().getCount(),
							sc.getProduct().getDescription());
					productList.add(product);
				}
			}
		}*/
		intent.putExtra("shops", (Serializable)shopList);
		try{
			startActivity(intent);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class ProductDetailsAsyncTask extends AsyncTask<Void, Void,  ProductDetailsBean> {
		private String user_id;
		private String goods_id;

		public ProductDetailsAsyncTask(String user_id,String goods_id) {
			this.user_id = user_id;
			this.goods_id = goods_id;
		}
		protected ProductDetailsBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			ProductDetailsBean mProductDetailsBean = null;
			try {
				String str = "user_id/"+user_id+"/goods_id/" + goods_id;
				String request = http.httpGet(AppParameters.getInstance().url_get_product_details(), str);
				Gson gson = new Gson();
				mProductDetailsBean = gson.fromJson(request, ProductDetailsBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mProductDetailsBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(ProductDetailsActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		protected void onPostExecute(ProductDetailsBean mProductDetailsBean) {
			mProgressDialog.dismiss();
			if (mProductDetailsBean != null) {
				listData = mProductDetailsBean.getData();

				Bitmap bmm_load = HttpTools.returnBitMap(listData.get(0).getGoodsimage());
				mViewFlipper.addView(getImageView(bmm_load));
				mViewFlipper.startFlipping(); 
				if(mProductDetailsBean.getData().get(0).getCollectgoods().equals("1")){
					Guanzhu_btn.setText("已收藏");
				}else if(mProductDetailsBean.getData().get(0).getCollectgoods().equals("0")){
					Guanzhu_btn.setText("未收藏");
				}

				for (int i=0; i < listData.size(); i++) {
					listData.get(i);
					/*Log.d("TAG", "############## listData.get(" + i + "): " + listData.get(i));
					Log.d("TAG", "############## listData.get(" + i + ").getGoods_id(): " + listData.get(i).getGoods_id());
					Log.d("TAG", "############## listData.get(" + i + ").getShopid(): " + listData.get(i).getShopid());
					Log.d("TAG", "############## listData.get(" + i + ").getShopname(): " + listData.get(i).getShopname());
					Log.d("TAG", "############## listData.get(" + i + ").getGoodsname(): " + listData.get(i).getGoodsname());
					Log.d("TAG", "############## listData.get(" + i + ").getGoodsprice(): " + listData.get(i).getGoodsprice());
					Log.d("TAG", "############## listData.get(" + i + ").getMaketprice(): " + listData.get(i).getMaketprice());
					Log.d("TAG", "############## listData.get(" + i + ").getSellnum(): " + listData.get(i).getSellnum());
					Log.d("TAG", "############## listData.get(" + i + ").getTotal(): " + listData.get(i).getTotal());
					Log.d("TAG", "############## listData.get(" + i + ").getBrand(): " + listData.get(i).getBrand());
					Log.d("TAG", "############## listData.get(" + i + ").getDesc(): " + listData.get(i).getDesc());
					Log.d("TAG", "############## listData.get(" + i + ").getGoodsimage(): " + listData.get(i).getGoodsimage());*/
					goodsId = listData.get(i).getGoods_id();
					shopid = listData.get(i).getShopid();
					shopName = listData.get(i).getShopname();
					goodsName = listData.get(i).getGoodsname();
					goodsPrice = listData.get(i).getGoodsprice();
					maketPrice = listData.get(i).getMaketprice();
					sellNum = listData.get(i).getSellnum();
					Total = listData.get(i).getTotal();
					Brand = listData.get(i).getBrand();
					Desc = listData.get(i).getDesc();
					goodsimage = listData.get(i).getGoodsimage();

					shangPinMing.setText("商品名：" + Brand);
					shangpinSrc.setText("商品来源：" + shopName);
					yunFei.setText("运费（到北京）：卖家承担运费");
					yueXiaoLiang.setText("月销量：33件");
					huoHao.setText("价格：" + goodsPrice);
					fengGe.setText("风格：甜美");
					zhuTuLaiYuan.setText("主图来源：自主实拍图");
					houHao2.setText("货号：D50345345");

				}

				/*Bitmap bmm_load = HttpTools.returnBitMap(goodsimage);
				Log.d("TAG", "############## bmm_load" + bmm_load);
				mViewFlipper.addView(getImageView(bmm_load));
				mViewFlipper.startFlipping(); */
			}
		}
	}

	private ImageView getImageView(Bitmap bm){
		ImageView imageView = new ImageView(this);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setImageBitmap(bm);
		return imageView;
	}

	/***********************************************************************
	 * 1.17收藏商品
	 */
	private class GoodsCollectAsyncTask extends AsyncTask<Void, Void,  CollectProductBean> {
		private String _user_id;
		private String _goods_id;

		public GoodsCollectAsyncTask(String user_id,String goods_id){
			this._user_id = user_id;
			this._goods_id = goods_id;
		}

		protected CollectProductBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			CollectProductBean mCollectProductBean = null;
			try { 
				String str = "user_id/"+_user_id+"/goods_id/" + _goods_id;
				String request = http.httpGet(AppParameters.getInstance().url_getCollectInfo(),str);
				Gson gson = new Gson();
				mCollectProductBean = gson.fromJson(request, CollectProductBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mCollectProductBean;
		}
		protected void onPreExecute() {  
			/*	mProgressDialog = new Dialog(ProductDetailsActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();*/
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		protected void onPostExecute(CollectProductBean mCollectProductBean) {
			mProgressDialog.dismiss();
			if (mCollectProductBean != null) {
				if(mCollectProductBean.getRes() == 0){
					Guanzhu_btn.setText("已收藏");
				}else{
					Guanzhu_btn.setText("未收藏");
				}
				System.out.println("===================="+mCollectProductBean.getMsg()+mCollectProductBean.getRes());
			}
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.guanzhu_btn://收藏按钮
			if(Guanzhu_btn.getText().toString().equals("未收藏"))
				new GoodsCollectAsyncTask(String.valueOf(lm.getUserId()),goods_id).execute();
			else
				Toast.makeText(getApplicationContext(), "该商品已添加收藏了！", Toast.LENGTH_LONG).show();

			break;
		case R.id.goodslist_relative:
			Intent goodsIntent = new Intent(ProductDetailsActivity.this, GoodsListActivity.class);
			startActivity(goodsIntent);
			break;
		case R.id.welcom_text:
			ShoppingCartDBService sdbs = new ShoppingCartDBService(this);
			try {
				sdbs.insert(Integer.parseInt(shopid), shopName, Integer.parseInt(listData.get(0).getGoods_id()), listData.get(0).getGoodsimage(), goodsName, Float.parseFloat(goodsPrice), Desc, 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Intent intent = new Intent(ProductDetailsActivity.this, ShoppingCartActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.right_relative:
			LoginManager lm = new LoginManager(this);
			if(lm.getUserId()==-1){
				//jump to LoginActivity
				Intent i = new Intent(ProductDetailsActivity.this, LoginActivity.class);
				startActivityForResult(i, 0);
				return;	
			}
			jumpToSubmitActivity();

			finish();
			break;
		default:
			break;
		}
	}

}
