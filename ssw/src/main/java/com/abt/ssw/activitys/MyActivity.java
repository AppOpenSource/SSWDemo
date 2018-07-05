package com.abt.ssw.activitys;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.abt.ssw.base.AppParameters;
import com.abt.ssw.beans.MyMsgInfoBean;
import com.abt.ssw.beans.MyMsgInfoDataBean;
import com.abt.ssw.helper.HttpTools;
import com.abt.ssw.helper.QHttpClient;
import com.abt.ssw.servcie.LoginManager;
import com.abt.ssw.views.OrderItemView;

/**我的*/
public class MyActivity extends Activity implements OnClickListener{
	private Dialog mProgressDialog;
	private OrderItemView mOrderbacklogLayout, mOrderHistoryLayout,mAccountInfoLayout,
	mChangePasswordLayout,mMyfocusLayout,mMessageCenterLayout;
	private TextView myname;
	private LoginManager lm;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_layout);
		findViewById(R.id.back_btn).setOnClickListener(this);
		mOrderbacklogLayout = (OrderItemView)findViewById(R.id.Orderbacklog_layout);
		mOrderbacklogLayout.getTitleView().setText("未完成订单");
		
		mOrderbacklogLayout.setOnClickListener(this);

		mOrderHistoryLayout = (OrderItemView)findViewById(R.id.OrderHistory_layout);
		mOrderHistoryLayout.getTitleView().setText("历史订单");
	
		mOrderHistoryLayout.setOnClickListener(this);

		mAccountInfoLayout = (OrderItemView)findViewById(R.id.AccountInfo_layout);
		mAccountInfoLayout.getTitleView().setText("账户信息");
		//mAccountInfoLayout.getCountView().setText("5");
		mAccountInfoLayout.setOnClickListener(this);

		mChangePasswordLayout = (OrderItemView)findViewById(R.id.ChangePassword_layout);
		mChangePasswordLayout.getTitleView().setText("修改密码");
		//mChangePasswordLayout.getCountView().setText("5");
		mChangePasswordLayout.setOnClickListener(this);



		mMyfocusLayout = (OrderItemView)findViewById(R.id.Myfocus_layout);
		mMyfocusLayout.getTitleView().setText("我的关注");
		//mMyfocusLayout.getCountView().setText("5");
		mMyfocusLayout.setOnClickListener(this);

		mMessageCenterLayout = (OrderItemView)findViewById(R.id.MessageCenter_layout);
		mMessageCenterLayout.getTitleView().setText("消息中心");
		//mAccountBalanceLayout.getCountView().setText("5");
		mMessageCenterLayout.setOnClickListener(this);
		
		myname = (TextView)findViewById(R.id.truename);
		
		lm = new LoginManager(this);
		if(lm.getUserId()==-1){
			Intent i = new Intent(MyActivity.this, LoginActivity.class);
			startActivityForResult(i, 0);
			return;	
		}
		new MyMsgAsyncTask(String.valueOf(lm.getUserId())).execute();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { 
		case RESULT_OK://resultCode为回传的标记，我在B中回传的是RESULT_OK
			new MyMsgAsyncTask(String.valueOf(lm.getUserId())).execute();
			break;
		default:
			break;
		}
	}
	/***********************************************************************
	 * 1.13账户信息
	 */
	private class MyMsgAsyncTask extends AsyncTask<Void, Void,  MyMsgInfoBean> {
		private String _user_id;

		public MyMsgAsyncTask(String user_id){
			this._user_id = user_id;
		}
		
		protected MyMsgInfoBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			MyMsgInfoBean mMyMsgInfoBean = null;
			try { 
				String request = http.httpGet(AppParameters.getInstance().url_getMyMesInfo()+"/user_id",_user_id);
				Gson gson = new Gson();
				mMyMsgInfoBean = gson.fromJson(request, MyMsgInfoBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mMyMsgInfoBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(MyActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		protected void onPostExecute(MyMsgInfoBean mMyMsgInfoBean) {
			mProgressDialog.dismiss();
			if (mMyMsgInfoBean != null) {
				if(mMyMsgInfoBean.getRes() == 1)
					Toast.makeText(getApplicationContext(), "请求数据失败！", Toast.LENGTH_LONG).show();
				else
					initData(mMyMsgInfoBean.getData().get(0));
			}
		}
	}
	private void initData(MyMsgInfoDataBean mMyMsgInfoDataBean){
		myname.setText("真实姓名："+mMyMsgInfoDataBean.getTruename());
		mOrderbacklogLayout.getCountView().setText(mMyMsgInfoDataBean.getUninfo());//未完成订单数量
		mOrderHistoryLayout.getCountView().setText(mMyMsgInfoDataBean.getAlinfo()); //历史订单数量
	}

	public void onClick(View v) {
		switch (v.getId()) {
		    //未完成订单
		case R.id.Orderbacklog_layout:
			startActivity(new Intent(MyActivity.this, OrderbacklogActivity.class));
			break;
			//历史订单
		case R.id.OrderHistory_layout:
			startActivity(new Intent(MyActivity.this, CompletedOrdersAcitvity.class));
			break;
			//账户信息
		case R.id.AccountInfo_layout:
			startActivity(new Intent(MyActivity.this, AccountInfoActivity.class));
			break;
			//修改密码
		case R.id.ChangePassword_layout:
			startActivity(new Intent(MyActivity.this, ChangePasswordActivity.class));
			break;
			//消息中心
		case R.id.MessageCenter_layout:
			startActivity(new Intent(MyActivity.this, MessageActivity.class));
			break;
			//我的关注
		case R.id.Myfocus_layout:
			startActivity(new Intent(MyActivity.this, MyFocusActivity.class));
			break;
		case R.id.back_btn:
			this.finish();
			break;
		}

	}
}
