package com.sswdemo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class ViewPager extends android.support.v4.view.ViewPager {

	public ViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		boolean ret = super.dispatchTouchEvent(ev);
		if (ret) {
			requestDisallowInterceptTouchEvent(true);
		}
		return ret;
	}
}
