package com.ins.test.ui;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.HashMap;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

import com.ins.functiontestscheme.R;
import com.ins.test.tool.DlgTool;
import com.ins.test.tool.DlgTool.GridAdapter.ItemViewTag;
import com.ins.test.tool.StrTool;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginActivity extends BaseAppActivity implements OnFocusChangeListener,
OnItemClickListener, PlatformActionListener{

	private final int CHECK_USER = 1;
	private final int CHECK_PASSWORD = 2;
	private final int CHECK_NONE = 0;
	public boolean user_enable = true;
	private View mPbProgress = null;
	private TextView mTvMsg = null;
	private String mStrMsg=null;
	private Handler mHander = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:{
				mTvMsg.setText(mStrMsg);
				break;
			}
			case 1:{
				mPbProgress.setVisibility(View.VISIBLE);
				break;
			}
			case -1:{
				mTvMsg.setText(mStrMsg);
			}
			case 2:{
				mPbProgress.setVisibility(View.GONE);
				break;
			}
			default:finish();
			}
		}
	};
	private void sendStateMsg(String str, boolean pbVisible) {
		mStrMsg = str;
		mHander.sendEmptyMessage(0);
		if (!pbVisible) {
			if(mPbProgress.getVisibility()==View.VISIBLE){
			mHander.sendEmptyMessage(2);
			}
		}else{
			if(mPbProgress.getVisibility()!=View.VISIBLE){
				mHander.sendEmptyMessage(1);
			}
		}
	}
	private boolean hasFlag(int flag) {
		return flag == (this.mCheckState & flag);
	}

	private void addFlag(int flag) {
		this.mCheckState |= flag;
	}

	private void subFlag(int flag) {
		if (hasFlag(flag)) {
			this.mCheckState ^= flag;
		}
	}

	private int mCheckState = CHECK_NONE;
	private EditText mEtUser, mEtPassword;
	//private WebHelper mWebhelper = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mEtUser = (EditText) findViewById(R.id.et_name);
		mEtPassword = (EditText) findViewById(R.id.et_phone);
		mPbProgress = findViewById(R.id.pb_progress);
		mTvMsg = (TextView) findViewById(R.id.tv_msg);
		mPbProgress.setVisibility(View.GONE);
		mTvMsg.setText("");
		mEtUser.setOnFocusChangeListener(this);
		mEtPassword.setOnFocusChangeListener(this);
		//mWebhelper = new WebHelper(this);
	}

	public void onLoginSuccess(){
		//Log.d("TianjunXu", "onLoginSuccess");//add by hsu
	    /*Intent tent = new Intent(AtyBootLogin.this, AtyTbMain.class);
	    tent.putExtra("download", false);
	    startActivity(tent);
		finish();*/
	}
	
	public void onXmlBtClick(View v) {
		switch (v.getId()) {
		case R.id.bt_login: {
			if (user_enable && checkSubmit()) {
				//startSubmitToServer();
			}
			break;
		}
		case R.id.tv_findpass:{
			//startActivity(new Intent(this, AtyFindpass.class));
			break;
		}
		case R.id.tv_register:{
			/*if (user_enable) {
				startActivityForResult(new Intent(this, AtyBootRegister.class),
						1234);
			}*/
			break;
		}
		case R.id.tv_direct_login:{
			//startActivity(new Intent(this, AtyTbMain.class));
			break;
		}
		case R.id.tv_third:{
			//popupOthers();
			((BaseApplication)getApplication()).openShareMenu(v, this);
			break;
		}
		case R.id.bt_cancle_share:{
			DlgTool.closeDelayed(100);
			break;
		}
		}
	}


	private void popupOthers() {
		/*Dialog dlg = new Dialog(this);
		View dlgView = View.inflate(this, R.layout.other_plat_dialog, null);
		View tvQq = dlgView.findViewById(R.id.tv_qq);
		tvQq.setTag(dlg);
		tvQq.setOnClickListener(this);
		View tvSina = dlgView.findViewById(R.id.tv_sina);
		tvSina.setTag(dlg);
		tvSina.setOnClickListener(this);
		
		View tvWechat = dlgView.findViewById(R.id.tv_wechat);
		tvWechat.setTag(dlg);
		tvWechat.setOnClickListener(this);

		dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dlg.setContentView(dlgView);
		dlg.show();*/
	}
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			if (v.equals(mEtUser)) {
				if (!StrTool.isEmpty(mEtUser.getText().toString())) {
					checkCellPhone(mEtUser);
				}
			} else {
				if (!StrTool.isEmpty(mEtPassword.getText().toString())) {
					checkPassword(mEtPassword);
				}
			}
		} else {
		}
	}

	private boolean checkPassword(EditText edit) {
		String name = edit.getText().toString().trim();
		if (name.equals("") || name.length() < 6) {
			subFlag(CHECK_PASSWORD);
			//popMsg("���벻��С��6���ַ�");
			return false;
		} else {
			addFlag(CHECK_PASSWORD);
			return true;
		}
	}

	private boolean checkCellPhone(EditText edit) {
		if (!StrTool.isMobileNO(edit.getText().toString().trim())
				&& !StrTool.isEmail(edit.getText().toString().trim())) {
			subFlag(CHECK_USER);
			//popMsg("��������ȷ���ֻ������");
			return false;
		} else {
			addFlag(CHECK_USER);
			return true;
		}
	}

	private boolean checkSubmit() {
		if (mCheckState != 3) {
			int check = 3 - mCheckState;
			int adjust = 0x1;
			while ((check & adjust) != adjust) {
				adjust <<= 1;
			}
			if (adjust == CHECK_USER) {
				if (checkCellPhone(mEtUser)) {
					checkSubmit();
				}
			} else if (adjust == CHECK_PASSWORD) {
				checkPassword(mEtPassword);
			}
			return mCheckState == 3;
		}
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 
	}

	public void popUIMsg(final String msg) {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				//popMsg(msg);
			}
		};
		//AtyBootLogin.this.runOnUiThread(r);
	}
	@Override
	public void onCancel(Platform arg0, int arg1) {
		Log.d("TianjunXu", "onCancel");
		
	}
	@Override
	public void onComplete(Platform plat, int result, HashMap<String, Object> map) {
		Log.d("TianjunXu", "onComplete");
		Log.d("TianjunXu","userName:"+plat.getDb().getUserName()+",userId:"+plat.getDb().getUserId()+",name="+plat.getName());
		Log.d("TianjunXu", "int:"+result);
		for(String key:map.keySet()) {
			Log.d("TianjunXu", "current key is:"+key+",vaue:"+map.get(key));
		}
		
		
	}
	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		Log.d("TianjunXu", "onError");
		
	}

	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.tv_qq:{
			showUser(new QQ(this));
			break;
		}
		case R.id.tv_sina:{
			showUser(new SinaWeibo(this));
			break;
		}
		case R.id.tv_wechat:{
			showUser(new TencentWeibo(this));
			break;
		}
		}
		
	}
	private void showUser(Platform plat) {
		if(plat == null) {
			Log.d("TianjunXu", "showUser,platform is null");
			return;
		}
		plat.setPlatformActionListener(this);
		plat.SSOSetting(true);
		plat.showUser(null);
		
	}
	@Override
	public void onItemClick(AdapterView<?> p, View v, int arg2, long arg3) {
		//Log.d("TianjunXu", "onItemClick:"+((ItemViewTag)v.getTag()).getPlatName());
		String plat = ((ItemViewTag)v.getTag()).getPlatName();
		if(plat.equals("QQ")) {
			showUser(new QQ(this));
		} else if(plat.equals("SinaWeibo")) {
			showUser(new SinaWeibo(this));
		} else if(plat.equals("TencentWeibo")) {
			showUser(new TencentWeibo(this));
		} else if(plat.equals("Wechat")) {
			showUser(new Wechat(this));
		}
	}

}
