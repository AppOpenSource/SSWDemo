package com.sswdemo.activitys;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sswdemo.model.Order;
import com.sswdemo.model.Product;
import com.sswdemo.model.Shop;
import com.sswdemo.servcie.GetOrderHttpService;
import com.sswdemo.servcie.ShoppingCartDBService;
import com.sswdemo.servcie.SubmitOrderHttpService;
import com.sswdemo.utils.DateFormatUtil;

/**
 * 提交订单
 *
 */
public class SubmitOrderActivity extends Activity implements OnClickListener , TextWatcher{

	protected static final int FINISH = 0;
	protected static final int SUBMIT_ORDER_ERROR = 1;
	private TextView mTotal;
	private Button mSubmit;
	private List<Shop> shops;
	private TextView title;
	private TextView price;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_order_layout);
		findViewById(R.id.back_btn).setOnClickListener(this);
		title = (TextView)findViewById(R.id.title_text);
		price = (TextView)findViewById(R.id.price_order);
		
		mSubmit = (Button)findViewById(R.id.submit_btn);
		mSubmit.setOnClickListener(this);
		EditText edit = (EditText)findViewById(R.id.count_edit);
		edit.selectAll();
		edit.addTextChangedListener(this);
		mTotal = (TextView)findViewById(R.id.total_text);
		
		dealIntent();
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
			price += p.getPrice();
		}
		this.price.setText(price+"");
		title.setText(shop.getName());
		mTotal.setText(price+"");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.submit_btn:
			if(shops == null || shops.size()== 0) return;
			new SubmitOrderAsyncTask(shops).execute();
			
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
			SubmitOrderHttpService sohs = new SubmitOrderHttpService(SubmitOrderActivity.this);
			boolean res = sohs.submitOrder(shops);
			if(res){
				ShoppingCartDBService sdbs = new ShoppingCartDBService(SubmitOrderActivity.this);
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

        }  
		protected void onPostExecute(Boolean res) {
			if(res){
				finish();
			}else{
				Toast.makeText(SubmitOrderActivity.this, "提交订单出错!", Toast.LENGTH_SHORT).show(); 
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
	
	
	
}
