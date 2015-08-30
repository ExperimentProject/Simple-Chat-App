package com.ramananda.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class DialogMsg {
	public static AlertDialog showDialog(Context ctx, String msg, String btn1,
			String btn2, OnClickListener clickListener1,
			OnClickListener clickListener2) {

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setMessage(msg).setCancelable(false)
				.setPositiveButton(btn1, clickListener1);
		if (btn2 != null && clickListener2 != null)
			builder.setNegativeButton(btn2, clickListener2);

		AlertDialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public static AlertDialog showDialog(Context ctx, String msg, String btn1,
			String btn2, DialogInterface.OnClickListener listener) {

		return showDialog(ctx, msg, btn1, btn2, listener,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int id) {

						dialog.dismiss();
					}
				});

	}

	public static AlertDialog showDialog(Context ctx, int msg, int btn1,
			int btn2, DialogInterface.OnClickListener listener) {

		return showDialog(ctx, ctx.getString(msg), ctx.getString(btn1),
				ctx.getString(btn2), listener);

	}

	public static AlertDialog showDialog(Context ctx, String msg,
			DialogInterface.OnClickListener listener) {

		return showDialog(ctx, msg, ctx.getString(android.R.string.ok), null,
				listener, null);
	}

	public static AlertDialog showDialog(Context ctx, int msg,
			DialogInterface.OnClickListener listener) {

		return showDialog(ctx, ctx.getString(msg),
				ctx.getString(android.R.string.ok), null, listener, null);
	}

	public static AlertDialog showDialog(Context ctx, String msg) {

		return showDialog(ctx, msg, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {

				dialog.dismiss();
			}
		});

	}

	public static AlertDialog showDialog(Context ctx, int msg)
	{

		return showDialog(ctx, ctx.getString(msg));

	}
	
	// keyboard hide

	public static final void hideKeyboard(Activity activity) {
		if (activity.getCurrentFocus() != null) {
			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(activity.getCurrentFocus()
					.getWindowToken(), 0);
		}
	}

	public static final void hideKeyboard(Activity act, View v) {
		try {
			InputMethodManager imm = (InputMethodManager) act
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(),
					0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
