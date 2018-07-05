
package com.sswdemo.activitys;
import java.util.List;
import org.json.JSONObject;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.gson.Gson;
import com.sswdemo.adapters.ServeListAdapter;
import com.sswdemo.base.AppParameters;
import com.sswdemo.beans.ServeListBean;
import com.sswdemo.beans.ServeListDataBean;
import com.sswdemo.helper.HttpTools;
import com.sswdemo.utils.HttpUtil;
import com.sswdemo.views.MyListView;
import com.sswdemo.views.MyListView.OnRefreshListener;
import com.sswdemo.views.PopMenu;

/*************************************************************************
 * 服务圈Activity
 * xuena.ni
 * @since 2013-6-6
 */
public class ServeActivity extends Activity implements OnClickListener{
	private MyListView servelist;
	private LinearLayout ClassifyBtn;  //分类按钮
	private LinearLayout SortBtn;  //排序按钮
	private PopMenu ClassifypopMenu,SortpopMenu;
	private ImageButton MapBtn;
	private ServeListAdapter mServeListAdapter;
	private ImageButton SearchBtn;
	private Dialog mProgressDialog;
	public static TextView Classify_text;
	private TextView Sort_text;
	private static String type = "0";
	private static String order = "0";
	private static final int DATA_PICKER_ID = 1; 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.serve);
		initView();
		initPopView();	
//		new ServeListAsyncTask(type,order).execute();
	}

	protected void onResume() {
		super.onResume();
		switch (HomeActivity.getFlag()) {
		case 3:
			type = "0";
			new ServeListAsyncTask(type,order).execute();
			Classify_text.setText("全部");
			break;
		case 0://餐饮
			type = "1";
			new ServeListAsyncTask(type,order).execute();
			Classify_text.setText("餐饮");
			break;
		case 1://百货
			type = "2";
			new ServeListAsyncTask(type,order).execute();
			Classify_text.setText("百货");
			break;
		case 2://服务
			type = "3";
			new ServeListAsyncTask(type,order).execute();
			Classify_text.setText("服务");
			break;
		default:
			break;
		}
	}
	
	/*******************************************************************
	 * 初始化控件
	 */
	public void initView(){
		ClassifyBtn = (LinearLayout)findViewById(R.id.left_relative);	
		SortBtn = (LinearLayout)findViewById(R.id.right_relative);
		ClassifyBtn.setOnClickListener(this);
		SortBtn.setOnClickListener(this);
		MapBtn = (ImageButton)findViewById(R.id.ditu_btn);
		MapBtn.setOnClickListener(this);
		SearchBtn = (ImageButton)findViewById(R.id.search_btn);
		SearchBtn.setOnClickListener(this);
		Classify_text = (TextView)findViewById(R.id.classify_text);
		Sort_text = (TextView)findViewById(R.id.sort_text);
	}

	/********************************************************************
	 * 初始化popview数据
	 */
	private void initPopView(){
		ClassifypopMenu = new PopMenu(this);
		SortpopMenu = new PopMenu(this);
		ClassifypopMenu.addData(new int[]{R.drawable.pop0_0,R.drawable.pop0_1,R.drawable.pop0_2,R.drawable.pop0_3}, 
				new String[] { "全部", "餐饮", "百货", "服务"});
		SortpopMenu.addData(new int[]{R.drawable.pop1_0,R.drawable.pop1_1,R.drawable.pop1_2}, 
				new String[] { "默认排序", "人气最高", "离我最近"});
		// 菜单项点击监听器
		ClassifypopMenu.setOnItemClickListener(popmenuClassifyItemClickListener);
		SortpopMenu.setOnItemClickListener(popmenuSortItemClickListener);
	}

	/******************************************************************************
	 * 1.1获取商家列表接口
	 */
	private static ServeListAsyncTask sat;
	public ServeListAsyncTask getSAT(String type, String order) {
		if (sat == null) {
//			String type = "0";
//			String order = "1";
			sat = new ServeListAsyncTask(type, order);
		}
		return sat;
	}
	public class ServeListAsyncTask extends AsyncTask<Void, Void,  ServeListBean> {
		private String _type;
		private String _order;

		public ServeListAsyncTask(String type, String order) {
			this._type = type;
			this._order = order;
		}

		protected ServeListBean doInBackground(Void... params) {
			JSONObject json = new JSONObject();
			ServeListBean mServeListBean = null;
			try { 
				json.put("type", _type);
				json.put("order", _order);
				json.put("location_x", "1.0");
				json.put("location_y", "2.0");
				String request =  HttpUtil.doPost(AppParameters.getInstance().url_getServeList(),
						"parm",json.toString());
				Gson gson = new Gson();
				mServeListBean = gson.fromJson(request, ServeListBean.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return mServeListBean;
		}
		protected void onPreExecute() {  
			mProgressDialog = new Dialog(ServeActivity.this,R.style.theme_dialog_alert);
			mProgressDialog.setContentView(R.layout.window_layout);
			mProgressDialog.show();
			//检查网络
			if(!HttpTools.checkNetwork(getApplicationContext())){
				mProgressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "非常抱歉，您尚未链接网络!", Toast.LENGTH_LONG).show();
			}
		}  
		protected void onPostExecute(ServeListBean mServeListBean) {
			mProgressDialog.dismiss();
			if (mServeListBean != null) {
				if(mServeListBean.getRes() == 1)
					Toast.makeText(getApplicationContext(), "请求数据失败！", Toast.LENGTH_LONG).show();
				else
					initListView(mServeListBean.getData());
			}
		}
	}

	/******************************************************************
	 * 初始化ListView数据
	 */
	public void initListView(final List<ServeListDataBean> listData){
		servelist = (MyListView)findViewById(R.id.serve_list);
		mServeListAdapter = new ServeListAdapter(this,listData);
		servelist.setAdapter(mServeListAdapter);
		servelist.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
				Intent intent = new Intent(ServeActivity.this,MerchantDetailsActivity.class);
				intent.putExtra("_shop_id", listData.get(position-1).getShop_id());
				startActivity(intent);
			}

		});
		servelist.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				servelist.onRefreshComplete();
				/*	new AsyncTask<Void, Void, Void>() {
						protected Void doInBackground(Void... params) {
							try {
								Thread.sleep(1000);
							} catch (Exception e) {
								e.printStackTrace();
							}
							listData.add("刷新后添加的内容");
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							mServeListAdapter.notifyDataSetChanged();
							servelist.onRefreshComplete();
						}

					}.execute(null);*/
			}
		});
	}


	// 弹出菜单监听器（分类）
	OnItemClickListener popmenuClassifyItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			ClassifypopMenu.dismiss();
			switch (position) {
			case 0://全部
				type = "0";
				new ServeListAsyncTask(type,order).execute();
				Classify_text.setText("全部");
				break;
			case 1://餐饮
				type = "1";
				new ServeListAsyncTask(type,order).execute();
				Classify_text.setText("餐饮");
				break;
			case 2://百货
				type = "2";
				new ServeListAsyncTask(type,order).execute();
				Classify_text.setText("百货");
				break;
			case 3://服务
				type = "3";
				new ServeListAsyncTask(type,order).execute();
				Classify_text.setText("服务");
				break;
			default:
				break;
			}
		}
	};
	
	// 弹出菜单监听器（排序）
	OnItemClickListener popmenuSortItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			SortpopMenu.dismiss();
			switch (position) {
			case 0:
				order = "0";
				new ServeListAsyncTask(type,order).execute();
				Sort_text.setText("默认排序");
				System.out.println("分类"+type+"排序"+order);
				break;
			case 1:
				order = "1";
				new ServeListAsyncTask(type,order).execute();
				Sort_text.setText("人气最高");
				System.out.println("分类"+type+"排序"+order);
				break;
			case 2:
				order = "3";
				new ServeListAsyncTask(type,order).execute();
				Sort_text.setText("离我最近");
				System.out.println("分类"+type+"排序"+order);
				break;

			default:
				break;
			}
		}
	};

	/*******************************************************************
	 * 控件点击事件
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_relative:
			ClassifypopMenu.showAsDropDown(v);
			break;
		case R.id.right_relative:
			SortpopMenu.showAsDropDown(v);
			break;
		case R.id.ditu_btn:
			Intent mapintent = new Intent(ServeActivity.this,MyMapActivity.class);
			startActivity(mapintent);
			break;
		case R.id.search_btn:
			Intent searchintent = new Intent(ServeActivity.this,SearchActivity.class);
			startActivity(searchintent);
			/*  showDialog(DATA_PICKER_ID);*/ 
			break;
		default:
			break;
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
	    	AlertDialog.Builder ad=new AlertDialog.Builder(ServeActivity.this);
	    	ad.setTitle("退出");
	    	ad.setMessage("是否退出软件?");
	    	ad.setPositiveButton("是", new DialogInterface.OnClickListener() {//退出按钮
				@Override
				public void onClick(DialogInterface dialog, int i) {
					// TODO Auto-generated method stub
					ServeActivity.this.finish();//关闭activity
	 
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
   /* @Override  
    protected Dialog onCreateDialog(int id) {  
        // TODO Auto-generated method stub  
        switch (id) {  
        case DATA_PICKER_ID:  
            Log.v("Test", "--------start---------->");  
//            return new DatePickerDialog(this, onDateSetListener, 2011, 01, 01);
            return new TimePickerDialog(this, onTimeSetListener, 23, 22, true);
        }  
        return super.onCreateDialog(id);  
    }  
    TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {  
    	  
        @Override  
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {  
            // TODO Auto-generated method stub  
            System.out.println(hourOfDay + "-" + minute);  
            Log.i("ss", "sssssssssssssssssssssssssssssssss");
            //button4.setText(hourOfDay + "-" + minute);
        }  
    
    };
	*/
}
