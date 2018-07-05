package com.sswdemo.activitys;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


/**我的*/
public class MyFocusActivity extends FragmentActivity implements OnCheckedChangeListener, OnPageChangeListener,OnClickListener {
	private RadioGroup mMainTab;
	private ViewPager mViewPager;
	//private ImageView cursor;
	private int bmpW, offset, currIndex, one, two;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_focus);
		//initImageView();

		one = offset * 2 + bmpW;// tab1 -> tab2 offset
		two = one * 2;// tab1 -> tab3 offset
		mMainTab = (RadioGroup)findViewById(R.id.main_tab);
		mMainTab.setOnCheckedChangeListener(this);
		mViewPager = (ViewPager) findViewById(R.id.vPager);
		List<Fragment> list = new ArrayList<Fragment>();
		list.add(new MyFocusTab1Fragment());
		list.add(new MyFocusTab2Fragment());
		PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), list);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(this);

		findViewById(R.id.back_btn).setOnClickListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch(checkedId){
		case R.id.my_curriculum_radio: // 我的课程
			mViewPager.setCurrentItem(0);
			break;
		case R.id.my_questions_and_answers_radio: // 我的问答
			mViewPager.setCurrentItem(1);
			break;

		}
	}

	/*	private void initImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.ico_arrow).getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 3 - bmpW) / 2;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);
	}*/

	@Override
	public void onPageSelected(int arg0) {
		Animation animation = null;
		switch (arg0) {
		case 0:
			if (currIndex == 1) {
				animation = new TranslateAnimation(one, 0, 0, 0);
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, 0, 0, 0);
			}
			setCurrentTab(0);
			break;
		case 1:
			if (currIndex == 0) {
				animation = new TranslateAnimation(offset, one, 0, 0);
			} else if (currIndex == 2) {
				animation = new TranslateAnimation(two, one, 0, 0);
			}
			setCurrentTab(1);
			break;
		}
		currIndex = arg0;
		animation.setFillAfter(true);
		animation.setDuration(300);
		//cursor.startAnimation(animation);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageScrollStateChanged(int arg0) {}

	private void setCurrentTab(int index){
		((RadioButton)mMainTab.getChildAt(index)).setChecked(true);
	}

	private class PageAdapter extends FragmentPagerAdapter {

		private List<Fragment> _listFragment;

		public PageAdapter(FragmentManager fm, List<Fragment> list) {
			super(fm);
			_listFragment = list;
		}

		@Override
		public Fragment getItem(int position) {
			return _listFragment.get(position);
		}

		@Override
		public int getCount() {
			return _listFragment.size();
		}
	}

	/********************************************************************
	 * 点击事件
	 */
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_btn:
			finish();
			break;

		default:
			break;
		}

	}
}
