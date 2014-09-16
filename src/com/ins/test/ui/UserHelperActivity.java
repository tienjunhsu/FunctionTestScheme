package com.ins.test.ui;

import java.util.ArrayList;

import com.ins.functiontestscheme.R;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserHelperActivity extends BaseAppActivity implements
		OnPageChangeListener {
	private int[] mItem = new int[] { R.drawable.img_splash0,
			R.drawable.img_splash1, R.drawable.img_splash2,
			R.drawable.img_splash3 };
	private ViewPager mViewPager = null;
	private PageAdp mViewAdp = null;
	private ImageView[] dots;
	private int mCurSelected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_helper);

		initAdapter();
		initDots();
	}

	public void onXmlBtClick(View v) {
		switch (v.getId()) {
		case R.id.tv_launch:
			finish();
			startActivity(new Intent(this,LoginActivity.class));
			break;
		}
	}

	@Override
	protected void onDestroy() {
		mViewPager.setAdapter(null);
		mViewPager.removeAllViews();
		//mViewAdp.clearnPaperView();
		super.onDestroy();
	}

	private void initAdapter() {
		mViewPager = (ViewPager) findViewById(R.id.vp_helper);
		mViewAdp = new PageAdp();
		mViewPager.setAdapter(mViewAdp);
		mViewPager.setOnPageChangeListener(this);
	}

	private void initDots() {
		LinearLayout mIndicator = (LinearLayout) findViewById(R.id.indicator);
		dots = new ImageView[mItem.length];

		for (int i = 0; i < dots.length; i++) {
			dots[i] = (ImageView) mIndicator.getChildAt(i);
			dots[i].setEnabled(false);
		}
		mCurSelected = 0;
		dots[mCurSelected].setEnabled(true);
	}

	private void setCurrentDot(int position) {
		if (position < 0 || position > dots.length - 1
				|| mCurSelected == position) {
			return;
		}

		dots[position].setEnabled(true);
		dots[mCurSelected].setEnabled(false);

		mCurSelected = position;
	}

	public class PageAdp extends PagerAdapter {

		private LayoutInflater mInflater = null;
		private ArrayList<View> mChilds = null;

		public PageAdp() {
			mInflater = getLayoutInflater();
			mChilds = new ArrayList<View>(mItem.length);
			for (int i = 0; i < mItem.length; i++) {
				mChilds.add(null);
			}
		}

		public void clearnPaperView() {
			for (int i = 0; i < mChilds.size(); i++) {
				View v = mChilds.get(i);
				if (v != null) {
					ImageView iv = (ImageView) v.findViewById(R.id.iv_image);
					Drawable da = iv.getDrawable();
					if (da != null && da instanceof BitmapDrawable) {
						Bitmap bmp = ((BitmapDrawable) da).getBitmap();
						if (bmp != null && !bmp.isRecycled()) {
							bmp.recycle();
							iv.setImageBitmap(null);
						}
					}
				}
				mChilds.set(i, null);
			}

		}

		@Override
		public int getCount() {
			if (mChilds != null) {
				return mChilds.size();
			}
			return 0;
		}

		@Override
		public Object instantiateItem(ViewGroup p, int i) {
			View v = mChilds.get(i);
			if (v == null) {
				v = mInflater.inflate(R.layout.helper_page_item, null);
				mChilds.set(i, v);
				ImageView iv = (ImageView) v.findViewById(R.id.iv_image);
				iv.setImageResource(mItem[i]);
				if (i == mChilds.size() - 1) {
					TextView tv = (TextView) v.findViewById(R.id.tv_launch);
					tv.setVisibility(View.VISIBLE);
				}
			}
			p.addView(v);
			return v;
		}

		@Override
		public void destroyItem(ViewGroup p, int i, Object o) {
			p.removeView(mChilds.get(i));
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			// TODO Auto-generated method stub
			return view == obj;
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		setCurrentDot(position);

	}
}
