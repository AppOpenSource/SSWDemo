package com.sswdemo.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/** 指引页面Adapter */
public class GuidePageAdapter extends PagerAdapter { 
	private Context mActivity;
	private ArrayList<View> mpageViews;
	public GuidePageAdapter(Activity activity,ArrayList<View> pageViews){
		this.mActivity = activity;
		this.mpageViews = pageViews;
		
	}
	  
    public int getCount() {  
        return mpageViews.size();  
    }  

    @Override  
    public boolean isViewFromObject(View arg0, Object arg1) {  
        return arg0 == arg1;  
    }  

    @Override  
    public int getItemPosition(Object object) {  
        // TODO Auto-generated method stub  
        return super.getItemPosition(object);  
    }  

    @Override  
    public void destroyItem(View arg0, int arg1, Object arg2) {  
        // TODO Auto-generated method stub  
        ((ViewPager) arg0).removeView(mpageViews.get(arg1));  
    }  

    @Override  
    public Object instantiateItem(View arg0, int arg1) {  
        // TODO Auto-generated method stub  
        ((ViewPager) arg0).addView(mpageViews.get(arg1));  
        return mpageViews.get(arg1);  
    }  

    @Override  
    public void restoreState(Parcelable arg0, ClassLoader arg1) {  
        // TODO Auto-generated method stub  

    }  

    @Override  
    public Parcelable saveState() {  
        // TODO Auto-generated method stub  
        return null;  
    }  

    @Override  
    public void startUpdate(View arg0) {  
        // TODO Auto-generated method stub  

    }  

    @Override  
    public void finishUpdate(View arg0) {  
        // TODO Auto-generated method stub  

    }  

} 

