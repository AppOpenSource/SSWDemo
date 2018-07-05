package com.abt.ssw.activitys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.abt.ssw.adapters.GuidePageAdapter;
import com.abt.ssw.adapters.HomeGridViewAdapter;
import com.abt.ssw.utils.L;
import com.abt.ssw.views.TagsGridView;
import com.abt.ssw.views.ViewPager;

/***********************************************************
 * 主页
 * nixuena
 *
 */
public class HomeActivity extends Activity{
	protected static final String TAG = "ssw";
	protected static final int REFRESH_ORDER = 0;
	private ViewPager viewPager;  
	private ArrayList<View> pageViews;  
	private ViewGroup main, group;  
	private ImageView imageView;  
	private ImageView[] imageViews;
	private TagsGridView myGridView;
	private List<Map<String,Object>> listData;
	private static Handler mainTabHandler;
	public static Handler getMainTabHandler() {
		return mainTabHandler;
	}

	public static void registerMainTabHandler(Handler mainTabHandler) {
		HomeActivity.mainTabHandler = mainTabHandler;
	}

	/**************************************************
	 * scroll ad auto
	 */
	private Handler scrollAdHandler;
	private Runnable scrollAdRunnable;
	private boolean activityIsRunning = false;
	private RelativeLayout viewPagerRelative;
	private TextView orderSn;
	private TextView orderTotal;
	private TextView orderTime;
	private ImageView orderImg;
	private TextView orderDesp;
	private static int flag = 3;
	
	public static int getFlag() {
		return flag;
	}
	
	public static void setFlag(int f) {
		flag = f;
	}
	
	//	protected List<Order> orders;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shouye);
		activityIsRunning = true;
		initViewpager();
		initGridView();
		//initOrderView();
		//new GetOrderAsyncTask().execute();
		HomeActivity.setFlag(3);
	}

	/*private void initOrderView() {
		orderSn = (TextView)findViewById(R.id.dingdanhao_text);
		orderTotal = (TextView)findViewById(R.id.dingdanjiner_text);
		orderTime = (TextView)findViewById(R.id.xiadanshijian_text);
		orderImg = (ImageView)findViewById(R.id.dingdan_img);
		orderDesp = (TextView)findViewById(R.id.dingdanmiaoshu_text);
	}*/
	
	protected void onResume() {
		super.onResume();
	}

	/*private class GetOrderAsyncTask extends AsyncTask<Void, Void, List<Order>> {

		protected List<Order> doInBackground(Void... params) {
			GetOrderHttpService gohs = new GetOrderHttpService();
			List<Order> orders = gohs.getOrders();
			return orders;
		}
		protected void onPreExecute() {  

		}  
		protected void onPostExecute(List<Order> orders) {
			if(orders == null || orders.size() == 0) return;
			try{
				Order o = orders.get(0);
				orderSn.setText(o.getSn());
				orderTotal.setText(o.getTotal()+"");
				orderTime.setText(DateFormatUtil.getComparedFormateDateString(o.getTime()));
				orderDesp.setText(o.getName());
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/

	protected void onDestroy() {
		super.onDestroy();
		activityIsRunning = false;
	}

	/***********************************************************************
	 * 初始化Viewpager控件
	 */
	public void initViewpager(){
		LayoutInflater inflater = getLayoutInflater();  
		pageViews = new ArrayList<View>(); 
		pageViews.add(inflater.inflate(R.layout.item01, null));
		//pageViews.add(inflater.inflate(R.layout.item01, null));
	/*	for(int i=0;i<MainTabActivity.PAGE_COUNT;i++){
			pageViews.add(inflater.inflate(R.layout.item01, null));   
		}*/

		imageViews = new ImageView[pageViews.size()];  
		//	        main = (ViewGroup)inflater.inflate(R.layout.shouye, null);  

		// group是R.layou.main中的负责包裹小圆点的LinearLayout.  
		group = (ViewGroup)findViewById(R.id.viewGroup);  

		viewPager = (ViewPager)findViewById(R.id.guidePages);  
		viewPagerRelative = (RelativeLayout)findViewById(R.id.view_pager_relative);  

		for (int i = 0; i < pageViews.size(); i++) {  
			imageView = new ImageView(HomeActivity.this);  
			imageView.setLayoutParams(new LayoutParams(15,15));  
			imageView.setPadding(20, 20, 20, 20); 
			imageViews[i] = imageView;  
			if (i == 0) {  
				//默认选中第一张图片
				imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);  
			} else {  
				imageViews[i].setBackgroundResource(R.drawable.page_indicator);  
			}  
			group.addView(imageViews[i]);  
		}  

		//	        setContentView(main);  

		viewPager.setAdapter(new GuidePageAdapter(this,pageViews));  
		viewPager.setOnPageChangeListener(new GuidePageChangeListener()); 
		float width = this.getWindowManager().getDefaultDisplay().getWidth();
		float height =this.getWindowManager().getDefaultDisplay().getHeight();

		viewPagerRelative.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				(int) (width/1.4)));

		//scroll ad thread
		startScrollAdThread();

	}

	/** 指引页面改监听器 */
	class GuidePageChangeListener implements OnPageChangeListener {  

		@Override  
		public void onPageScrollStateChanged(int arg0) {  
			// TODO Auto-generated method stub  

		}  

		@Override  
		public void onPageScrolled(int arg0, float arg1, int arg2) {  
			// TODO Auto-generated method stub  

		}  

		@Override  
		public void onPageSelected(int arg0) {  
			for (int i = 0; i < imageViews.length; i++) {  
				imageViews[arg0].setBackgroundResource(R.drawable.page_indicator_focused);  
				if (arg0 != i) {  
					imageViews[i].setBackgroundResource(R.drawable.page_indicator);  
				}  
			}

		}  

	}

	public void initGridView(){
		myGridView = (TagsGridView)findViewById(R.id.shouye_gridview);
		listData = getDataList();
		HomeGridViewAdapter mHomeGridViewAdapter = new HomeGridViewAdapter(this,listData);
		myGridView.setAdapter(mHomeGridViewAdapter);
		myGridView.setOnItemClickListener(gridviewItemClickListener);
	}

	public List<Map<String,Object>> getDataList(){
		listData = new ArrayList<Map<String,Object>>();
		int img[] = {R.drawable.canyin_bg,R.drawable.baihuo_bg,R.drawable.fuwu_bg,R.drawable.wumei_bg,R.drawable.twofour_bg,R.drawable.wode_bg,};
		String name[] = {"餐饮","百货","服务","物美","24h","我的"};
		for (int i=0; i<img.length; i++){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("name", name[i]);
			map.put("imgId", img[i]);
			listData.add(map);
		}

		return listData;
	}

	OnItemClickListener gridviewItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			switch (position) {
			case 0:
				HomeActivity.setFlag(0);
				getMainTabHandler().sendEmptyMessage(MainTabActivity.JUMP_TO_TAB_2); 
				break;
			case 1:
				HomeActivity.setFlag(1);
				getMainTabHandler().sendEmptyMessage(MainTabActivity.JUMP_TO_TAB_2); 
				break;
			case 2:
				HomeActivity.setFlag(2);
				getMainTabHandler().sendEmptyMessage(MainTabActivity.JUMP_TO_TAB_2); 
				break;
			case 3:
				Intent goodsIntent_wumei = new Intent(HomeActivity.this,GoodsListActivity.class);
				goodsIntent_wumei.putExtra("_shop_id", "2");
				goodsIntent_wumei.putExtra("_shop_name", "物美");
				startActivity(goodsIntent_wumei);
				break;
			case 4:
				Intent goodsIntent_24hours = new Intent(HomeActivity.this,GoodsListActivity.class);
				goodsIntent_24hours.putExtra("_shop_id", "1");
				goodsIntent_24hours.putExtra("_shop_name", "24h便利店");
				startActivity(goodsIntent_24hours);
				break;
			case 5: // 我的
				startActivity(new Intent(HomeActivity.this, MyActivity.class));
				break;
				//				case 0://餐饮
				//					/*Intent repastIntent = new Intent(HomeActivity.this,RepastActivity.class);
				//					startActivity(repastIntent);*/
				//				  break;
				//				case 5: // 我的
				//startActivity(new Intent(HomeActivity.this, ServeActivity.class));
			default:
				//startActivity(new Intent(HomeActivity.this, ServeActivity.class));

				break;
			}
		}
	};

	/**
	 * start to scroll ad by thread
	 * @author mfy
	 */
	private void startScrollAdThread() {
		scrollAdHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				L.v(TAG, "scrollAdHandler");
			}
		};
		scrollAdRunnable = new scrollAdRunnable();
		Thread thread = new Thread(scrollAdRunnable);
		thread.start();
	}

	/**
	 * start ad runnable
	 * @author mfy
	 */
	class scrollAdRunnable implements Runnable {
		int index = 0;

		@Override
		public void run() {
			if (activityIsRunning) {
				//L.v(TAG, "scrollAdRunnable");
				scrollAdHandler.postDelayed(scrollAdRunnable, 1000 * 2);
				if (++index > MainTabActivity.PAGE_COUNT) index = 0;
				viewPager.setCurrentItem(index, true);
			}
		}
	}
	 public boolean onKeyDown(int keyCode, KeyEvent event) {//捕捉返回键
	      if(keyCode == KeyEvent.KEYCODE_BACK){
	        	ConfirmExit();//按了返回键，但已经不能返回，则执行退出确认
	        	return true; 
	        }   
	        return super.onKeyDown(keyCode, event);   
	    }
	    public void ConfirmExit(){//退出确认
	    	AlertDialog.Builder ad=new AlertDialog.Builder(HomeActivity.this);
	    	ad.setTitle("退出");
	    	ad.setMessage("是否退出软件?");
	    	ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按钮
				@Override
				public void onClick(DialogInterface dialog, int i) {
					// TODO Auto-generated method stub
					HomeActivity.this.finish();//关闭activity
	 
				}
			});
	    	ad.setNegativeButton("否",new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int i) {
					//不退出不用执行任何操作
				}
			});
	    	ad.show();//显示对话框
	    }
}
