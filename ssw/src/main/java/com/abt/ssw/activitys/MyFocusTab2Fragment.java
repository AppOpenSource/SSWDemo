package com.abt.ssw.activitys;

import java.util.List;


import android.app.Dialog;
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
import com.abt.ssw.adapters.GoodsListAdapter;
import com.abt.ssw.base.AppParameters;
import com.abt.ssw.beans.GoodListsDataBean;
import com.abt.ssw.beans.OrderBean;
import com.abt.ssw.helper.HttpTools;
import com.abt.ssw.helper.QHttpClient;
import com.abt.ssw.servcie.LoginManager;
import com.abt.ssw.views.PullToRefreshView;
import com.abt.ssw.views.PullToRefreshView.OnFooterRefreshListener;
import com.abt.ssw.views.PullToRefreshView.OnHeaderRefreshListener;


/**
 * 我的课程
 *
 */
public class MyFocusTab2Fragment extends Fragment implements 
OnHeaderRefreshListener, OnFooterRefreshListener, OnItemClickListener {

	private PullToRefreshView mPullToRefreshView;
	private long mPage = 1;
	//private LoadNetworkAsync mAsync;
	private ListView servelist;
	private GoodsListAdapter mGoodsListAdapter;
	private Dialog mProgressDialog;
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
		new ServeListAsyncTask(String.valueOf(lm.getUserId()),"1").execute();
	}

	/******************************************************************************
	 * 1.16我的关注(商家)
	 */
	private class ServeListAsyncTask extends AsyncTask<Void, Void,  OrderBean> {
		private String _user_id;
		private String _type;

		public ServeListAsyncTask(String user_id, String type) {
			this._user_id = user_id;
			this._type = type;
		}

		protected OrderBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			OrderBean mOrderBean = null;
			try { 

				String request = http.httpGet(AppParameters.getInstance().url_getFocusServeList(),
						"user_id/"+_user_id+"/type/"+_type);

				Gson gson = new Gson();
				mOrderBean = gson.fromJson(request, OrderBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mOrderBean;
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

		protected void onPostExecute(OrderBean mOrderBean) {
			//mProgressDialog.dismiss();
			mPullToRefreshView.onHeaderRefreshComplete();
			mPullToRefreshView.onFooterRefreshComplete();
			if (mOrderBean != null) {
				if(mOrderBean.getRes() == 1)
					Toast.makeText(getActivity(), "请求数据失败！", Toast.LENGTH_LONG).show();
				else
					initListView(mOrderBean.getData());
			}
		}	
	}

	/******************************************************************
	 * 初始化ListView数据
	 */
	public void initListView(final List<GoodListsDataBean> listData){

		mGoodsListAdapter = new GoodsListAdapter(getActivity(),listData);
		servelist.setAdapter(mGoodsListAdapter);
		servelist.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
				/*Intent intent = new Intent(MyFocusTab1Activity.this,MerchantDetailsActivity.class);
					intent.putExtra("_shop_id", listData.get(position-1).getShop_id());
					startActivity(intent);*/
			}

		});

	}


	@Override
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
