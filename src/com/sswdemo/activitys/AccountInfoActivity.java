package com.sswdemo.activitys;

import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sswdemo.base.AppParameters;
import com.sswdemo.beans.AccountInfoBean;
import com.sswdemo.beans.AccountInfoDataBean;
import com.sswdemo.helper.HttpTools;
import com.sswdemo.helper.QHttpClient;
import com.sswdemo.servcie.LoginManager;
import com.sswdemo.views.AccountItemView;

/****************************************************************
 * 账户信息
 * @author xuena.ni
 */
public class AccountInfoActivity extends Activity implements OnClickListener{
	private AccountItemView mUserNameLayout,TelephoneLayout,
	EmailLayout,AddressLayout,TrueNameLayout;
	private Dialog mProgressDialog;
	private LoginManager lm;
	private RadioButton boyRadioBtn;
	private RadioButton grilRadioBtn;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_account);
		findViewById(R.id.back_btn).setOnClickListener(this);
		lm = new LoginManager(this);
		new AccountInfoAsyncTask(String.valueOf(lm.getUserId())).execute();
	}
	
	
	/***********************************************************************
	 * 1.13账户信息
	 */
	private class AccountInfoAsyncTask extends AsyncTask<Void, Void,  AccountInfoBean> {
		private String _user_id;

		public AccountInfoAsyncTask(String user_id){
			this._user_id = user_id;
		}
		
		protected AccountInfoBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			AccountInfoBean mAccountInfoBean = null;
			try { 
				String request = http.httpGet(AppParameters.getInstance().url_getAccountInfo()+"/user_id",_user_id);
				Gson gson = new Gson();
				mAccountInfoBean = gson.fromJson(request, AccountInfoBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mAccountInfoBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(AccountInfoActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		protected void onPostExecute(AccountInfoBean mAccountInfoBean) {
			mProgressDialog.dismiss();
			if (mAccountInfoBean != null) {
				if(mAccountInfoBean.getRes() == 1)
					Toast.makeText(getApplicationContext(), "请求数据失败！", Toast.LENGTH_LONG).show();
				else
					initData(mAccountInfoBean.getData().get(0));
			}
		}
	}
	
	public void initData(AccountInfoDataBean mAccountInfoDataBean){
		mUserNameLayout = (AccountItemView)findViewById(R.id.username_layout);
		mUserNameLayout.getTitleView().setText("用户名:");
		mUserNameLayout.getCountView().setText(mAccountInfoDataBean.getUsername());
		
		TelephoneLayout = (AccountItemView)findViewById(R.id.telephone_layout);
		TelephoneLayout.getTitleView().setText("手机号:");
		TelephoneLayout.getCountView().setText(mAccountInfoDataBean.getMobile());
		
		EmailLayout = (AccountItemView)findViewById(R.id.email_layout);
		EmailLayout.getTitleView().setText("邮箱:");
		EmailLayout.getCountView().setText(mAccountInfoDataBean.getEmail());
		
		AddressLayout = (AccountItemView)findViewById(R.id.address_layout);
		AddressLayout.getTitleView().setText("地址:");
		AddressLayout.getCountView().setText(mAccountInfoDataBean.getAddress());
		
		TrueNameLayout = (AccountItemView)findViewById(R.id.truename_layout);
		TrueNameLayout.getTitleView().setText("真实姓名:");
		TrueNameLayout.getCountView().setText(mAccountInfoDataBean.getTruename());
		
		boyRadioBtn = (RadioButton)findViewById(R.id.nan_radio);
		boyRadioBtn.setClickable(false);
		grilRadioBtn = (RadioButton)findViewById(R.id.nv_radio);
		grilRadioBtn.setClickable(false);
		
		if(mAccountInfoDataBean.getSex().equals("0"))
			boyRadioBtn.setChecked(true);
		    
		else
			grilRadioBtn.setChecked(true);
			
		
		
	}
	
	
   /********************************************************************
    * 点击事件
    */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			finish();
			break;

		default:
			break;
		}
		
	}
}
