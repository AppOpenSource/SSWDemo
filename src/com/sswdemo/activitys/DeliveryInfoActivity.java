package com.sswdemo.activitys;


import com.google.gson.Gson;
import com.sswdemo.adapters.DeliveryInfoAdapter;
import com.sswdemo.base.AppParameters;
import com.sswdemo.beans.DeliveryInfoBean;
import com.sswdemo.beans.OrderDetailsBean;
import com.sswdemo.helper.HttpTools;
import com.sswdemo.helper.QHttpClient;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/*************************************************************************
 * 配送信息
 * @author Administrator
 *
 */
public class DeliveryInfoActivity extends Activity implements OnClickListener{
	private ListView DeliveryLists;
	private Dialog mProgressDialog;
	private TextView dingdanhao;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.delivery_info_layout);
		initView();
		Intent inte = getIntent();     
		String orderid1 = inte.getStringExtra("orderid");   
		new DeliveryInfoAsyncTask(orderid1).execute();
	
	}
	private void initView(){
		findViewById(R.id.back_btn).setOnClickListener(this);
		dingdanhao = (TextView)findViewById(R.id.dingdanbianhao);
		DeliveryLists = (ListView)findViewById(R.id.delivery_lists);
	}
	
	/******************************************************************************
	 * 1.19订单详情
	 */
	private class DeliveryInfoAsyncTask extends AsyncTask<Void, Void,  DeliveryInfoBean> {
		private String _order_id;

		public DeliveryInfoAsyncTask(String order_id) {
			this._order_id = order_id;
		}
		protected DeliveryInfoBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			DeliveryInfoBean mDeliveryInfoBean = null;
			try { 
				String request = http.httpGet(AppParameters.getInstance().url_getDeliveryInfo()+"/order_id",_order_id);
				Gson gson = new Gson();
				mDeliveryInfoBean = gson.fromJson(request, DeliveryInfoBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mDeliveryInfoBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(DeliveryInfoActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  

		protected void onPostExecute(DeliveryInfoBean mDeliveryInfoBean) {
			mProgressDialog.dismiss();
			if (mDeliveryInfoBean != null) {
				initData(mDeliveryInfoBean);
			}
		}
	}
	
	/**
	 * 初始化数据
	 * @param mDeliveryInfoBean
	 */
	private void initData(DeliveryInfoBean mDeliveryInfoBean){
		dingdanhao.setText(mDeliveryInfoBean.getOrder_sn());
		DeliveryInfoAdapter mDeliveryInfoAdapter = new DeliveryInfoAdapter(this,mDeliveryInfoBean.getData());
		DeliveryLists.setAdapter(mDeliveryInfoAdapter);
	}
	public void onClick(View v) {
		this.finish();
		
	}

}
