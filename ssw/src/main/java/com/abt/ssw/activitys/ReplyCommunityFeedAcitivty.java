package com.abt.ssw.activitys;

import java.util.ArrayList;
import com.abt.ssw.adapters.ReplyCommunityAdapter;
import com.abt.ssw.model.ReplyCommunityItem;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 回复社区动态
 */
public class ReplyCommunityFeedAcitivty extends Activity implements OnClickListener{

	private ReplyCommunityAdapter mAdapter;
	private ArrayList<ReplyCommunityItem> mData;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply_community_feed_layout);
		findViewById(R.id.back_btn).setOnClickListener(this);
		
		TextView username = (TextView) findViewById(R.id.username_text);
		username.setText("保罗");
		
		ListView listView = (ListView) findViewById(R.id.list_view);
		mData = new ArrayList<ReplyCommunityItem>();
		mData.add(new ReplyCommunityItem("", "动态原文", "2013-6-1 13:30", "android", 50));
		for (int i = 0; i < 50; i++) {
			mData.add(new ReplyCommunityItem("科比", "测试回复", "2013-6-1 13:30", "android", 0));
		}
		mAdapter = new ReplyCommunityAdapter(this, mData, null);
		listView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			this.finish();
			break;
		}
	}
}
