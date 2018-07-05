
package com.sswdemo.activitys;
import java.util.List;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.sswdemo.adapters.GoodsListAdapter;
import com.sswdemo.base.AppParameters;
import com.sswdemo.beans.OrderBean;
import com.sswdemo.beans.GoodListsDataBean;
import com.sswdemo.beans.RecommendedInfoBean;
import com.sswdemo.beans.RecommendedInfoDataBean;
import com.sswdemo.helper.HttpTools;
import com.sswdemo.helper.QHttpClient;
import com.sswdemo.views.MyListView;
import com.sswdemo.views.MyListView.OnRefreshListener;
import com.sswdemo.views.PopMenu;
import com.sswdemo.views.PopMenuNoImg;

/*************************************************************************
 * 商品列表Activity
 * xuena.ni
 * @since 2013-6-6
 */
public class GoodsListActivity extends Activity implements OnClickListener{
	private MyListView servelist;
	private LinearLayout ClassifyBtn;  //分类按钮
	private LinearLayout SortBtn;  //排序按钮
	private PopMenuNoImg ClassifypopMenu;
	private PopMenu SortpopMenu;
	private GoodsListAdapter mGoodsListAdapter;
	private ImageButton BackBtn;
	private Button Go_order;
	private List<RecommendedInfoDataBean> listDatas;  
	private Dialog mProgressDialog;
	private TextView titleName; //标题名字
	private String shopname;
	private static String _typeid = "0";
	private static String _order = "0";
	private  String _shop_id;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.goodslist);
		Intent intent = getIntent();
		_shop_id = intent.getStringExtra("_shop_id");  
		shopname = intent.getStringExtra("_shop_name");  
		initView();
		initPopView();
		new GoodsListsAsyncTask(_shop_id).execute();
	}

	/*****************************************************************
	 * 初始化popview
	 */
	public void initPopView(){
		ClassifypopMenu = new PopMenuNoImg(this);
		SortpopMenu = new PopMenu(this);
		SortpopMenu.addData(new int[]{R.drawable.pop1_0,R.drawable.pop1_1,R.drawable.pop1_2}, new String[] { "默认排序", "人气最高", "离我最近"});
		// 菜单项点击监听器
		ClassifypopMenu.setOnItemClickListener(popmenuItemClickListener);
		SortpopMenu.setOnItemClickListener(popmenuItem_orderClickListener);
	}

	/***************************************************************
	 * 初始化控件
	 */
	public void initView(){
		ClassifyBtn = (LinearLayout)findViewById(R.id.left_relative);	
		SortBtn = (LinearLayout)findViewById(R.id.right_relative);
		ClassifyBtn.setOnClickListener(this);
		SortBtn.setOnClickListener(this);
		BackBtn = (ImageButton)findViewById(R.id.back_btn);
		BackBtn.setOnClickListener(this);
		/*goOrder = (Button)findViewById(R.id.go_order);
		goOrder.setOnClickListener(this);*/
		titleName = (TextView)findViewById(R.id.title_name);
		titleName.setText(shopname);

		Go_order = (Button)findViewById(R.id.go_order);
		Go_order.setOnClickListener(this);

	}

	/******************************************************************
	 * 初始化ListView控件
	 */
	public void initListView(final List<GoodListsDataBean> listData){
		servelist = (MyListView)findViewById(R.id.serve_list);
		mGoodsListAdapter = new GoodsListAdapter(this,listData);
		servelist.setAdapter(mGoodsListAdapter);
		servelist.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
				//					Intent intent = new Intent(GoodsListActivity.this,GoodDetailsActivity.class);
				//					startActivity(intent);
				Intent intent = new Intent(GoodsListActivity.this, ProductDetailsActivity.class);
				Log.d("TAG", "listData.get((int) arg3).getGoods_id(): " + listData.get((int) arg3).getGoods_id());
				intent.putExtra("goods_id", listData.get((int) arg3).getGoods_id());
				startActivity(intent);
			}

		});
		servelist.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				servelist.onRefreshComplete();
				/*	new AsyncTask<Void, Void, Void>() {
						protected Void doInBackground(Void... params) {
							try {
								Thread.sleep(1000);
							} catch (Exception e) {
								e.printStackTrace();
							}
							listData.add("刷新后添加的内容");
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							mServeListAdapter.notifyDataSetChanged();
							servelist.onRefreshComplete();
						}

					}.execute(null);*/
			}
		});
	}

	/******************************************************************************
	 * 1.3获取某商家商品默认列表接口（从某商家页面跳转至商品列表时调用）
	 */
	private class GoodsListsAsyncTask extends AsyncTask<Void, Void,  OrderBean> {
		private String _shop_id;

		public GoodsListsAsyncTask(String shop_id) {
			this._shop_id = shop_id;
		}
		protected OrderBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			OrderBean mGoodListsBean = null;
			try { 
				String request = http.httpGet(AppParameters.getInstance().url_getGoodLists()+"/shop_id",_shop_id);
				Gson gson = new Gson();
				mGoodListsBean = gson.fromJson(request, OrderBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mGoodListsBean;
		}
		
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(GoodsListActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		protected void onPostExecute(OrderBean mGoodListsBean) {
			mProgressDialog.dismiss();
			if (mGoodListsBean.getData().size() != 0) {
				if(mGoodListsBean.getRes() == 1)
					Toast.makeText(getApplicationContext(), "请求数据失败！", Toast.LENGTH_LONG).show();
				else
					initListView(mGoodListsBean.getData());
			}else{
				Toast.makeText(getApplicationContext(), "抱歉，此商店没有数据！", Toast.LENGTH_LONG).show();
			}
		}
	}

	
	/******************************************************************************
	 * 1.4 获取某商家商品分类接口（分类推送）
	 */
	private class RecommendedInfoAsyncTask extends AsyncTask<Void, Void,  RecommendedInfoBean> {
		private String _shop_id;

		public RecommendedInfoAsyncTask(String shop_id) {
			this._shop_id = shop_id;
		}
		protected RecommendedInfoBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			RecommendedInfoBean mRecommendedInfoBean = null;
			try { 
				String request = http.httpGet(AppParameters.getInstance().url_getRecommendeds()+"/shop_id",_shop_id);
				Gson gson = new Gson();
				mRecommendedInfoBean = gson.fromJson(request, RecommendedInfoBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mRecommendedInfoBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(GoodsListActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		protected void onPostExecute(RecommendedInfoBean mRecommendedInfoBean) {
			mProgressDialog.dismiss();
			if (mRecommendedInfoBean != null) {
					if(mRecommendedInfoBean.getRes() == 1)
						Toast.makeText(getApplicationContext(), "请求数据失败！", Toast.LENGTH_LONG).show();
					else{
						listDatas = mRecommendedInfoBean.getData();
						ClassifypopMenu.addData(mRecommendedInfoBean.getData());
					}
			}else{
				Toast.makeText(getApplicationContext(), "没有数据！", Toast.LENGTH_LONG).show();
			}
		}
	}


	// 弹出菜单监听器
	OnItemClickListener popmenuItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			//System.out.println("下拉菜单点击" + position);
			ClassifypopMenu.dismiss();
			if(listDatas.size() != 0){
				_typeid = listDatas.get(position).getTypeid();
				new RecommendedInfoListAsyncTask(_typeid,_order).execute();
			}else
				return;
			
		}
	};

	// 弹出菜单监听器
	OnItemClickListener popmenuItem_orderClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			SortpopMenu.dismiss();
			switch (position) {
			case 0:
				_order = "0";
				break;
			case 1:
				_order = "1";
				break;
			case 2:
				_order = "3";
				break;

			default:
				break;
			}
			if(listDatas!=null){
				_typeid = listDatas.get(position).getTypeid();
				new RecommendedInfoListAsyncTask(_typeid,_order).execute();	
			}else
				return;
			
		}
	};

	/******************************************************************************
	 * 1.5 获取某商家商品（分类）列表接口（在某商家商品列表调用）
	 */
	private class RecommendedInfoListAsyncTask extends AsyncTask<Void, Void,  OrderBean> {
		private String  _typeid;
		private String  _order;


		public RecommendedInfoListAsyncTask(String typeid,String order) {
			this._typeid = typeid;
			this._order = order;
		}
		protected OrderBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			OrderBean mGoodListsBean = null;
			try { 
				String request = http.httpGet(AppParameters.getInstance().url_getRecommendedInfoList(),"/typeid/"+_typeid+"/order/"+_order);
				Gson gson = new Gson();
				mGoodListsBean = gson.fromJson(request, OrderBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mGoodListsBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(GoodsListActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		protected void onPostExecute(OrderBean mGoodListsBean) {
			mProgressDialog.dismiss();
			if (mGoodListsBean != null) {
				if(mGoodListsBean.getRes() == 1)
					Toast.makeText(getApplicationContext(), "请求数据失败！", Toast.LENGTH_LONG).show();
				else
					initListView(mGoodListsBean.getData());

			}
		}
	}

	/*******************************************************************
	 * 控件点击事件
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_relative:
			new RecommendedInfoAsyncTask(_shop_id).execute();
			ClassifypopMenu.showAsDropDown(v);
			break;
		case R.id.right_relative:
			SortpopMenu.showAsDropDown(v);
			break;
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.go_order: //去结算
		Intent intent_go = new Intent(GoodsListActivity.this,ShoppingCartActivity.class);
		GoodsListActivity.this.startActivity(intent_go);
		break;

		default:
			break;
		}
	}
}
