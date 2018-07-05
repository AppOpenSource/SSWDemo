package com.sswdemo.activitys;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sswdemo.adapters.MessageListAdapter;
import com.sswdemo.base.AppParameters;
import com.sswdemo.beans.MessageDataInfoBean;
import com.sswdemo.beans.MessageInfoBean;
import com.sswdemo.beans.MyInfoData;
import com.sswdemo.helper.HttpTools;
import com.sswdemo.helper.QHttpClient;
import com.sswdemo.servcie.LoginManager;
import com.sswdemo.views.PullToRefreshView;
import com.sswdemo.views.PullToRefreshView.OnFooterRefreshListener;
import com.sswdemo.views.PullToRefreshView.OnHeaderRefreshListener;

/**
 * 消息的Activity
 * @author xuena.ni
 */
public class MessageActivity extends Activity implements OnHeaderRefreshListener,OnFooterRefreshListener{
	private ListView messageList;
	private LoginManager lm;
	PullToRefreshView mPullToRefreshView;
	private Dialog mProgressDialog;
	private List<MessageDataInfoBean> data;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mila_message);
		mPullToRefreshView = (PullToRefreshView)findViewById(R.id.main_pull_refresh_view);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		lm = new LoginManager(this);
		if(lm.getUserId()==-1){
			Intent i = new Intent(MessageActivity.this, LoginActivity.class);
			startActivityForResult(i, 0);
			MyInfoData.isform_message = true;
			return;	
		}
		new MessageInfoAsyncTask(String.valueOf(lm.getUserId())).execute();
		System.out.println("onCreate!!!!!!!!!!!!!!!!!!!!!!!!!!");

	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { 
		case RESULT_OK://resultCode为回传的标记，我在B中回传的是RESULT_OK
			new MessageInfoAsyncTask(String.valueOf(lm.getUserId())).execute();
			break;
		default:
			break;
		}
	}
	MessageListAdapter mMessageListAdapter;
	public void initListView(List<MessageDataInfoBean> listData){
		messageList = (ListView)findViewById(android.R.id.list);
		//listData = getDataList();
		mMessageListAdapter = new MessageListAdapter(this,listData);
		messageList.setAdapter(mMessageListAdapter);
		
		//Item跳转
		messageList.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,long arg3) {

				if(data.get(position).getType().equals("1")){
					Intent intent = new Intent(MessageActivity.this,OrderDetailsActivity.class);
					intent.putExtra("orderid",data.get(position).getContent().get(0).getOrder_id());
					startActivity(intent);
				}
				if(data.get(position).getType().equals("2")){
					Intent intent = new Intent(MessageActivity.this,MerchantMessageActivity.class);
					intent.putExtra("shopmsgid",data.get(position).getContent().get(0).getShopmsg_id());
					startActivity(intent);
				}
				
				if(data.get(position).getType().equals("3")){
					Intent intent_syetem = new Intent(MessageActivity.this,SystemMessagesWebviewActivity.class);
					intent_syetem.putExtra("noticeid",data.get(position).getContent().get(0).getNotice_id());
					startActivity(intent_syetem);
				}
			}});

	}
	 @Override
	protected void onResume() {
		super.onResume();
	
	}
	 @Override
	protected void onStart() {
		super.onStart();
		System.out.println("onStart!!!!!!!!!!!!!!!!!!!!!!!!!!");
	}
	 @Override
	protected void onRestart() {
		super.onRestart();
	/*	if(MyInfoData.isnot_login){
			Intent i = new Intent(MessageActivity.this, LoginActivity.class);
			startActivityForResult(i, 0);
			MyInfoData.isform_message = true;
			return;	
		}*/
	}

	/***********************************************************************
	 * 2.2消息详情
	 */
	private class MessageInfoAsyncTask extends AsyncTask<Void, Void,  MessageInfoBean> {
		private String _user_id;

		public MessageInfoAsyncTask(String user_id){
			this._user_id = user_id;
		}

		protected MessageInfoBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			MessageInfoBean mMessageInfoBean = null;
			try { 
				String request = http.httpGet(AppParameters.getInstance().url_getMessageInfo()+"/user_id",_user_id);
				Gson gson = new Gson();
				mMessageInfoBean = gson.fromJson(request, MessageInfoBean.class);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			return mMessageInfoBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(MessageActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		protected void onPostExecute(MessageInfoBean mMessageInfoBean) {
			mProgressDialog.dismiss();
			if (mMessageInfoBean != null) {
				if(mMessageInfoBean.getRes() == 1)
					Toast.makeText(getApplicationContext(), "请求数据失败！", Toast.LENGTH_LONG).show();
				else{
					if(mMessageInfoBean.getData().size() != 0){
						data = mMessageInfoBean.getData();
						initListView(data);
					}else{
						Toast.makeText(getApplicationContext(), "抱歉，您还没有消息！", Toast.LENGTH_LONG).show();
					}
				
				}

			}
		}
	}
	
	/***********************************************************************
	 * 2.2之前消息详情
	 */
	private class BeforeMessageInfoAsyncTask extends AsyncTask<Void, Void,  MessageInfoBean> {
		private String _user_id;
		private String _addtime;

		public BeforeMessageInfoAsyncTask(String user_id,String addtime){
			this._user_id = user_id;
			this._addtime = addtime;
		}

		protected MessageInfoBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			MessageInfoBean mMessageInfoBean = null;
			try { 
				String str  = "user_id/"+_user_id+"/addtime/"+_addtime;
				String request = http.httpGet(AppParameters.getInstance().url_getBeforeMessageInfo(),str);
				Gson gson = new Gson();
				mMessageInfoBean = gson.fromJson(request, MessageInfoBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mMessageInfoBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(MessageActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		protected void onPostExecute(MessageInfoBean mMessageInfoBean) {
			mPullToRefreshView.onHeaderRefreshComplete();
			mProgressDialog.dismiss();
			if (mMessageInfoBean != null) {
				if(mMessageInfoBean.getRes() == 1)
					Toast.makeText(getApplicationContext(), "请求数据失败！", Toast.LENGTH_LONG).show();
				else{
					data = getDataBefore(mMessageInfoBean);
					initListView(data);
				}
			}else{
				Toast.makeText(getApplicationContext(), "没有历史消息!", Toast.LENGTH_LONG).show();
			}
		}
	}
	/***********************************************************************
	 * 2.2之后消息详情
	 */
	private class AfterMessageInfoAsyncTask extends AsyncTask<Void, Void,  MessageInfoBean> {
		private String _user_id;
		private String _addtime;

		public AfterMessageInfoAsyncTask(String user_id,String addtime){
			this._user_id = user_id;
			this._addtime = addtime;
		}

		protected MessageInfoBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			MessageInfoBean mMessageInfoBean = null;
			try { 
				String str  = "user_id/"+_user_id+"/addtime/"+_addtime;
				String request = http.httpGet(AppParameters.getInstance().url_getAfterMessageInfo(),str);
				Gson gson = new Gson();
				mMessageInfoBean = gson.fromJson(request, MessageInfoBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mMessageInfoBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(MessageActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		
		
		protected void onPostExecute(MessageInfoBean mMessageInfoBean) {
			mProgressDialog.dismiss();
			mPullToRefreshView.onFooterRefreshComplete();
			if (mMessageInfoBean != null) {
				if(mMessageInfoBean.getRes() == 1)
					Toast.makeText(getApplicationContext(), "请求数据失败！", Toast.LENGTH_LONG).show();
				else{
					// getData(mMessageInfoBean);
					//mMessageListAdapter.notifyDataSetChanged();
					data = getData(mMessageInfoBean);
					initListView(data);
				}
			}else{
				Toast.makeText(getApplicationContext(), "没有最新消息！", Toast.LENGTH_LONG).show();
			}
		}
	}
	
	 public List<MessageDataInfoBean> getDataBefore(MessageInfoBean mMessageInfoBean) {

	    	//List<MessageDataInfoBean> datas = mMessageInfoBean.getData();

	   /*   for(int i = 0;i<mMessageInfoBean.getData().size();i++){
				MessageDataInfoBean mMessageDataInfoBean= mMessageInfoBean.getData().get(i);
				data.add((data.size()+i)+1, mMessageDataInfoBean);
				data.addAll(arg0, arg1)
			}*/

	    	data.addAll(0,mMessageInfoBean.getData());
	        return data;

	    }
    public List<MessageDataInfoBean> getData(MessageInfoBean mMessageInfoBean) {

    	//List<MessageDataInfoBean> datas = mMessageInfoBean.getData();

   /*   for(int i = 0;i<mMessageInfoBean.getData().size();i++){
			MessageDataInfoBean mMessageDataInfoBean= mMessageInfoBean.getData().get(i);
			data.add((data.size()+i)+1, mMessageDataInfoBean);
			data.addAll(arg0, arg1)
		}*/

    	data.addAll(data.size(),mMessageInfoBean.getData());
        return data;

    }

	
	/**
	 * 下面
	 */
	public void onFooterRefresh(PullToRefreshView view) {
		String datetime = data.get(data.size()-1).getAddtime();
		new AfterMessageInfoAsyncTask(String.valueOf(lm.getUserId()),datetime).execute();
		/*mPullToRefreshView.postDelayed(new Runnable() {
			public void run() {
				mPullToRefreshView.onFooterRefreshComplete();
			}
		}, 1000);*/

	}
	
	/**
	 * 上面
	 */
	public void onHeaderRefresh(PullToRefreshView view) {
		String datetime = data.get(0).getAddtime();
		new BeforeMessageInfoAsyncTask(String.valueOf(lm.getUserId()),datetime).execute();
		/*mPullToRefreshView.postDelayed(new Runnable() {
			public void run() {
				//mPullToRefreshView.onHeaderRefreshComplete("鏈�繎鏇存柊:01-23 12:01");
				mPullToRefreshView.onHeaderRefreshComplete();
			}
		},1000);*/
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回键
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(MyInfoData.isfromnotifity == false){
				ConfirmExit();//按了返回键，但已经不能返回，则执行退出确认
			}else{
				this.finish();
				// MyInfoData.isfromnotifity = false;
			}

			return true; 
		}   
		return super.onKeyDown(keyCode, event);   
	}
	public void ConfirmExit(){//退出确认
		AlertDialog.Builder ad=new AlertDialog.Builder(MessageActivity.this);
		ad.setTitle("退出");
		ad.setMessage("是否退出软件?");
		ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按钮
			@Override
			public void onClick(DialogInterface dialog, int i) {
				// TODO Auto-generated method stub
				MessageActivity.this.finish();//关闭activity

			}
		});
		ad.setNegativeButton("否",new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int i) {
				//不退出不用执行任何操作
			}
		});
		ad.show();//显示对话框
	}

}
