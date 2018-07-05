package com.abt.ssw.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class NoScrollListView extends ListView {

	public NoScrollListView(Context context) {
		super(context);
	}

	public NoScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, 0));
		int i = getMeasuredHeight() - (getListPaddingTop() + getListPaddingBottom() + 2 * getVerticalFadingEdgeLength());
	    i = getListPaddingTop() + getListPaddingBottom() + i * getCount();
	    setMeasuredDimension(getMeasuredWidth(), i);
	}
}
