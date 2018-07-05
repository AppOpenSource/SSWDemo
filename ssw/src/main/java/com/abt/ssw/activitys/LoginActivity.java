package com.abt.ssw.activitys;

import java.util.LinkedHashSet;
import java.util.Set;
import org.json.JSONObject;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.google.gson.Gson;
import com.abt.ssw.base.AppParameters;
import com.abt.ssw.beans.LoginBean;
import com.abt.ssw.helper.HttpTools;
import com.abt.ssw.servcie.LoginManager;
import com.abt.ssw.utils.HttpUtil;
/************************************************************************
 * 登录页面
 * @author Administrator
 *
 */
public class LoginActivity extends Activity implements OnClickListener,TagAliasCallback {

	private Dialog mProgressDialog;
	private EditText username_text;
	private EditText password_text;
	private Button myloginBtn;
	private LoginManager lm;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		initView();
	}
    private void initView(){
    	username_text = (EditText)findViewById(R.id.username);
    	password_text = (EditText)findViewById(R.id.password);
    	myloginBtn = (Button)findViewById(R.id.loginBtn);
    	myloginBtn.setOnClickListener(this);
    }
	public void onClick(View v) {
		new LoginAsyncTask(username_text.getText().toString().trim(),password_text.getText().toString().trim()).execute();
	}

	/******************************************************************************
	 * 1.1获取商家列表接口
	 */
	private class LoginAsyncTask extends AsyncTask<Void, Void,  LoginBean> {
		private String _userName;
		private String _password;

		public LoginAsyncTask(String userName, String password) {
			this._userName = userName;
			this._password = password;
		}
		protected LoginBean doInBackground(Void... params) {
		//	QHttpClient http = new QHttpClient();
			//List<NameValuePair> parm = new ArrayList<NameValuePair>(); 
			JSONObject json = new JSONObject();
			LoginBean mLoginBean = null;
			try { 
				/*parm.add(new BasicNameValuePair("upasswd", _password)); 
				parm.add(new BasicNameValuePair("uname", _userName));*/ 
				json.put("upasswd", _password);
				json.put("uname", _userName);
				 //HttpUtil.doPost(uri,"parm",jObj.toString()); 
				String request = HttpUtil.doPost(AppParameters.getInstance().url_getLogin(),"parm", json.toString());
				Gson gson = new Gson();
				mLoginBean = gson.fromJson(request, LoginBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mLoginBean;
		}
	    protected void onPreExecute() {  
	    	mProgressDialog = new Dialog(LoginActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
        }  
		protected void onPostExecute(LoginBean mLoginBean) {
			mProgressDialog.dismiss();
			if (mLoginBean != null) {
				Logins(_userName,mLoginBean);
			}
		}
	}
	 public void Logins(String username,LoginBean mLoginBean){
		 if(mLoginBean.getRes() == 0){
			    lm = new LoginManager(LoginActivity.this);
		    	lm.login(username, mLoginBean.getUser_id());
			    System.out.println("用户的id"+mLoginBean.getUser_id());
			    setResult(RESULT_OK);
			    Set<String> tagSet = new LinkedHashSet<String>();
		        tagSet.add("1"+lm.getUserId());
		         //registerMessageReceiver();  // used for receive msg
		 	    //调用JPush API设置Tag
		 	    JPushInterface.setAliasAndTags(getApplicationContext(), null, tagSet, this);
			    finish();
		 }else{
			 Toast.makeText(getApplicationContext(), "请求数据失败！", Toast.LENGTH_LONG).show(); 
		 }
	 }
	
	@Override
	public void gotResult(int code, String alias, Set<String> tags) {
		String logs ;
		switch (code) {
		case 0:
			logs = "Set tag and alias success, alias = " + alias + "; tags = " + tags;
			//Log.i(TAG, logs);
			break;
		
		default:
			logs = "Failed with errorCode = " + code + " " + alias + "; tags = " + tags;
			//Log.e(TAG, logs);
		}
		ExampleUtil.showToast(logs, getApplicationContext());
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回键
		if(keyCode == KeyEvent.KEYCODE_BACK){
		/*	if(MyInfoData.isform_message){
				MyInfoData.isnot_login = true;
				MyInfoData.isform_message = false;
			}*/
		      this.finish();
			//return true; 
		}   
		return super.onKeyDown(keyCode, event);   
	}
	
}
