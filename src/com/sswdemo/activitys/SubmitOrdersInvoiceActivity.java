package com.sswdemo.activitys;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

/**
 * 提交订单（发票信息）
 *
 */
public class SubmitOrdersInvoiceActivity extends Activity implements OnClickListener,OnCheckedChangeListener{

	private RadioGroup typeGroup; //发票类型
	private RadioGroup contentGroup; //发票明细
	private EditText danwei_edits;
	private Button Submit_btn;
	private String taitou = "个人";
	private String mingxi = "明细";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_order_invoice);
		findViewById(R.id.back_btn).setOnClickListener(this);
		initView();
		dealIntent();
	}

	private void initView() {
		typeGroup = (RadioGroup)findViewById(R.id.type_group);
		typeGroup.setOnCheckedChangeListener(this);
		
		contentGroup = (RadioGroup)findViewById(R.id.content_group);
		contentGroup.setOnCheckedChangeListener(this);
		
		danwei_edits = (EditText)findViewById(R.id.danwei_edit);
		
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
			if(taitou.equals("单位")){
				taitou = "单位:"+ danwei_edits.getText().toString();
			}
			intent.putExtra("invoice_type", taitou);
			intent.putExtra("invoice_content", mingxi);
			setResult(RESULT_OK, intent);
			this.finish();
		   break;
		
		
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (group.getId()) {
		case R.id.type_group:
            //获取变更后的选中项的ID
           int radioButtonId = group.getCheckedRadioButtonId();
           //根据ID获取RadioButton的实例
           RadioButton rb = (RadioButton)findViewById(radioButtonId);
          if(rb.getText().equals("单位")){
        	  danwei_edits.setVisibility(View.VISIBLE);
        	 // taitou =  "单位："+danwei_edits.getText().toString();
        	  taitou =  "单位";
          }else if(rb.getText().equals("个人")){
        	  danwei_edits.setVisibility(View.GONE);
        	  taitou = "个人";
          }
			break;
		case R.id.content_group:
			  // TODO Auto-generated method stub
            //获取变更后的选中项的ID
           int radioButtonId1 = group.getCheckedRadioButtonId();
           //根据ID获取RadioButton的实例
           RadioButton rb1 = (RadioButton)findViewById(radioButtonId1);
          if(rb1.getText().equals("明细")){
        	  mingxi = "明细";
          }else if(rb1.getText().equals("办公用品")){
        	  mingxi = "办公用品";
          }else if(rb1.getText().equals("电脑配件")){
        	  mingxi = "电脑配件";
          }else if(rb1.getText().equals("耗材")){
        	  mingxi = "耗材";
          }
			break;
		default:
			break;
		}
		
	}

	
}
