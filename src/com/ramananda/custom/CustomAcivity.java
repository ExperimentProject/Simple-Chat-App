package com.ramananda.custom;

import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;

import com.chatdemo.R;
import com.ramananda.utils.TouchEffect;

public class CustomAcivity extends FragmentActivity implements OnClickListener {

	public static final TouchEffect TOUCH_EFFECT = new TouchEffect();

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		setupActionBar();
	}

	protected void setupActionBar() {
		final ActionBar actionBar = getActionBar();
		if (actionBar == null)
			return;
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayUseLogoEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.actionbar_bg));
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}

	public View setTouchClick(int id) {
		View v = setClick(id);
		if (v != null) {
			v.setOnTouchListener(TOUCH_EFFECT);
		}
		return v;
	}

	@Override
	public void onClick(View v) {

	}

	public View setClick(int id) {
		View v = findViewById(id);
		v.setOnClickListener(this);
		return v;
	}
}
