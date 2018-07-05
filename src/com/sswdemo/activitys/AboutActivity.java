package com.sswdemo.activitys;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class AboutActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
		findViewById(R.id.back_btn).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		this.finish();
	}

	
}
