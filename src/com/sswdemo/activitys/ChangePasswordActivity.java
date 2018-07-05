package com.sswdemo.activitys;

import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import com.google.gson.Gson;
import com.sswdemo.base.AppParameters;
import com.sswdemo.beans.PasswordBean;
import com.sswdemo.helper.HttpTools;
import com.sswdemo.servcie.LoginManager;
import com.sswdemo.utils.HttpUtil;
import com.sswdemo.views.AccountItemView;
/**
 * 修改密码
 * @author Administrator
 *
 */
public class ChangePasswordActivity extends Activity implements OnClickListener{
	private AccountItemView mOldPasswordLayout,NewPasswordLayout,PasswordOkLayout;
	private Dialog mProgressDialog;
	private Button confromBtn;
	private LoginManager lm;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_changepassword);
		lm = new LoginManager(this);
		mOldPasswordLayout = (AccountItemView)findViewById(R.id.oldpassword_layout);
		mOldPasswordLayout.getTitleView().setText("原密码");
		mOldPasswordLayout.getCountView().setText("");
		
		NewPasswordLayout = (AccountItemView)findViewById(R.id.newpassword_layout);
		NewPasswordLayout.getTitleView().setText("新密码");
		NewPasswordLayout.getCountView().setText("");
		
		PasswordOkLayout = (AccountItemView)findViewById(R.id.passwordok_layout);
		PasswordOkLayout.getTitleView().setText("确认新密码");
		PasswordOkLayout.getCountView().setText("");
		
		confromBtn = (Button)findViewById(R.id.commit);
		confromBtn.setOnClickListener(this);
		
		findViewById(R.id.back_btn).setOnClickListener(this);

	}
	
	/******************************************************************************
	 * 1.15密码修改
	 */
	private class ChangePwdAsyncTask extends AsyncTask<Void, Void,  PasswordBean> {
		private String _user_id;
		private String _passwd;
		private String _newpasswd;
		private String _newupasswd;

		public ChangePwdAsyncTask(String user_id, String passwd,String newpasswd,String newupasswd) {
			this._user_id = user_id;
			this._passwd = passwd;
			this._newpasswd = newpasswd;
			this._newupasswd = newupasswd;
		}

		protected PasswordBean doInBackground(Void... params) {
			JSONObject json = new JSONObject();
			PasswordBean mPasswordBean = null;
			try { 
				json.put("user_id", _user_id);
				json.put("passwd", _passwd);
				json.put("newpasswd", _newpasswd);
				json.put("newupasswd", _newupasswd);
				String request =  HttpUtil.doPost(AppParameters.getInstance().url_getServeList(),
						"parm",json.toString());
				Gson gson = new Gson();
				mPasswordBean = gson.fromJson(request, PasswordBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mPasswordBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(ChangePasswordActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		protected void onPostExecute(PasswordBean mPasswordBean) {
			mProgressDialog.dismiss();
			if (mPasswordBean != null) {
				if(mPasswordBean.getRes() == 1)
					Toast.makeText(getApplicationContext(), "修改失败！", Toast.LENGTH_LONG).show();
				else{
					Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			finish();
			break;
		case R.id.commit:
			String userId = String.valueOf(lm.getUserId());
			String yuanshimima = mOldPasswordLayout.getCountView().getText().toString();
			String xinmima = NewPasswordLayout.getCountView().getText().toString();
			String querenmima = PasswordOkLayout.getCountView().getText().toString();
			new ChangePwdAsyncTask(userId,yuanshimima,xinmima,querenmima).execute();
			break;
		default:
			break;
		}
		
	}
}
