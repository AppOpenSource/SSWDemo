package com.abt.ssw.activitys;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 提交订单（支付方式）
 *
 */
public class PayforTypeActivity extends Activity implements OnClickListener,OnCheckedChangeListener{

	private RadioGroup payforGroup; //发票类型
	private Button Submit_btn;
	private String payforType = "货到付款";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payfor_type);
		findViewById(R.id.back_btn).setOnClickListener(this);
		initView();
		dealIntent();
	}

	private void initView() {
		payforGroup = (RadioGroup)findViewById(R.id.payfor_group);
		payforGroup.setOnCheckedChangeListener(this);
		
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
			Intent intent = new Intent();
			intent.putExtra("payfor_type", payforType);
			setResult(RESULT_OK, intent);
			this.finish();
		   break;
		
		
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (group.getId()) {
		case R.id.payfor_group:
            //获取变更后的选中项的ID
           int radioButtonId = group.getCheckedRadioButtonId();
           //根据ID获取RadioButton的实例
           RadioButton rb = (RadioButton)findViewById(radioButtonId);
          if(rb.getText().equals("米拉卡")){
        	  payforType = "米拉卡";
          }else if(rb.getText().equals("支付宝")){
        	  payforType = "支付宝";
          }else if(rb.getText().equals("货到付款")){
        	  payforType = "货到付款";
          }
			break;
		
		default:
			break;
		}
		
	}

	
}
