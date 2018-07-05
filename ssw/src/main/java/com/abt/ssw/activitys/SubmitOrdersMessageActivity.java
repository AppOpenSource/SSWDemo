package com.abt.ssw.activitys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


/**
 * 提交订单
 *
 */
public class SubmitOrdersMessageActivity extends Activity implements OnClickListener{


	private EditText message;
	private Button Submit_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_order_msg);
		findViewById(R.id.back_btn).setOnClickListener(this);
		//findViewById(R.id.submit_btn).setOnClickListener(this);
		initView();
		dealIntent();
	}

	private void initView() {
		message = (EditText)findViewById(R.id.leave_message);
		Submit_btn = (Button)findViewById(R.id.submit_btn);
		Submit_btn.setOnClickListener(this);
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
			i.putExtra("message", message.getText().toString());
			setResult(RESULT_OK,i);
			this.finish();
			break;
		
		}
	}


	
	
}
