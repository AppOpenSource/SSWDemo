package com.abt.ssw.activitys;

import java.util.ArrayList;
import java.util.List;
import com.abt.ssw.adapters.GoodDetailsListAdapter;
import com.abt.ssw.adapters.GradeListAdapter;
import com.abt.ssw.adapters.SetMailListAdapter;
import com.abt.ssw.model.MoreListItem;
import com.abt.ssw.views.GoodOrderView;
import com.abt.ssw.views.GoodOrderView.StayViewListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class GoodDetailsActivity extends Activity {
	private final int LIST_TYPE_0 = 0;
	private final int LIST_TYPE_1 = 1; 
	private final int LIST_TYPE_2 = 2;
	private List<MoreListItem> mData0, mData1, mData2;
	private TextView taocan;
	
	@Override    
	public void onCreate(Bundle savedInstanceState) {        
		super.onCreate(savedInstanceState);        
		setContentView(R.layout.good_detail_main);
		ListView mListView0 = (ListView)findViewById(R.id.list_view_0);
		ListView mListView1 = (ListView)findViewById(R.id.list_view_1);
		ListView mListView2 = (ListView)findViewById(R.id.list_view_2);
		taocan = (TextView)findViewById(R.id.taocan);
		
		Button button = (Button)findViewById(R.id.liji_qianggou);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(GoodDetailsActivity.this, SubmitOrderActivity.class);
				startActivity(intent);
			}
		});
		
		mData0 = new ArrayList<MoreListItem>();
		mData0.add(new MoreListItem(R.drawable.address_img, "5.0   512人评分", "", R.drawable.more_arrow));
		mData0.add(new MoreListItem(R.drawable.address_img, "122112人已购买		400天1小时12分", "", -1));
		GradeListAdapter adapter0 = new GradeListAdapter(this, mData0);
		mListView0.setAdapter(adapter0);
		mListView0.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});
		
		mData1 = new ArrayList<MoreListItem>();
		mData1.add(new MoreListItem(R.drawable.feedback, "商家信息", "", -1));
		mData1.add(new MoreListItem(-1, "快递电话:010-555555", "010-555555", R.drawable.call_bg));
		mData1.add(new MoreListItem(-1, "查看其它分店", "", R.drawable.more_arrow));
		GoodDetailsListAdapter adapter1 = new GoodDetailsListAdapter(this, mData1);
		mListView1.setAdapter(adapter1);
		mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GoodDetailsActivity.this.onItemClick(parent, view, LIST_TYPE_1, position, id);
			}
		});
		
		mData2 = new ArrayList<MoreListItem>();
		mData2.add(new MoreListItem(R.drawable.feedback, "套餐", -1));
		mData2.add(new MoreListItem(-1, "套餐详情", -1));
		mData2.add(new MoreListItem(-1, "查看图文详情", R.drawable.more_arrow));
		mData2.add(new MoreListItem(-1, "", -1));
		mData2.add(new MoreListItem(-1, "", -1));
		SetMailListAdapter adapter2 = new SetMailListAdapter(this, mData2);
		mListView2.setAdapter(adapter2);
		mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GoodDetailsActivity.this.onItemClick(parent, view, LIST_TYPE_2, position, id);
			}
		});
		
		GoodOrderView refreshableView = (GoodOrderView) findViewById(R.id.refreshview);                
		refreshableView.setStayView(findViewById(R.id.title), findViewById(R.id.theview),
				(ScrollView)findViewById(R.id.scrollview),
				new StayViewListener() {			
			@Override			
			public void onStayViewShow() {				
				//从下往上拉的时候回复显示
				findViewById(R.id.theviewstay).setVisibility(View.VISIBLE);				
				findViewById(R.id.theview).setVisibility(View.GONE);			
			}						
			@Override			
			public void onStayViewGone() {				
				//从上往下拉隐藏大布局，显示小布局
				findViewById(R.id.theviewstay).setVisibility(View.GONE);				
				findViewById(R.id.theview).setVisibility(View.VISIBLE);			
			}
		});                   
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		taocan.setText("鲜菌清汤炭火锅底	1份	128元\n贡品羔羊肉	2份	约200g/份	96元\n" +
					"特级精品肥牛	2份	约200g/份	96元\n高级花枝丸	1份	约200g	22元\n" +
					"蔬菜大拼	1份	48元\n糕点组合(双拼)	1份	25元\n" +
					"传统杂面	1份	8元\n传统秘制麻酱	5份,不可续	30元\n" +
					"配料	1份	6元\n桂花酸梅汤	1扎	约1000ml,不可续	25元\n");
	}
	
	public void onItemClick(AdapterView<?> parent, View view,int listtype, int position,
			long id) {
		switch (listtype) {
		case LIST_TYPE_0:
		{
		}
		case LIST_TYPE_1:
		{
			// 拨打电话
//			MoreListItem item = mData1.get(position);
//			CallPhoneUtil.callPhone(this, item.getPhoneNumber());
		}
		break;
		case LIST_TYPE_2:
		{
			switch (position) {
			case 0:
				startActivity(new Intent(this, FeedbackActivity.class));
				break;
			case 1:
				Toast.makeText(this, "无更新", Toast.LENGTH_SHORT).show();
				break;
			case 2:
				startActivity(new Intent(this, AboutActivity.class));
				break;
			case 3:
				loginOutDialog();
				break;

			default:
				break;
			}
		}
		break;
		}
	}
	
	private void loginOutDialog() {
		AlertDialog.Builder dialog = new AlertDialog.Builder(this);
		dialog.setTitle("提醒");
		dialog.setMessage("是否退出?");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		dialog.setNegativeButton("取消", null);
		dialog.create();
		dialog.show();
	}
}