package com.nangua.xiaomanjflc.ui;

import com.louding.frame.KJActivity;
import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.support.ApkInfo;
import com.nangua.xiaomanjflc.support.UIHelper;
import com.nangua.xiaomanjflc.utils.ApplicationUtil;

import android.app.ActionBar.LayoutParams;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class NewHandActivity extends KJActivity {
	
	private ImageView iv;

	@Override
	public void setRootView() {
		setContentView(R.layout.activity_new_hand);
		UIHelper.setTitleView(this, "", "新手福利");
		iv = (ImageView) findViewById(R.id.newHandBg);
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_bg_new_hand);
		ApkInfo info = ApplicationUtil.getApkInfo(this);
		int h = bitmap.getHeight() * info.width /bitmap.getWidth();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, h);
		iv.setImageBitmap(bitmap);
	}

	@Override
	public void widgetClick(View v) {
		super.widgetClick(v);
	}

}
