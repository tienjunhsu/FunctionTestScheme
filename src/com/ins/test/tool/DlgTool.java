package com.ins.test.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.ins.functiontestscheme.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class DlgTool {
	private static PopupWindow mPop = null;

	public static boolean isShowing() {
		return mPop != null && mPop.isShowing();
	}
    private static Handler mHandler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
		  if(msg.what==0){
			  closePopDlg();
		  }
		}
    	
    };
	public static void closePopDlg() {
		if (mPop != null) {
			if (mPop.isShowing()) {
				mPop.dismiss();
				mPop = null;
			}
		}
	}
	public static void closeDelayed(int time){
		mHandler.sendEmptyMessageDelayed(0, time);
	}
	public static void showPopMenu(View anchor,int[]icons,String[] titles,String[] platList,OnItemClickListener listen){
		Context cx=anchor.getContext();
		View menuView = View.inflate(cx, R.layout.com_gridview_menu, null);
		GridView menuGrid = (GridView) menuView.findViewById(R.id.gridview);
		/*ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		 for(int i=0;i<icons.length;i++){
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("icon", icons[i]);
				map.put("title", titles[i]);
				data.add(map); 
		 }
		SimpleAdapter simperAdapter = new SimpleAdapter(cx, data,
				R.layout.com_grid_item, new String[] { "icon" ,"title"},
				new int[] { R.id.iv_icon,R.id.tv_title });
		 menuGrid.setAdapter(simperAdapter);*/
		 ArrayList<String> titleList= new ArrayList<String>();
		 ArrayList<Integer> iconList= new ArrayList<Integer>();
		 ArrayList<String> nameList= new ArrayList<String>();
		 for(int i=0;i<icons.length;i++){
			 titleList.add(titles[i]);
			 iconList.add(icons[i]);
			 nameList.add(platList[i]);
		 }
		 GridAdapter mGridAdp = new GridAdapter(cx,titleList,iconList,nameList);
		 menuGrid.setAdapter(mGridAdp);
		 menuGrid.setOnItemClickListener(listen);
		 menuGrid.setOnKeyListener(new OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if(keyCode==KeyEvent.KEYCODE_MENU){
						 mPop.dismiss();
					     mPop = null;	
					}
					return false;
				}
			});
		
		mPop=new PopupWindow(menuView,WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT);
		
		//设置或获得焦点，窗口内部的控件才可以响应监听事件。然而它使外面的父窗口不能响应按钮事件。
		mPop.setFocusable(true);
		mPop.setOutsideTouchable(true);
		//这个要设置，否则不能响应外部事件。
		mPop.setBackgroundDrawable(new BitmapDrawable());   
		mPop.setTouchInterceptor(new OnTouchListener(){
			public boolean onTouch(View v, MotionEvent event) {
				 if(event.getY()<0&&event.getAction()==MotionEvent.ACTION_DOWN) {
					 mPop.dismiss();
					 mPop=null;
				 } 
				 return false;
			}
		});
		mPop.setAnimationStyle(android.R.style.Animation_InputMethod);
		mPop.showAtLocation(anchor, Gravity.BOTTOM, 0, -5);
	}

	public static class GridAdapter extends BaseAdapter {
		private ArrayList<String> mNameList = new ArrayList<String>();
		private ArrayList<Integer> mDrawableList = new ArrayList<Integer>();
		private ArrayList<String> mPlatName = new ArrayList<String>();
		private LayoutInflater mInflater;
		private Context mContext;
		LinearLayout.LayoutParams params;

		public GridAdapter(Context context, ArrayList<String> nameList, ArrayList<Integer> drawableList,ArrayList<String> platList) {
			mNameList = nameList;
			mDrawableList = drawableList;
			mPlatName = platList;
			mContext = context;
			mInflater = LayoutInflater.from(context);
			
			params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER;
		}

		public int getCount() {
			return mNameList.size();
		}

		public Object getItem(int position) {
			return mNameList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ItemViewTag viewTag;
			
			if (convertView == null)
			{
				convertView = mInflater.inflate(R.layout.com_grid_item, null);
				
				// construct an item tag
				viewTag = new ItemViewTag((ImageView) convertView.findViewById(R.id.iv_icon), (TextView) convertView.findViewById(R.id.tv_title),mPlatName.get(position));
				convertView.setTag(viewTag);
			} else
			{
				viewTag = (ItemViewTag) convertView.getTag();
			}
			
			// set name
			viewTag.mName.setText(mNameList.get(position));
			
			// set icon
			viewTag.mIcon.setBackgroundResource(mDrawableList.get(position).intValue());
			viewTag.mIcon.setLayoutParams(params);
			return convertView;
		}
		
		public class ItemViewTag
		{
			protected ImageView mIcon;
			protected TextView mName;
			protected String platName;
			
			/**
			 * The constructor to construct a navigation view tag
			 * 
			 * @param name
			 *            the name view of the item
			 * @param platName
			 *            platform name
			 * @param icon
			 *            the icon view of the item
			 */
			public ItemViewTag(ImageView icon, TextView name,String platName)
			{
				this.mName = name;
				this.mIcon = icon;
				this.platName = platName;
			}
			public String getPlatName() {
				return platName;
			}
		}

	}
}
