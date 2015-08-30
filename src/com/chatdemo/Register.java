package com.chatdemo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.ramananda.custom.CustomAcivity;
import com.ramananda.utils.DialogMsg;

public class Register extends CustomAcivity {

	private EditText etUser;
	private EditText etPass;
	private EditText etEmail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		setTouchClick(R.id.btnRegistration);

		etUser = (EditText) findViewById(R.id.etUser);
		etPass = (EditText) findViewById(R.id.etPwd);
		etEmail = (EditText) findViewById(R.id.etEmail);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		String us = etUser.getText().toString();
		String pw = etPass.getText().toString();
		String em = etEmail.getText().toString();

		if (us.length() == 0 || pw.length() == 0 || em.length() == 0) {
			DialogMsg.showDialog(this, R.string.err_fields_empty);
			return;
		}
		final ProgressDialog dia = ProgressDialog.show(this, null,
				getString(R.string.alert_wait));

		final ParseUser pu = new ParseUser();

		pu.setEmail(em);
		pu.setPassword(pw);
		pu.setUsername(us);

		pu.signUpInBackground(new SignUpCallback() {

			@Override
			public void done(ParseException e) {
				dia.dismiss();
				if (e == null) {
					UserListActivity.pUser = pu;
					startActivity(new Intent(Register.this,
							UserListActivity.class));
					setResult(RESULT_OK);
					finish();
				} else {
					DialogMsg.showDialog(
							Register.this,
							getString(R.string.err_singup) + " "
									+ e.getMessage());
					e.printStackTrace();
				}
			}
		});
	}
}
