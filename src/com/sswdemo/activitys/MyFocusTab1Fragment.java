package com.sswdemo.activitys;

import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.sswdemo.adapters.ServeListAdapter;
import com.sswdemo.base.AppParameters;
import com.sswdemo.beans.ServeListBean;
import com.sswdemo.beans.ServeListDataBean;
import com.sswdemo.helper.HttpTools;
import com.sswdemo.helper.QHttpClient;
import com.sswdemo.servcie.LoginManager;
import com.sswdemo.views.PullToRefreshView;
import com.sswdemo.views.PullToRefreshView.OnFooterRefreshListener;
import com.sswdemo.views.PullToRefreshView.OnHeaderRefreshListener;


/******************************************************
 * 我的课程
 *
 */
public class MyFocusTab1Fragment extends Fragment implements OnHeaderRefreshListener, OnFooterRefreshListener, OnItemClickListener {

	private PullToRefreshView mPullToRefreshView;
	private long mPage = 1;
	//private LoadNetworkAsync mAsync;
	 private ListView servelist;
	 private ServeListAdapter mServeListAdapter;
	 //private Dialog mProgressDialog;
	 private LoginManager lm;
	 
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.myfocus_tab1, null);
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		View view = getView();
		servelist = (ListView)view.findViewById(R.id.serve_list);
		mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh_view);
		/*mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		mPullToRefreshView.headerRefreshing();*/
		lm = new LoginManager(getActivity());
		new ServeListAsyncTask(String.valueOf(lm.getUserId()),"0").execute();
	}

	/******************************************************************************
	 * 1.16我的关注(商家)
	 */
	private class ServeListAsyncTask extends AsyncTask<Void, Void,  ServeListBean> {
		private String _user_id;
		private String _type;

		public ServeListAsyncTask(String user_id, String type) {
			this._user_id = user_id;
			this._type = type;
		}
		
		protected ServeListBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			ServeListBean mServeListBean = null;
			try { 
				
				String request = http.httpGet(AppParameters.getInstance().url_getFocusServeList(),
						"user_id/"+_user_id+"/type/"+_type);

				Gson gson = new Gson();
			    mServeListBean = gson.fromJson(request, ServeListBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mServeListBean;
		}
	    protected void onPreExecute() {  
	    /*  mProgressDialog = new Dialog(getActivity(),R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();*/
			//检查网络
			if(!HttpTools.checkNetwork(getActivity())){
				//mProgressDialog.dismiss();
				Toast.makeText(getActivity(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
        }  
	    
		protected void onPostExecute(ServeListBean mServeListBean) {
			//mProgressDialog.dismiss();
			mPullToRefreshView.onHeaderRefreshComplete();
			mPullToRefreshView.onFooterRefreshComplete();
			if (mServeListBean != null) {
				if(mServeListBean.getRes() == 1)
			    	Toast.makeText(getActivity(), "请求数据失败！", Toast.LENGTH_LONG).show();
			    else
				    initListView(mServeListBean.getData());
			}
		}	
	}
	
	 /******************************************************************
	  * 初始化ListView数据
	  */
	 public void initListView(final List<ServeListDataBean> listData){
		  
	    	mServeListAdapter = new ServeListAdapter(getActivity(),listData);
	    	servelist.setAdapter(mServeListAdapter);
	    	servelist.setOnItemClickListener(new OnItemClickListener(){
				public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
					/*Intent intent = new Intent(MyFocusTab1Activity.this,MerchantDetailsActivity.class);
					intent.putExtra("_shop_id", listData.get(position-1).getShop_id());
					startActivity(intent);*/
				}
	    		
	    	});
	   
	    }
	 
	/*	private void loadNetWork(long page) {
			if (mAsync != null) {
				mAsync.cancel(true);
			}
			mAsync = new LoadNetworkAsync(page);
			mAsync.execute();
		}*/
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.onHeaderRefreshComplete();
		mPullToRefreshView.onFooterRefreshComplete();
		
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPullToRefreshView.onHeaderRefreshComplete();
		mPullToRefreshView.onFooterRefreshComplete();
		
	}
}
