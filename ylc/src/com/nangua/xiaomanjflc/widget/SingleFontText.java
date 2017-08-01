package com.nangua.xiaomanjflc.widget;

import android.content.Context;
import android.graphics.Typeface;

/**
 * 加载字体 单例
 * */
public class SingleFontText {

	private static Typeface typeface;

	private SingleFontText() {
	}

	public static Typeface getInstance(Context context, String TypefaceName) {
		if (typeface == null) {
			typeface = Typeface.createFromAsset(context.getAssets(), "font/"
					+ TypefaceName + ".ttf");
		}
		return typeface;
	}

}