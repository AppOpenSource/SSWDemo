package com.abt.ssw.activitys;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.abt.ssw.servcie.LoginManager;


import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
/**
 * 启动页面
 * @author Administrator
 *
 */
public class StartActivity extends Activity implements TagAliasCallback{
	private LoginManager lm;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_start);
		
		lm = new LoginManager(this);
		if(lm.getUserId()!=-1){
			Set<String> tagSet = new LinkedHashSet<String>();
	         tagSet.add("1"+lm.getUserId());
	         //registerMessageReceiver();  // used for receive msg
	 	    //调用JPush API设置Tag
	 	    JPushInterface.setAliasAndTags(getApplicationContext(), null, tagSet, this);
		}
		
	   
			
		  Timer timer = new Timer();
		    TimerTask task = new TimerTask(){
	            public void run(){
	            	Intent intent = new Intent(StartActivity.this,MainTabActivity.class);
	            	startActivity(intent);
	            	finish();
	            }
		    };
		    timer.schedule(task, 1000*1);
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


	
}
