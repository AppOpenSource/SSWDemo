package com.sswdemo.activitys;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sswdemo.adapters.OrderDetailsListsAdapter;
import com.sswdemo.base.AppParameters;
import com.sswdemo.beans.OrderDetailsBean;
import com.sswdemo.helper.HttpTools;
import com.sswdemo.helper.QHttpClient;

/**********************************************************
 * 订单详情
 * @author xuena.ni
 *
 */
public class OrderDetailsActivity extends Activity implements OnClickListener {
	private Dialog mProgressDialog;
	private ListView OrderLists; 
	private TextView OrderStatus; //订单状态
	private TextView Delivery; //配送方式
	private TextView Numbercourier; //快递员编号
	private TextView Courierphone; //快递员手机
	private TextView fromShop;//来自哪个店
	private TextView Total;//小计
	private TextView Consignee;//收货人
	private TextView Consigneeaddress;//收货人地址
	private TextView OrderNumber;// 订单编号
	private TextView SignPeople; //签收人员
	private TextView SingTime;   //签收时间
	private RelativeLayout SignLinear;//签收跳转
	private String orderid1;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderdetails_layout);
		findViewById(R.id.back_btn).setOnClickListener(this);
		initView();
		Intent inte = getIntent();     
		orderid1 = inte.getStringExtra("orderid");   
		new OrderDetailsListsAsyncTask(orderid1).execute();
	}

	/***********************************************************************
	 * 初始化控件
	 */
	public void initView(){
		OrderLists = (ListView)findViewById(R.id.dingdan_lists);
		OrderStatus = (TextView)findViewById(R.id.dingdanzhuangtai);
		Delivery = (TextView)findViewById(R.id.peisongfangshi);
		Numbercourier = (TextView)findViewById(R.id.bianhao);
		Courierphone = (TextView)findViewById(R.id.dianhua);
		fromShop = (TextView)findViewById(R.id.dianmian);
		Total = (TextView)findViewById(R.id.xiaoji);
		Consignee = (TextView)findViewById(R.id.shouhuoren);
		Consigneeaddress = (TextView)findViewById(R.id.shouhuorendizhi);
		OrderNumber = (TextView)findViewById(R.id.didanbianhao);
		SignPeople = (TextView)findViewById(R.id.qianshourenyuan);
		SingTime = (TextView)findViewById(R.id.qianshoushijian);
		SignLinear = (RelativeLayout)findViewById(R.id.sign_linear);
		SignLinear.setOnClickListener(this);
	}
	/******************************************************************************
	 * 1.19订单详情
	 */
	private class OrderDetailsListsAsyncTask extends AsyncTask<Void, Void,  OrderDetailsBean> {
		private String _order_id;

		public OrderDetailsListsAsyncTask(String order_id) {
			this._order_id = order_id;
		}
		protected OrderDetailsBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			OrderDetailsBean mOrderDetailsBean = null;
			try { 
				String request = http.httpGet(AppParameters.getInstance().url_getOrderDetailsLists()+"/order_id",_order_id);
				Gson gson = new Gson();
				mOrderDetailsBean = gson.fromJson(request, OrderDetailsBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mOrderDetailsBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(OrderDetailsActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  

		protected void onPostExecute(OrderDetailsBean mOrderDetailsBean) {
			mProgressDialog.dismiss();
			if (mOrderDetailsBean != null) {
				initData(mOrderDetailsBean);
			}
		}
	}

	/**************************************************************
	 * 初始化数据
	 * @param orderDetailsBean
	 */
	private void initData(OrderDetailsBean orderDetailsBean){
		OrderStatus.setText(getStatus(orderDetailsBean.getOrder_status()));
		Delivery.setText(orderDetailsBean.getOrder_status());
		Numbercourier.setText(orderDetailsBean.getAction_user_id());
		Courierphone.setText(orderDetailsBean.getPhone());
		fromShop.setText("来自物美大卖场");
		SignPeople.setText(orderDetailsBean.getAction_info());
		SingTime.setText(orderDetailsBean.getLogtime());
		Total.setText(orderDetailsBean.getOrder_total());
		Consignee.setText(orderDetailsBean.getConsignee());
		Consigneeaddress.setText(orderDetailsBean.getAddress());
		OrderNumber.setText(orderDetailsBean.getOrder_sn());
		OrderDetailsListsAdapter mOrderDetailsListsAdapter = new 
				OrderDetailsListsAdapter(this,orderDetailsBean.getGoods());
		OrderLists.setAdapter(mOrderDetailsListsAdapter);

	}
	/*3：已收货，2：已发货，1：等待发货，0：订单确认*/
	private String getStatus(String statuse){
		if(statuse.equals("0")){
			return "订单确认";
		}
		if(statuse.equals("1")){
			return "等待发货";
		}
		if(statuse.equals("2")){
			return "已发货";
		}
		if(statuse.equals("3")){
			return "订单确认";
		}
		return "未知状态";

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sign_linear:
          Intent intent = new Intent(OrderDetailsActivity.this,DeliveryInfoActivity.class);
          intent.putExtra("orderid", orderid1);
          startActivity(intent);
			break;
		case R.id.back_btn:
			this.finish();

			break;


		default:
			break;
		}
	}


}
