package com.sswdemo.activitys;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.sswdemo.base.AppParameters;
import com.sswdemo.beans.MyInfoBean;
import com.sswdemo.beans.MyInfoData;
import com.sswdemo.helper.HttpTools;
import com.sswdemo.helper.QHttpClient;
import com.sswdemo.model.Product;
import com.sswdemo.model.Shop;
import com.sswdemo.servcie.LoginManager;
import com.sswdemo.servcie.ShoppingCartDBService;
import com.sswdemo.servcie.SubmitOrderHttpService;

/*****************************************************************
 * 提交订单
 *
 */
public class SubmitOrdersActivity extends Activity implements OnClickListener , TextWatcher,OnCheckedChangeListener{

	private static final int SUBMIT_ORDERS_ADDRESS = 100;
	private static final int SUBMIT_ORDERS_INVOICE = 101;
	protected static final int FINISH = 0;
	protected static final int SUBMIT_ORDER_ERROR = 1;
	private static final int SUBMIT_ORDERS_MESSAGE = 102;
	private static final int SUBMIT_ORDERS_PAYFOR = 103;
	private TextView mTotal;
	private Button mSubmit;
	private List<Shop> shops;
	private TextView title;
	private TextView price;
	private RelativeLayout submitOrderAddrRelative;
	private TextView submitOrderName; //收货人
	private TextView submitOrderPhone;
	private TextView submitOrderAddr;
	private TextView submitOrderMobile;
	private TextView payfor_text;
	private RelativeLayout invoiceLinear; //发票
	private RelativeLayout Payfor_relative; //支付方式
	private TextView content;
	private TextView remark;
	private RelativeLayout remarkRelative;
	private Dialog mProgressDialog;
	private LoginManager lm;
	private View view;
	private String fapiaotrue = "0"; //fapiao
	private String peisongfangshi = "0";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_order);
		findViewById(R.id.back_btn).setOnClickListener(this);
		initView();
		dealIntent();
		lm = new LoginManager(this);
		if(lm.getUserId()==-1){
			Intent i = new Intent(SubmitOrdersActivity.this, LoginActivity.class);
			startActivityForResult(i, 0);
			return;	
		}
		new GetMyInfoAsyncTask(String.valueOf(lm.getUserId())).execute();
	}


	private void initView() {
		
		submitOrderAddrRelative = (RelativeLayout)findViewById(R.id.sumit_order_addr_relative);
		submitOrderAddrRelative.setOnClickListener(this);
		submitOrderName = (TextView)findViewById(R.id.submit_order_name);
		submitOrderPhone = (TextView)findViewById(R.id.submit_order_phone);
		submitOrderMobile = (TextView)findViewById(R.id.submit_order_mobile);
		submitOrderAddr = (TextView)findViewById(R.id.submit_order_addr);
		payfor_text = (TextView)findViewById(R.id.payfor_tex);
		invoiceLinear = (RelativeLayout)findViewById(R.id.invoice);
		invoiceLinear.setOnClickListener(this);

		
		title = (TextView)findViewById(R.id.title_of_invoice);
		content = (TextView)findViewById(R.id.content_of_invoice);
		price = (TextView)findViewById(R.id.total_price);
		remark = (TextView)findViewById(R.id.remark);

		remarkRelative = (RelativeLayout)findViewById(R.id.remark_relative);
		remarkRelative.setOnClickListener(this);

		mSubmit = (Button)findViewById(R.id.submit_order);
		mSubmit.setOnClickListener(this);

		view = (View)findViewById(R.id.line);
		RadioGroup group = (RadioGroup)this.findViewById(R.id.fapiao_rg);
		//绑定一个匿名监听器
		group.setOnCheckedChangeListener(this);

		RadioGroup group1 = (RadioGroup)this.findViewById(R.id.peisongfangshi_rg);
		//绑定一个匿名监听器
		group1.setOnCheckedChangeListener(this);

		Payfor_relative = (RelativeLayout)findViewById(R.id.payfor_relative);
		Payfor_relative.setOnClickListener(this);
	}

	/***********************************************************************
	 * 1.17订单提交后获取用户信息
	 */
	private class GetMyInfoAsyncTask extends AsyncTask<Void, Void,  MyInfoBean> {
		private String _user_id;

		public GetMyInfoAsyncTask(String user_id){
			this._user_id = user_id;
		}

		protected MyInfoBean doInBackground(Void... params) {
			QHttpClient http = new QHttpClient();
			MyInfoBean mMyInfoBean = null;
			try { 
				String request = http.httpGet(AppParameters.getInstance().url_getMyInfo()+"/user_id",_user_id);
				Gson gson = new Gson();
				mMyInfoBean = gson.fromJson(request, MyInfoBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mMyInfoBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(SubmitOrdersActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		protected void onPostExecute(MyInfoBean mMyInfoBean) {
			mProgressDialog.dismiss();
			if (mMyInfoBean != null) {
				submitOrderName.setText(mMyInfoBean.getConsignee());
				submitOrderAddr.setText(mMyInfoBean.getAddress());
				submitOrderPhone.setText(mMyInfoBean.getTel());
				submitOrderMobile.setText(mMyInfoBean.getMobile());
			}
		}
	}

	private void dealIntent() {
		Intent intent = getIntent();
		if(intent == null) return;
		shops = (ArrayList<Shop>) intent.getSerializableExtra("shops");
		if(shops == null || shops.size() == 0 ) return;
		Shop shop = shops.get(0);
		List<Product> pList = shop.getProductList();
		float price = 0;
		for(Product p: pList){
			price += p.getPrice()*p.getCount();
		}
		this.price.setText(price+"");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sumit_order_addr_relative://收货人信息
			Intent i = new Intent(SubmitOrdersActivity.this, SubmitOrdersAddrActivity.class);
			startActivityForResult(i, SUBMIT_ORDERS_ADDRESS);
			break;
		case R.id.back_btn://返回按钮
			this.finish();
			break;
		case R.id.submit_order://提交订单按钮
			if(shops == null || shops.size()== 0) return;
			//写入订单信息
			SetOrdersData();
			new SubmitOrderAsyncTask(shops).execute();
			break;
		case R.id.invoice: //发票信息
			Intent i1 = new Intent(SubmitOrdersActivity.this, SubmitOrdersInvoiceActivity.class);
			startActivityForResult(i1, SUBMIT_ORDERS_INVOICE);
			break;
		case R.id.remark_relative://订单附言
			Intent i11 = new Intent(SubmitOrdersActivity.this, SubmitOrdersMessageActivity.class);
			startActivityForResult(i11, SUBMIT_ORDERS_MESSAGE);
			break;
		case R.id.payfor_relative:
			Intent i111 = new Intent(SubmitOrdersActivity.this, PayforTypeActivity.class);
			startActivityForResult(i111, SUBMIT_ORDERS_PAYFOR);
			break;
		}
	}

	/**
	 * 写入信息
	 */
	public void SetOrdersData(){
		MyInfoData mMyInfoData = MyInfoData.getInstance();
		mMyInfoData.setConsignee(submitOrderName.getText().toString());
		mMyInfoData.setMobile(submitOrderMobile.getText().toString());
		mMyInfoData.setTel(submitOrderPhone.getText().toString());
		mMyInfoData.setAddress(submitOrderAddr.getText().toString());
		mMyInfoData.setAddress(submitOrderAddr.getText().toString());
		if(!title.getText().toString().equals("")){
			if(title.getText().toString().equals("个人"))
				fapiaotrue = "1";
			else 
				fapiaotrue = "2";
		}else{
			fapiaotrue = "0";
		}
		mMyInfoData.setIs_need_inv(fapiaotrue);	
		if(title.getText().toString()!=null && !title.getText().toString().equals("个人")){
			mMyInfoData.setInv_payee(title.getText().toString());
		}
		mMyInfoData.setShipping_id(peisongfangshi);
		mMyInfoData.setPostscript(remark.getText().toString());

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
		case RESULT_OK:
			if(requestCode == SUBMIT_ORDERS_ADDRESS){
				String consignee = data.getStringExtra("consignee");
				String addr = data.getStringExtra("addr");
				String mobile = data.getStringExtra("mobile");
				String phone = data.getStringExtra("phone");

				if(consignee != null ) submitOrderName.setText(consignee);
				if(addr != null ) submitOrderAddr.setText(addr);
				if(phone != null ) submitOrderPhone.setText(phone);
				if(mobile != null ) submitOrderMobile.setText(mobile);
			}
			if(requestCode == SUBMIT_ORDERS_INVOICE){
				String type = data.getStringExtra("invoice_type");
				String content = data.getStringExtra("invoice_content");
				if(type != null ) title.setText(type);
				if(content != null ) this.content.setText(content);
			}
			if(requestCode == SUBMIT_ORDERS_MESSAGE){
				String message = data.getStringExtra("message");

				if(message != null ) remark.setText(message);
			}
			if(requestCode == SUBMIT_ORDERS_PAYFOR){
				String payfor = data.getStringExtra("payfor_type");
				if(payfor != null ) payfor_text.setText(payfor);
			}
			break;
		default:
			break;
		}
	}

	class SubmitOrderAsyncTask extends AsyncTask<Void, Void, Boolean> {
		private List<Shop> shops;

		public SubmitOrderAsyncTask(List<Shop> shops) {
			super();
			this.shops = shops;
		}

		protected Boolean doInBackground(Void... params) {
			SubmitOrderHttpService sohs = new SubmitOrderHttpService(SubmitOrdersActivity.this);
			boolean res = sohs.submitOrder(shops);
			if(res){
				ShoppingCartDBService sdbs = new ShoppingCartDBService(SubmitOrdersActivity.this);
				try {
					sdbs.deleteByOrder(shops);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return res;
		}

		protected void onPreExecute() { 
			/*	submitOrderAddr.setText(mMyInfoBean.getAddress());
			submitOrderPhone.setText(mMyInfoBean.getTel());
			submitOrderMobile.setText(mMyInfoBean.getMobile());*/
			/*MyInfoData mMyInfoData = MyInfoData.getInstance();
			mMyInfoData.setConsignee(submitOrderName.getText().toString());
			mMyInfoData.setAddress(submitOrderAddr.getText().toString());
			mMyInfoData.setTel(submitOrderPhone.getText().toString());
			mMyInfoData.setMobile(submitOrderMobile.getText().toString());*/

		}  
		protected void onPostExecute(Boolean res) {
			if(res){
				Intent intent = new Intent(SubmitOrdersActivity.this, SubmitOrdersSuccessActivity.class);
				//	intent.putExtra("order_no", shops.get(0).get);
				startActivity(intent);
				//	Toast.makeText(SubmitOrdersActivity.this, "提交订单成功!", Toast.LENGTH_SHORT).show();
				finish();
			}else{
				Toast.makeText(SubmitOrdersActivity.this, "提交订单出错!", Toast.LENGTH_SHORT).show(); 
			}

		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {}

	@Override
	public void afterTextChanged(Editable s) {}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(!TextUtils.isEmpty(s)) {
			try {
				float c = Float.valueOf(s.toString());
				c = c* Float.parseFloat(price.getText().toString());
				mTotal.setText(c+"元");
				mSubmit.setEnabled(true);
			} catch (Exception e) {
			}
		} else {
			mSubmit.setEnabled(false);
			mTotal.setText("0元");
		}
	}


	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (group.getId()) {
		case R.id.fapiao_rg:
			// TODO Auto-generated method stub
			//获取变更后的选中项的ID
			int radioButtonId = group.getCheckedRadioButtonId();
			//根据ID获取RadioButton的实例
			RadioButton rb = (RadioButton)findViewById(radioButtonId);
			if(rb.getText().equals("普通发票")){
				view.setVisibility(View.VISIBLE);
				invoiceLinear.setVisibility(View.VISIBLE);
			}else if(rb.getText().equals("不开发票")){
				fapiaotrue = "0";
				view.setVisibility(View.GONE);
				invoiceLinear.setVisibility(View.GONE);
			}
			break;
		case R.id.peisongfangshi_rg:
			//获取变更后的选中项的ID
			int radioButtonId1 = group.getCheckedRadioButtonId();
			//根据ID获取RadioButton的实例
			RadioButton rb1 = (RadioButton)findViewById(radioButtonId1);
			if(rb1.getText().equals("送货上门")){
				peisongfangshi = "0";
			}else if(rb1.getText().equals("用户自取")){
				peisongfangshi = "1";
			}
			break;

		default:
			break;
		}

	}



}
