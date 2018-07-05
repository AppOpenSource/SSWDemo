package com.sswdemo.activitys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.sswdemo.adapters.CommunityListAdapter;
import com.sswdemo.adapters.SearchGridViewAdapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

/**
 * 搜索Activity
 * @author xuena.ni
 */
public class SearchActivity extends Activity implements OnItemClickListener, OnClickListener {

	private List<HashMap<String, Object>> mListDate;
	private SearchGridViewAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		findViewById(R.id.back_btn).setOnClickListener(this);
		initData();
		mAdapter = new SearchGridViewAdapter(this, mListDate);
		GridView gridView = (GridView) findViewById(R.id.search_gridview);
		gridView.setOnItemClickListener(this);
		gridView.setAdapter(mAdapter);
	}
	
	private void initData() {
		mListDate = new ArrayList<HashMap<String,Object>>();
		for (int i = 0; i < 9; i++) {
			HashMap<String, Object> item = new HashMap<String, Object>();
			item.put("name", "餐饮");
			mListDate.add(item);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
	/*	Intent intent = new Intent(this, ReplyCommunityFeedAcitivty.class);
		startActivity(intent);*/
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
