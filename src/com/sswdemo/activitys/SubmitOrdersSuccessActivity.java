package com.sswdemo.activitys;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * 提交订单
 *
 */
public class SubmitOrdersSuccessActivity extends Activity implements OnClickListener{

	
	private TextView orderNo;
	private TextView orderPrice;
	private TextView orderPayWay;
	private Button orderBtn;
	private Button finishBtn;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_order_success);
		//findViewById(R.id.back_btn).setOnClickListener(this);
		initView();
		dealIntent();
	}

	private void initView() {
		//orderNo = (TextView)findViewById(R.id.order_no);
		orderPrice = (TextView)findViewById(R.id.order_price);
		//orderPayWay = (TextView)findViewById(R.id.way_of_pay);
		orderBtn = (Button)findViewById(R.id.order);
		orderBtn.setOnClickListener(this);
		finishBtn = (Button)findViewById(R.id.finish_btn);
		finishBtn.setOnClickListener(this);
	}

	private void dealIntent() {
		Intent intent = getIntent();
		if(intent == null) return;
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.order:
			Intent intent = new Intent(SubmitOrdersSuccessActivity.this,OrderbacklogActivity.class);
			startActivity(intent);
			//this.finish();
			break;
		case R.id.finish_btn:
			this.finish();
			break;
		}
	}

	
}
