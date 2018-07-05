package com.abt.ssw.activitys;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * 提交订单
 *
 */
public class SubmitOrdersAddrActivity extends Activity implements OnClickListener{

	
	private EditText consignee;
	private EditText mobile;
	private EditText phone;
	private EditText addr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_order_addr);
		findViewById(R.id.back_btn).setOnClickListener(this);
		findViewById(R.id.submit_btn).setOnClickListener(this);
		initView();
		dealIntent();
	}

	private void initView() {
		consignee = (EditText)findViewById(R.id.consignee);
		addr = (EditText)findViewById(R.id.addr);
		mobile = (EditText)findViewById(R.id.mobile);
		phone = (EditText)findViewById(R.id.phone);
	}

	private void dealIntent() {
		Intent intent = getIntent();
		if(intent == null) return;
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		case R.id.submit_btn:
			Intent i = new Intent();
			i.putExtra("consignee", consignee.getText().toString());
			i.putExtra("addr", addr.getText().toString());
			i.putExtra("mobile", mobile.getText().toString());
			i.putExtra("phone", phone.getText().toString());
			setResult(RESULT_OK,i);
			this.finish();
			break;
		
		}
	}


	
	
}
