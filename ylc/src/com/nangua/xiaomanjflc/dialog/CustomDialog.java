package com.nangua.xiaomanjflc.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import com.nangua.xiaomanjflc.R;
import com.nangua.xiaomanjflc.widget.FontTextView;

public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private int resource;
		private String message;
		private String positiveButtonText;
		private View contentView;
		private DialogInterface.OnClickListener positiveButtonClickListener;
		private DialogInterface.OnClickListener cancelButtonClickListener;


		public Builder(Context context) {
			this.context = context;
			resource = R.layout.dialog_normal_layout;
		}

		public Builder(Context context, int resource) {
			this.context = context;
			this.resource = resource;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		public Builder setPositiveButton(int positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = (String) context.getText(positiveButtonText);
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener listener) {
			this.positiveButtonText = positiveButtonText;
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setCancelButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.cancelButtonClickListener = listener;
			return this;
		}

		public Builder setCancelButton(DialogInterface.OnClickListener listener) {
			this.cancelButtonClickListener = listener;
			return this;
		}

		@SuppressWarnings("deprecation")
		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final CustomDialog dialog = new CustomDialog(context, R.style.Dialog);
			View layout = inflater.inflate(resource, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			// 确认按钮
			if (positiveButtonText != null) {
				((Button) layout.findViewById(R.id.positiveButton))
						.setText(positiveButtonText);
				if (positiveButtonClickListener != null) {
					((Button) layout.findViewById(R.id.positiveButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				layout.findViewById(R.id.positiveButton).setVisibility(
						View.GONE);
			}

			// 取消按钮
			if (cancelButtonClickListener != null) {
				((ImageView) layout.findViewById(R.id.cancel))
						.setOnClickListener(new View.OnClickListener() {
							public void onClick(View v) {
								cancelButtonClickListener.onClick(dialog,
										DialogInterface.BUTTON_NEGATIVE);
							}
						});
			}

			// 提示消息
			if (message != null) {
				((FontTextView) layout.findViewById(R.id.message)).setText(message);
			} else if (contentView != null) {
				((WebView) layout.findViewById(R.id.content))
						.removeAllViews();
				((WebView) layout.findViewById(R.id.content)).addView(
						contentView, new LayoutParams(LayoutParams.FILL_PARENT,
								LayoutParams.FILL_PARENT));
			}
			dialog.setContentView(layout);
			return dialog;
		}
	}
}
