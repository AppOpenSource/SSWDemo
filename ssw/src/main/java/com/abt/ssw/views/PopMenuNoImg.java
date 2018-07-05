package com.abt.ssw.views;

import java.util.ArrayList;
import java.util.List;

import com.abt.ssw.activitys.R;
import com.abt.ssw.adapters.PopNoImgAdapter;
import com.abt.ssw.beans.RecommendedInfoDataBean;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

public class PopMenuNoImg {
	private ArrayList<String> itemList;
	private Context context;
	private PopupWindow popupWindow;
	private ListView listView;
	//private List<RecommendedInfoDataBean> listData;

	// private OnItemClickListener listener;

	public PopMenuNoImg(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;

		itemList = new ArrayList<String>(5);

		View view = LayoutInflater.from(context).inflate(R.layout.popmenu, null);

		// 设置 listview
		listView = (ListView) view.findViewById(R.id.listView);
		
		listView.setFocusableInTouchMode(true);
		listView.setFocusable(true);
		
		//popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
	}

	// 设置菜单项点击监听器
	public void setOnItemClickListener(OnItemClickListener listener) {
		// this.listener = listener;
		listView.setOnItemClickListener(listener);
	}
	/****************************************************************
	 * 加载ListView数据
	 * @return
	 */
	public void addData(List<RecommendedInfoDataBean> listDatas) {
		listView.setAdapter(new PopNoImgAdapter(context,listDatas));
	}

	/*// 批量添加菜单项
	public void addItems(String[] items) {
		for (String s : items)
			itemList.add(s);
	}

	// 单个添加菜单项
	public void addItem(String item) {
		itemList.add(item);
	}*/

	// 下拉式 弹出 pop菜单 parent 右下角
	public void showAsDropDown(View parent) {
		popupWindow.showAsDropDown(parent,0,0);
		popupWindow.getContentView().setOnTouchListener(new OnTouchListener(){    
            public boolean onTouch(View v, MotionEvent event) {  
            	popupWindow.setFocusable(false);//失去焦点  
            	popupWindow.dismiss();//消除pw  
               return true;  
            }       
    }); 
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 刷新状态״̬
		popupWindow.update();
	}

	// 隐藏菜单
	public void dismiss() {
		popupWindow.dismiss();
	}

	
}
