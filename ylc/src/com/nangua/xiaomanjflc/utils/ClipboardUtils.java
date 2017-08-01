package com.nangua.xiaomanjflc.utils;

import android.app.Activity;
import android.content.ClipboardManager;
import android.os.Build;

import com.nangua.xiaomanjflc.YilicaiApplication;

public class ClipboardUtils {

	public static void copy(String str) {
		if (Build.VERSION.SDK_INT >= 11) {
			ClipboardManager c = (ClipboardManager) YilicaiApplication.getInstance().getActivity().getSystemService(Activity.CLIPBOARD_SERVICE);
			c.setText(str);
		}
	}
	
}
