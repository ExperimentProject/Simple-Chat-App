package com.chatdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.ramananda.custom.CustomAcivity;
import com.ramananda.utils.DialogMsg;

public class MainActivity extends CustomAcivity {

	private EditText user;
	private EditText pass;

	private static final int REGISTER_REQUEST = 100;

	Button btnLogin, btnRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		user = (EditText) findViewById(R.id.user);
		pass = (EditText) findViewById(R.id.pwd);

		setTouchClick(R.id.btnLogin);
		setTouchClick(R.id.btnReg);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.btnReg:
			Intent intent = new Intent(MainActivity.this, Register.class);
			startActivityForResult(intent, REGISTER_REQUEST);
			break;
		case R.id.btnLogin:
			String un = user.getText().toString();
			String ps = pass.getText().toString();

			if (un.length() == 0 || ps.length() == 0) {
				DialogMsg.showDialog(this, R.string.err_fields_empty);
				return;
			}

			final ProgressDialog pBar = ProgressDialog.show(this, null,
					getString(R.string.alert_wait));

			ParseUser.logInInBackground(un, ps, new LogInCallback() {

				@Override
				public void done(ParseUser puser, ParseException e) {
					pBar.dismiss();
					if (puser != null) {
						UserListActivity.pUser = puser;
						startActivity(new Intent(MainActivity.this,
								UserListActivity.class));
						finish();
					} else {
						DialogMsg.showDialog(
								MainActivity.this,
								getString(R.string.err_login) + " "
										+ e.getMessage());
						e.printStackTrace();
					}
				}
			});
			
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REGISTER_REQUEST && resultCode == RESULT_OK) {
			finish();
		}
	}
}
