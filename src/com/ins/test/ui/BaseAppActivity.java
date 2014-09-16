package com.ins.test.ui;

import cn.sharesdk.framework.ShareSDK;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;

public class BaseAppActivity extends Activity {

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		ShareSDK.initSDK(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
