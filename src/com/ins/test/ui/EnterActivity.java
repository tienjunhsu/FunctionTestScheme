package com.ins.test.ui;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class EnterActivity extends Activity {

	private boolean isFirstBoot = true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.updateOnlineConfig(this);
		if (!isFirstBoot) {
			Intent intent = new Intent(this, SplashActivity.class);
			startActivity(intent);
		} else {
			startActivity(new Intent(this, UserHelperActivity.class));
		}
		finish();
	}
}
