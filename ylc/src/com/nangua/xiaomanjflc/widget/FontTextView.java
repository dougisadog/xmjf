package com.nangua.xiaomanjflc.widget;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.louding.frame.utils.StringUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ScaleXSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 集成字体配置 typefaceName 和字间距配置textSpacingExtra
 * @author Doug
 *
 */
public class FontTextView extends TextView {
	private Context context;
	private String TypefaceName = "";
	private boolean drawableCenter = false;

	public String getTypefaceName() {
		return TypefaceName;
	}

	public void setTypefaceName(String typefaceName) {
		TypefaceName = typefaceName;
		Typeface typeface = SingleFontText.getInstance(this.context,
				TypefaceName);
		this.setTypeface(typeface);
		System.gc();
	}

	public FontTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		int resouceId = attrs
				.getAttributeResourceValue(null, "typefaceName", 0);
		if (resouceId != 0) {
			TypefaceName = context.getResources().getString(resouceId);
		} else {
			TypefaceName = attrs.getAttributeValue(null, "typefaceName");
		}
		if (TypefaceName != null && !"".equals(TypefaceName)) {
			Typeface typeface = SingleFontText.getInstance(this.context,
					TypefaceName);
			this.setTypeface(typeface);
		}
		letterSpacing = attrs.getAttributeFloatValue(null, "textSpacingExtra", 0);
		if (letterSpacing == 0) return;
		originalText = super.getText();
        applyLetterSpacing();
        this.invalidate();
	}

	public FontTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		// 先判断是否配置的资源文件
		int resouceId = attrs
				.getAttributeResourceValue(null, "typefaceName", 0);
		if (resouceId != 0) {
			TypefaceName = context.getResources().getString(resouceId);
		} else {
			TypefaceName = attrs.getAttributeValue(null, "typefaceName");
		}
		if (StringUtils.isEmpty(TypefaceName)) {
			TypefaceName = "msyh";
		}
		Typeface typeface = SingleFontText.getInstance(this.context,
				TypefaceName);
		this.setTypeface(typeface);
		letterSpacing = attrs.getAttributeIntValue(null, "textSpacingExtra", 0);
		if (letterSpacing == 0) return;
		originalText = super.getText();
        applyLetterSpacing();
        this.invalidate();
	}

	public FontTextView(Context context) {
		super(context);
		this.context = context;
		if (TypefaceName != null && !"".equals(TypefaceName)) {
			Typeface typeface = SingleFontText.getInstance(this.context,
					TypefaceName);
			this.setTypeface(typeface);
		}
	}
	
	private float letterSpacing = 0;
    private CharSequence originalText = "";

	
	   public float getLetterSpacing() {
	        return letterSpacing;
	    }

	    public void setLetterSpacing(float letterSpacing) {
	        this.letterSpacing = letterSpacing;
	        applyLetterSpacing();
	    }

	    @Override
	    public void setText(CharSequence text, BufferType type) {
	        originalText = text;
	        if (letterSpacing == 0) {
	        	super.setText(text, type);;
	        }
	        else {
	        	applyLetterSpacing();
	        }
	        
	    }

	    @Override
	    public CharSequence getText() {
	        return originalText;
	    }
	    
	    /**
	     * 字距为任何字符串（技术上，一个简单的方法为CharSequence不使用）的TextView
	     */
	    private void applyLetterSpacing() {
	        if (this == null || this.originalText == null) return;
	        String o = originalText.toString();
	        StringBuilder builder = new StringBuilder();
	        for(int i = 0; i < o.length(); i++) {
	            String c = ""+ o.charAt(i);
	            if(i+1 < o.length() && i > 0) {
	            	if (isPunctuation(c.trim()) || 
	            			"".equals(c.trim())) continue;
	                builder.append("\u00A0");
	            }
	        }
	        SpannableString finalText = new SpannableString(builder.toString());
	        System.out.println(builder.toString());
	        if(builder.toString().length() > 1) {
	            for(int i = 1; i < finalText.toString().length(); i++) {
	            	//使用i+1 而非i-1 是为了跳过首个char的检测从而不更改首个空格的scaleX
	            	String current = "" + finalText.toString().charAt(i);
	            	String before = "" + finalText.toString().charAt(i-1);
	            	try {
						if ("\u00A0".equals(before.trim())) {
							if (isPunctuation(current)) {
								//i i+1 为空格位 
								finalText.setSpan(new ScaleXSpan(0.1f), i-1, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							}
							else if (!"".equals(current.trim()) && !"\u00A0".equals(current)){
								finalText.setSpan(new ScaleXSpan((letterSpacing+1)/10), i-1, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	            }
	        }
	        super.setText(finalText, BufferType.SPANNABLE);
	    }
	    
	    private boolean isPunctuation(CharSequence c) {
	    	Pattern pattern = Pattern.compile("[:：.。,，、\"\'()（）]");
			Matcher matcher = pattern.matcher(c);
			return matcher.matches();
	    	
	    }
}
