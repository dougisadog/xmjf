package com.nangua.xiaomanjflc.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

@SuppressLint("ShowToast")
public class ToastUtil {

	private static Toast toast;

	public ToastUtil(Context context, String msg, int duration) {
		toast = Toast.makeText(context, msg, duration);
	}

	public static void showToast(Context context, String msg, int duration) {
		if (toast != null) {
			toast.cancel();
		}
		toast = Toast.makeText(context, msg, duration);
		if (toast == null) {
			toast = Toast.makeText(context, msg, duration);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}
}
