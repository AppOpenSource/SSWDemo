package com.sswdemo.activitys;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.readystatesoftware.viewbadger.BadgeView;
import com.sswdemo.activitys.ServeActivity.ServeListAsyncTask;
import com.sswdemo.base.AppParameters;
import com.sswdemo.beans.MessageAccountBean;
import com.sswdemo.helper.QHttpClient;
import com.sswdemo.servcie.LoginManager;
/**
 * 防新浪微博底部工具栏的TabActivity
 * @author xuena.ni
 */
public class MainTabActivity extends TabActivity implements OnCheckedChangeListener{
	private RadioGroup mainTab;
	private TabHost mTabHost;
	private BadgeView badge;

	//private RadioButton mRadioButton;
	//内容Intent
	private Intent mHomeIntent;
	private Intent mNewsIntent;
	private Intent mInfoIntent;
	private Intent mSearchIntent;
	private Intent mMoreIntent;
	private LoginManager lm;
	private final static String TAB_TAG_HOME="tab_tag_home";
	private final static String TAB_TAG_NEWS="tab_tag_news";
	private final static String TAB_TAG_INFO="tab_tag_info";
	private final static String TAB_TAG_SEARCH="tab_tag_search";
	private final static String TAB_TAG_MORE="tab_tag_more";
	
//	//default tab selection
//	private static final int INIT_SELECT = 0;
//	//anim execute delay time
//	private static final long DELAY_TIME = 500;
	private TextView badgeViewTv;
	public static final int PAGE_COUNT = 5;
	public static final int JUMP_TO_TAB_2 = 0;

	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case JUMP_TO_TAB_2:
				mTabHost.setCurrentTabByTag(TAB_TAG_NEWS);
				((RadioButton) mainTab.getChildAt(1)).setChecked(true);
				break;
			}
		};
	};
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        mainTab=(RadioGroup)findViewById(R.id.main_tab);
        badgeViewTv=(TextView)findViewById(R.id.badge_view_message);
        mainTab.setOnCheckedChangeListener(this);
        prepareIntent();
        setupIntent();
//	    initCurSelectTab();
        HomeActivity.registerMainTabHandler(handler);
        lm = new LoginManager(this);
        if(lm.getUserId()==-1){
			
			return;	
		}
		new MessageAcountAsyncTask(String.valueOf(lm.getUserId())).execute();
    }
    
    /**
     * 准备tab的内容Intent
     */
	private void prepareIntent() { 
		mHomeIntent=new Intent(this, HomeActivity.class);
		mNewsIntent=new Intent(this, ServeActivity.class);
		mInfoIntent=new Intent(this, MessageActivity.class);
//		mInfoIntent=new Intent(this, ProductDetailsActivity.class);
		mSearchIntent=new Intent(this,CommunityActivity.class);
		mMoreIntent=new Intent(this, MoreActivity.class);
	}
	
	/**
	 * 
	 */
	private void setupIntent() {
		this.mTabHost=getTabHost();
		TabHost localTabHost=this.mTabHost;
		localTabHost.addTab(buildTabSpec(TAB_TAG_HOME, R.string.main_home, R.drawable.icon_1_n, mHomeIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_NEWS, R.string.main_news, R.drawable.icon_2_n, mNewsIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_INFO, R.string.main_my_info, R.drawable.icon_3_n, mInfoIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_SEARCH, R.string.menu_search, R.drawable.icon_4_n, mSearchIntent));
		localTabHost.addTab(buildTabSpec(TAB_TAG_MORE, R.string.more, R.drawable.icon_5_n, mMoreIntent));
		float width = this.getWindowManager().getDefaultDisplay().getWidth();
		badgeViewTv.setWidth((int)(width / PAGE_COUNT));
		badge = new BadgeView(this,badgeViewTv);

	}
	
	/***********************************************************************
	 * 2.1获取消息数量
	 */
	private class MessageAcountAsyncTask extends AsyncTask<Void, Void,  MessageAccountBean> {
		private String _user_id;

		public MessageAcountAsyncTask(String user_id){
			this._user_id = user_id;
		}
		
		protected MessageAccountBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			MessageAccountBean mMessageAccountBean = null;
			try { 
				String request = http.httpGet(AppParameters.getInstance().url_getMessageAccountInfo()+"/user_id",_user_id);
				Gson gson = new Gson();
				mMessageAccountBean = gson.fromJson(request, MessageAccountBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mMessageAccountBean;
		}
		protected void onPreExecute() {  
			/*mProgressDialog = new Dialog(AccountInfoActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}*/
		}  
		protected void onPostExecute(MessageAccountBean mMessageAccountBean) {
			//mProgressDialog.dismiss();
			if (mMessageAccountBean != null) {
				if(mMessageAccountBean.getRes() == 1)
					Toast.makeText(getApplicationContext(), "没有获取消息数量！", Toast.LENGTH_LONG).show();
				else{
					//展示消息数量
				    badge.setText(String.valueOf(mMessageAccountBean.getNum()));
				    badge.show();
				   
				}
			}
		}
	}
	
	/**
	 * 构建TabHost的Tab页
	 * @param tag 标记
	 * @param resLabel 标签
	 * @param resIcon 图标
	 * @param content 该tab展示的内容
	 * @return 一个tab
	 */
	private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon,final Intent content) {
		return this.mTabHost.newTabSpec(tag).setIndicator(getString(resLabel),
				getResources().getDrawable(resIcon)).setContent(content);
	} 
	
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.radio_button0:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_HOME);
			break;
		case R.id.radio_button1:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_NEWS);
			break;
		case R.id.radio_button2:
			badge.hide();
			this.mTabHost.setCurrentTabByTag(TAB_TAG_INFO);
			break;
		case R.id.radio_button3:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_SEARCH);
			break;
		case R.id.radio_button4:
			this.mTabHost.setCurrentTabByTag(TAB_TAG_MORE);
			break;
		}
	}
  
   
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		HomeActivity.registerMainTabHandler(null);
	}

}