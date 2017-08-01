package com.nangua.xiaomanjflc.widget;

import com.nangua.xiaomanjflc.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class InviteCodeDialog {
	Context context;
	android.app.AlertDialog ad;
	TextView mName;
	TextView mPhone;
	FrameLayout flDialogSubmit,flDialogCancel;
	ImageView imgClose;
	
	private OnSubmitCallBack onSubmitCallBack;
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.flDialogSubmit :
					if (null != onSubmitCallBack) {
						onSubmitCallBack.onSubmit();
					}
					break;
				case R.id.flDialogCancel :
					dismiss();
					break;
				case R.id.imgClose :
					dismiss();
					break;
				default :
					break;
			}
			
		}
	};
	
	public static interface OnSubmitCallBack {
		public void onSubmit();
	}

	public InviteCodeDialog(Context context, String name, String mobile) {
		this.context = context;
		ad = new android.app.AlertDialog.Builder(context).create();
		ad.setCancelable(false);
		ad.setOnKeyListener(onKeyListener);
		ad.show();
		// 关键在下面的两行,使用window.setContentView,替换整个对话框窗口的布局
		Window window = ad.getWindow();
		window.setContentView(R.layout.dialog_invited_user);
		mName = (TextView) window.findViewById(R.id.name);
		mName.setText(name);
		mPhone = (TextView) window.findViewById(R.id.phone);
		mPhone.setText(mobile);
		imgClose = (ImageView) window.findViewById(R.id.imgClose);
		flDialogSubmit = (FrameLayout) window.findViewById(R.id.flDialogSubmit);
		flDialogCancel = (FrameLayout) window.findViewById(R.id.flDialogCancel);
		imgClose.setOnClickListener(onClickListener);
		flDialogSubmit.setOnClickListener(onClickListener);
		flDialogCancel.setOnClickListener(onClickListener);
	}
	
		
	public void setCallBack(OnSubmitCallBack onSubmitCallBack) {
		this.onSubmitCallBack = onSubmitCallBack;
	}
	
	public void show() {
		if (null != ad)
			ad.show();
	}
	
	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		ad.dismiss();
	}
	
    /**
     * add a keylistener for progress dialog
     */
    private OnKeyListener onKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                dismiss();
            }
            return false;
        }
    };
}
