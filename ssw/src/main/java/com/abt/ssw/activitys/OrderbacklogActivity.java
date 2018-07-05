package com.abt.ssw.activitys;
import java.util.List;
import com.google.gson.Gson;
import com.abt.ssw.adapters.OrderbacklogAdapter;
import com.abt.ssw.base.AppParameters;
import com.abt.ssw.beans.OrderbacklogBean;
import com.abt.ssw.beans.OrderbacklogDataBean;
import com.abt.ssw.helper.HttpTools;
import com.abt.ssw.helper.QHttpClient;
import com.abt.ssw.servcie.LoginManager;
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
/**********************************************************
 * 未完成订单
 * @author Administrator
 *
 */
public class OrderbacklogActivity extends Activity implements OnClickListener {
	private ListView orderbacklog_list;
	private Dialog mProgressDialog;
	private LoginManager lm;
	private OrderbacklogBean orderbacklogBean = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderbacklog_layout);
		findViewById(R.id.back_btn).setOnClickListener(this);
		lm = new LoginManager(this);
		new OrderbacklogAsyncTask(String.valueOf(lm.getUserId())).execute();
	}
	
	/******************************************************************************
	 * 1.3获取某商家商品默认列表接口（从某商家页面跳转至商品列表时调用）
	 */
	private class OrderbacklogAsyncTask extends AsyncTask<Void, Void,  OrderbacklogBean> {
		private String _user_id;

		public OrderbacklogAsyncTask(String user_id) {
			this._user_id = user_id;
		}
		protected OrderbacklogBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			OrderbacklogBean mOrderbacklogBean = null;
			try { 
				String request = http.httpGet(AppParameters.getInstance().url_getOrderbacklog()+"/user_id",_user_id);
				Gson gson = new Gson();
				mOrderbacklogBean = gson.fromJson(request, OrderbacklogBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mOrderbacklogBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(OrderbacklogActivity.this,R.style.theme_dialog_alert);
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
				else{
					orderbacklogBean = mOrderbacklogBean;
					initListView(mOrderbacklogBean.getData());
				}
					
			}
		}
	}

	private void initListView(List<OrderbacklogDataBean> listData){
		orderbacklog_list = (ListView)findViewById(R.id.orderbacklog_list);
		OrderbacklogAdapter mOrderbacklogAdapter= new OrderbacklogAdapter(this,listData);
		orderbacklog_list.setAdapter(mOrderbacklogAdapter);
		orderbacklog_list.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {
				Intent intent = new Intent(OrderbacklogActivity.this,OrderDetailsActivity.class);
				intent.putExtra("orderid",orderbacklogBean.getData().get(position).getOrder_id());
				startActivity(intent);
			}});
	}
	public void onClick(View v) {
		this.finish();
	}


}
