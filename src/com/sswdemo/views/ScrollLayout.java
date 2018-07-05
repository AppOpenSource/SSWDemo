package com.sswdemo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

/************************************************************
 * ��Launcher�е�WorkSapce���������һ����л���Ļ����
 */
public class ScrollLayout extends ViewGroup {
	
	// private int page = 0;
	private Scroller mScroller; // ���ƹ�������
	private VelocityTracker mVelocityTracker;
	private int mCurScreen; // ��ǰ��ʾ��ҳ����
	private int mDefaultScreen = 0; // Ĭ����ʾҳ����
	private static final int TOUCH_STATE_REST = 0; // ֹͣ״̬
	private static final int TOUCH_STATE_SCROLLING = 1; // ����״̬
	private static final int SNAP_VELOCITY = 600; // �����ٶ�
	private int mTouchState = TOUCH_STATE_REST; // ScrollLayout�ĵ�ǰ״̬��Ĭ��Ϊֹͣ״̬
	private int mTouchSlop; // �ƶ�ʱ����Χ
	private float mLastMotionX; // ��¼��һ���������xλ��
	private float mLastMotionY;	// ��¼��һ���������yλ��
	private PageListener pageListener;
	
	public ScrollLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mScroller = new Scroller(context, new DecelerateInterpolator(2f));		//interpolator����ָ��һ������������

		mCurScreen = mDefaultScreen;
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop(); // ����ֵΪ16
	}

	
	// ���ӿؼ�����
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = 0;
		final int childCount = getChildCount(); // ��ȡScrollLayout���ӿؼ���ÿ���ӿؼ���һ��View�����һҳ

		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				final int childWidth = childView.getMeasuredWidth();
				childView.layout(childLeft, 0, childLeft + childWidth, childView.getMeasuredHeight());	//ָ���ӿؼ��ڸ��ؼ��е�λ��
				childLeft += childWidth;
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);	//����ṩ�Ĳ���ֵ(��ʽ)��ȡ��Сֵ(�����СҲ��������ͨ����˵�Ĵ�С)
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException("ScrollLayout only canmCurScreen run at EXACTLY mode!");
		}

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException("ScrollLayout only can run at EXACTLY mode!");
		}

		// The children are given the same width and height as the scrollLayout
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		scrollTo(mCurScreen * width, 0); // ������mCurScreenҳ
	}

	/**
	 * According to the position of current layout scroll to the destination
	 * page.
	 */
	public void snapToDestination() {
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		snapToScreen(destScreen);
	}


	public void snapToScreen(int whichScreen) {
		// get the valid layout page
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		if (getScrollX() != (whichScreen * getWidth())) {
			final int delta = whichScreen * getWidth() - getScrollX(); // ����Ҫ�ƶ��ľ���
			mScroller.startScroll(getScrollX(), 0, delta, 0, Math.abs(delta) * 2); // �ƶ�ScrollLayout�����deltaΪ�����������ƶ��������������ƶ�
			
			pageListener.page(mCurScreen, whichScreen);
			mCurScreen = whichScreen; // �������õ�ǰҳ����
			invalidate(); // Redraw the layout
		}
	}

	// public void setToScreen(int whichScreen) {
	// whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
	// mCurScreen = whichScreen;
	// scrollTo(whichScreen * getWidth(), 0);
	// }

	/**
	 * ��õ�ǰҳ��
	 */
	public int getCurScreen() {
		return mCurScreen;
	}

	/**
	 * ��������ĵ�ǰҳ��
	 */
//	public int getPage() {
//		return Configure.curentPage;
//	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) { // �жϹ����Ƿ�ֹͣ
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY()); // ��ScrollLayout�ƶ���ָ��λ�ã�getCurrX()��getCurrY()��ȡӦ���ƶ�����x��yλ��
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);

		final int action = event.getAction();
		final float x = event.getX(); // ��ȡ��ǰxλ��
		// final float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation(); // ֹͣ��������ĻҲ����ֹ�ƶ�
			}
			mLastMotionX = x;
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (mLastMotionX - x);
			System.out.println("ACTION_MOVE===========>deltaX:" + deltaX);
			mLastMotionX = x;
			scrollBy(deltaX, 0); // ˮƽ�ƶ�ScrollLayout��deltaX���ƶ��ľ��룬ע�⣺�����������ƶ������������ƶ�
			break;
		case MotionEvent.ACTION_UP:
			final VelocityTracker velocityTracker = mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000); // 1000��ʾ
															// һ��ʱ�����˶��˶��ٸ����أ�ָ������ʱ��Ϊһ��
			int velocityX = (int) velocityTracker.getXVelocity(); // �ж�ʵ�ʻ�������У�һ��ʱ�����˶��˶��ٸ�����

			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) { // �ж������ƶ��ٶ��Ƿ����SNAP_VELOCITY�����ҵ�ǰҳ���ǵ�һҳ
				// Fling enough to move left
				// �����ƶ�һҳ
				snapToScreen(mCurScreen - 1);
				// --page;
				// pageListener.page(page);
			} else if (velocityX < -SNAP_VELOCITY && mCurScreen < getChildCount() - 1) { // �ж������ƶ��ٶ��Ƿ����SNAP_VELOCITY�����ҵ�ǰҳ�������һҳ
				// Fling enough to move right
				// �����ƶ�һҳ
				snapToScreen(mCurScreen + 1);
				// ++page;
				// pageListener.page(page);
			} else { // �������������ƶ����ٶ�С��SNAP_VELOCITY�����ߵ�ǰҳΪ��һҳ�����һҳʱ����ʱ����������ƶ�
				snapToDestination();
			}
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return true;
	}

	// ����true��������¼����踸�ؼ����?�����ݸ��ӿؼ�������Ӧÿһҳ��View��������false�����Ƚ����¼����?���ӿؼ�������ӿؼ����?���false�������
	// �ӿؼ�û�д��?��ô�����ؼ������Ŵ������¼�
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		if (Configure.isMove)
//			return false;// ���طַ����ӿؼ�
		final int action = ev.getAction();
		if ((action == MotionEvent.ACTION_MOVE) && (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = ev.getX();
		final float y = ev.getY();

		switch (action) {
		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int) Math.abs(mLastMotionX - x);
			final int yDiff = (int) Math.abs(mLastMotionY - y);
			if (xDiff > mTouchSlop && yDiff<mTouchSlop) { // ��ˮƽ�ƶ��ľ������mTouchSlop�����Χʱ�����Ҵ�ֱ�ƶ�����С��mTouchSlop���ű�ʾ�ƶ�
				mTouchState = TOUCH_STATE_SCROLLING;
			}
			break;
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST : TOUCH_STATE_SCROLLING;
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mTouchState = TOUCH_STATE_REST;
			break;
		}
		return mTouchState != TOUCH_STATE_REST;
	}

	public void setPageListener(PageListener pageListener) {
		this.pageListener = pageListener;
	}

	//ҳ���ƶ�������
	public interface PageListener {
		//currPage:��ǰҳ
		//destPage:Ŀ��ҳ
		void page(int currPage, int destPage);
	}

	
}