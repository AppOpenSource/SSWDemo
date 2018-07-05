
package com.abt.ssw.activitys;



import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.abt.ssw.activitys.R.color;
import com.abt.ssw.base.AppParameters;
import com.abt.ssw.beans.BusinessConcernBean;
import com.abt.ssw.beans.MerchantDetailsBean;
import com.abt.ssw.beans.MerchantDetailsDataBean;
import com.abt.ssw.helper.HttpTools;
import com.abt.ssw.helper.QHttpClient;
import com.abt.ssw.servcie.LoginManager;
/****************************************************************
 * 商家详情Activity
 * @author xuena.ni
 */
@SuppressLint("HandlerLeak")
public class MerchantDetailsActivity extends Activity implements OnClickListener{


	//private ImageView showImage;
	private ImageButton backBtn;
	private RelativeLayout Goodslist_relative;
	private ViewFlipper mViewFlipper;
	private LinearLayout ClassifyBtn;  //分类按钮
	private LinearLayout SortBtn;  //排序按钮
	private TextView zoujinwomen;
	private LinearLayout lianxiwomen;
	private ImageView Welcome_img;
	private TextView Welcome_text;
	private ImageView sanjiao1;
	private ImageView Phone_Us_img;
	private TextView Phone_Us_text;
	private ImageView sanjiao2;
	private LinearLayout Left_relative;
	private LinearLayout Right_relative;
	private Dialog mProgressDialog;
	private TextView welcom_us_text;//商家描述
	private ImageView shopmaps; //商家地图
	private TextView shop_adress_text;//商家地址
	private TextView shop_phone_text; //商家电话
	private TextView title_name; //商家名字
	private String _shop_id_list = null; //商家id
	private String _shop_name = null;
	private Button guanzhu;
	private String _shop_id;
	private LoginManager lm;
	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.merchantdetails);
		initView();

		Intent intent = getIntent();
		_shop_id = intent.getStringExtra("_shop_id");  
		lm = new LoginManager(this);

		new MerchantDetailsAsyncTask(_shop_id,lm.getUserId()).execute();

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

	/*************************************************************************
	 * 初始化控件
	 */
	public void initView(){
		mViewFlipper=(ViewFlipper) findViewById(R.id.details);

		backBtn = (ImageButton)findViewById(R.id.back_btn);
		backBtn.setOnClickListener(this);
		Goodslist_relative = (RelativeLayout)findViewById(R.id.goodslist_relative);
		Goodslist_relative.setOnClickListener(this);
		ClassifyBtn = (LinearLayout)findViewById(R.id.left_relative);	
		SortBtn = (LinearLayout)findViewById(R.id.right_relative);
		ClassifyBtn.setOnClickListener(this);
		SortBtn.setOnClickListener(this);
		zoujinwomen = (TextView)findViewById(R.id.zoujinwomen_text);
		lianxiwomen = (LinearLayout)findViewById(R.id.lianxiwomen_linearlayout);
		Welcome_img = (ImageView)findViewById(R.id.welcom_img);
		Welcome_text = (TextView)findViewById(R.id.welcom_text);
		sanjiao1 = (ImageView)findViewById(R.id.sanjiao1);
		Phone_Us_img = (ImageView)findViewById(R.id.phone_us_img);
		Phone_Us_text = (TextView)findViewById(R.id.phone_us_text);
		sanjiao2 = (ImageView)findViewById(R.id.sanjiao2);
		Left_relative = (LinearLayout)findViewById(R.id.left_relative);
		Right_relative = (LinearLayout)findViewById(R.id.right_relative);
		guanzhu = (Button)findViewById(R.id.guanzhu_btn);
		guanzhu.setOnClickListener(this);
	}

	/************************************************************************
	 * 初始化数据
	 * @param listDatas
	 */
	private void initViewData(List<MerchantDetailsDataBean> listDatas){
		Bitmap bmm_load = HttpTools.returnBitMap(listDatas.get(0).getShopimage());
		mViewFlipper.addView(getImageView(bmm_load));
		mViewFlipper.startFlipping(); 
		welcom_us_text = (TextView)findViewById(R.id.zoujinwomen_text);
		welcom_us_text.setText(listDatas.get(0).getShopdesc());
		shopmaps = (ImageView)findViewById(R.id.shopmap);
		shopmaps.setImageBitmap(HttpTools.returnBitMap(listDatas.get(0).getLocationimg()));
		shop_adress_text = (TextView)findViewById(R.id.shop_adress);
		shop_adress_text.setText(listDatas.get(0).getAddress());
		shop_phone_text = (TextView)findViewById(R.id.shop_phone);
		shop_phone_text.setText(listDatas.get(0).getTelphone());
		title_name = (TextView)findViewById(R.id.title_name);
		title_name.setText(listDatas.get(0).getShopname());
		if(listDatas.get(0).getCollect().equals("0")){
			guanzhu.setText("未关注");
		}else{
			guanzhu.setText("已关注");
		}

	}

	/*************************************************************************
	 * 
	 * @param bm
	 * @return
	 */
	private ImageView getImageView(Bitmap bm){
		ImageView imageView = new ImageView(this);
		imageView.setScaleType(ScaleType.FIT_XY);
		imageView.setImageBitmap(bm);
		return imageView;
	}

	/**************************************************************************
	 * 1.2从网络获商家详情数据
	 */
	private class MerchantDetailsAsyncTask extends AsyncTask<Void, Void,  MerchantDetailsBean> {
		private String _shop_id;
		private int _user_id;

		public MerchantDetailsAsyncTask(String shop_id,int user_id) {
			this._shop_id = shop_id;
			this._user_id = user_id;
		}
		protected MerchantDetailsBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			MerchantDetailsBean mMerchantDetailsBean = null;
			try { 
				String request = http.httpGet(AppParameters.getInstance().url_getMerchantDetails(),"shop_id/"+_shop_id+"/user_id/"+_user_id);
				Gson gson = new Gson();
				mMerchantDetailsBean = gson.fromJson(request, MerchantDetailsBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mMerchantDetailsBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(MerchantDetailsActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		protected void onPostExecute(MerchantDetailsBean mMerchantDetailsBean) {
			mProgressDialog.dismiss();
			if (mMerchantDetailsBean != null) {
				if(mMerchantDetailsBean.getRes() == 1)
					Toast.makeText(getApplicationContext(), "请求数据失败！", Toast.LENGTH_LONG).show();
				else{
					initViewData(mMerchantDetailsBean.getData());
					_shop_id_list = mMerchantDetailsBean.getData().get(0).getShop_id();
					_shop_name = mMerchantDetailsBean.getData().get(0).getShopname();
				}

			}
		}
	}
	/**************************************************************************
	 * 1.9商家关注接口
	 */
	private class BusinessConcernAsyncTask extends AsyncTask<Void, Void,  BusinessConcernBean> {
		private String _shop_id;
		private int _user_id;

		public BusinessConcernAsyncTask(String shop_id,int user_id) {
			this._shop_id = shop_id;
			this._user_id = user_id;
		}
		protected BusinessConcernBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			BusinessConcernBean mBusinessConcernBean = null;
			try { 
				String request = http.httpGet(AppParameters.getInstance().url_getBusinessConcern(),"shopid/"+_shop_id+"/user_id/"+_user_id);
				Gson gson = new Gson();
				mBusinessConcernBean = gson.fromJson(request, BusinessConcernBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mBusinessConcernBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(MerchantDetailsActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		protected void onPostExecute(BusinessConcernBean mBusinessConcernBean) {
			mProgressDialog.dismiss();
			if (mBusinessConcernBean != null) {
				if(mBusinessConcernBean.getRes() == 1)
					guanzhu.setText("未关注");
				else
					guanzhu.setText("已关注");


			}
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
		case RESULT_OK:
			new BusinessConcernAsyncTask(_shop_id,lm.getUserId()).execute();
			break;
		default:
			break;
		}
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.goodslist_relative:
			Intent goodsIntent = new Intent(MerchantDetailsActivity.this,GoodsListActivity.class);
			goodsIntent.putExtra("_shop_id", _shop_id_list);
			goodsIntent.putExtra("_shop_name", _shop_name);
			startActivity(goodsIntent);
			break;
		case R.id.left_relative:
			Left_relative.setBackgroundResource(R.drawable.pop_title_bg);
			Welcome_img.setBackgroundResource(R.drawable.welcom_img1);
			Welcome_text.setTextColor(color.welcome_text_color1);
			sanjiao1.setBackgroundResource(R.drawable.sanjiao1);
			Right_relative.setBackgroundResource(R.drawable.top_title_line1);
			Phone_Us_img.setBackgroundResource(R.drawable.phone_us_img);
			Phone_Us_text.setTextColor(color.welcome_text_color);
			sanjiao2.setBackgroundResource(R.drawable.sanjiaoj);
			zoujinwomen.setVisibility(View.VISIBLE);
			lianxiwomen.setVisibility(View.GONE);
			break;
		case R.id.right_relative:
			Left_relative.setBackgroundResource(R.drawable.top_title_line1);
			Welcome_img.setBackgroundResource(R.drawable.welcom_img);
			Welcome_text.setTextColor(color.welcome_text_color);
			sanjiao1.setBackgroundResource(R.drawable.sanjiaoj);
			Right_relative.setBackgroundResource(R.drawable.pop_title_bg);
			Phone_Us_img.setBackgroundResource(R.drawable.phone_us_img1);
			Phone_Us_text.setTextColor(color.welcome_text_color1);
			sanjiao2.setBackgroundResource(R.drawable.sanjiao1);
			lianxiwomen.setVisibility(View.VISIBLE);
			zoujinwomen.setVisibility(View.GONE);
			break;
		case R.id.guanzhu_btn:

			if(lm.getUserId()==-1){
				//jump to LoginActivity
				Intent i = new Intent(MerchantDetailsActivity.this, LoginActivity.class);
				startActivityForResult(i, 0);
				return;	
			}
			new BusinessConcernAsyncTask(_shop_id,lm.getUserId()).execute();

			break;
		default:
			break;
		}

	}
}
