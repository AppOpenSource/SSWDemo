package com.sswdemo.activitys;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.gson.Gson;
import com.sswdemo.adapters.CompletedOrdersAdapter;
import com.sswdemo.base.AppParameters;
import com.sswdemo.beans.OrderbacklogBean;
import com.sswdemo.beans.OrderbacklogDataBean;
import com.sswdemo.helper.HttpTools;
import com.sswdemo.helper.QHttpClient;
import com.sswdemo.servcie.LoginManager;

public class CompletedOrdersAcitvity extends Activity implements OnClickListener {
	//private List<Map<String,Object>> listData;
	private ListView completedorderLists;
	private Dialog mProgressDialog;
	private LoginManager lm;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.completedorder_layout);
		findViewById(R.id.back_btn).setOnClickListener(this);
		lm = new LoginManager(this);
		new CompletedOrderAsyncTask(String.valueOf(lm.getUserId())).execute();
	}
	
	/******************************************************************************
	 * 1.3获取某商家商品默认列表接口（从某商家页面跳转至商品列表时调用）
	 */
	private class CompletedOrderAsyncTask extends AsyncTask<Void, Void,  OrderbacklogBean> {
		private String _user_id;

		public CompletedOrderAsyncTask(String user_id){
			this._user_id = user_id;
		}
		protected OrderbacklogBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			OrderbacklogBean mOrderbacklogBean = null;
			try { 
				String request = http.httpGet(AppParameters.getInstance().url_getCompletedOrder()+"/user_id",_user_id);
				Gson gson = new Gson();
				mOrderbacklogBean = gson.fromJson(request, OrderbacklogBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mOrderbacklogBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(CompletedOrdersAcitvity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		protected void onPostExecute(OrderbacklogBean mOrderbacklogBean) {
			mProgressDialog.dismiss();
			if (mOrderbacklogBean != null) {
				if(mOrderbacklogBean.getRes() == 1)
					Toast.makeText(getApplicationContext(), "请求数据失败！", Toast.LENGTH_LONG).show();
				else
					initListView(mOrderbacklogBean.getData());

			}
		}
	}

	private void initListView(List<OrderbacklogDataBean> listData){
		completedorderLists = (ListView)findViewById(R.id.completed_list);
		
		CompletedOrdersAdapter mCompletedOrdersAdapter= new CompletedOrdersAdapter(this,listData);
		completedorderLists.setAdapter(mCompletedOrdersAdapter);
		completedorderLists.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				Intent intent = new Intent(CompletedOrdersAcitvity.this,OrderDetailsActivity.class);
				startActivity(intent);
			}});

	}
	
	public void onClick(View v) {
		this.finish();
	}
	
}
