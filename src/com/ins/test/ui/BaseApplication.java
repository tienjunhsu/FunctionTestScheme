package com.ins.test.ui;

import com.ins.functiontestscheme.R;
import com.ins.test.tool.DlgTool;

import android.app.Application;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

public class BaseApplication extends Application {
	private String[] titles_ = new String[] { "QQ",  "新浪微博","腾讯微博",
	                          "微信" };
    private int[] icons_ = new int[] { R.drawable.logo_qq,
	                       R.drawable.logo_sinaweibo, R.drawable.logo_tencentweibo,
	                       R.drawable.logo_wechat,};
	private String[] plats_ = new String[] { "QQ","SinaWeibo",  "TencentWeibo","Wechat"};

	public void openShareMenu(View v, OnItemClickListener listen) {
		if(!DlgTool.isShowing()){
		DlgTool.showPopMenu(v, icons_, titles_,plats_, listen);
		}
	}
}
