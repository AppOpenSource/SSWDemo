package com.abt.ssw.views;

import com.abt.ssw.activitys.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 订单视图
 * 图表+标题+数量
 *
 */
public class OrderItemView extends RelativeLayout {

	//private ImageView mLogo;
	private TextView mTitle, mCount;
	
	public OrderItemView(Context context) {
		super(context);
		this.init();
	}

	public OrderItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init();
	}

	public OrderItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.init();
	}

	private void init() {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		inflater.inflate(R.layout.order_view, this);
		//mLogo = (ImageView)findViewById(R.id.logo_img);
		mTitle = (TextView)findViewById(R.id.title_text);
		mCount = (TextView)findViewById(R.id.count_text);
	}
	
	public TextView getTitleView() {
		return mTitle;
	}
	
	public TextView getCountView() {
		return mCount;
	}
}
