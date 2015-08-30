package com.chatdemo;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.ramananda.custom.CustomAcivity;
import com.ramananda.utils.Const;
import com.ramananda.utils.DialogMsg;

public class UserListActivity extends CustomAcivity {

	private ArrayList<ParseUser> uList;
	public static ParseUser pUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_list_activity);

		getActionBar().setDisplayHomeAsUpEnabled(false);
		updateUserStatus(true);
		loadUserList();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		updateUserStatus(false);
	}

	private void updateUserStatus(boolean online) {
		pUser.put("online", online);
		pUser.saveEventually();
	}

	private void loadUserList() {
		final ProgressDialog dialog = ProgressDialog.show(this, null,
				getString(R.string.alert_loading));

		ParseUser.getQuery().whereNotEqualTo("username", pUser.getUsername())
				.findInBackground(new FindCallback<ParseUser>() {

					@Override
					public void done(List<ParseUser> ls, ParseException e) {
						dialog.dismiss();
						if (ls != null) {
							if (ls.size() == 0) {
								Toast.makeText(UserListActivity.this,
										R.string.msg_no_user_found,
										Toast.LENGTH_SHORT).show();

							} else {
								uList = new ArrayList<ParseUser>(ls);
								ListView listView = (ListView) findViewById(R.id.list);
								listView.setAdapter(new UserListAdapter());

								listView.setOnItemClickListener(new OnItemClickListener() {

									@Override
									public void onItemClick(
											AdapterView<?> prent, View view,
											int position, long id) {
										startActivity(new Intent(
												UserListActivity.this,
												ChatActivity.class).putExtra(
												Const.EXTRA_DATA,
												uList.get(position)
														.getUsername()));

									}
								});
							}
						} else {
							DialogMsg.showDialog(
									UserListActivity.this,
									getString(R.string.err_users) + " "
											+ e.getMessage());
							e.printStackTrace();
						}
					}
				});
	}

	private class UserListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return uList.size();
		}

		@Override
		public ParseUser getItem(int position) {
			return uList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null)
				convertView = getLayoutInflater().inflate(R.layout.chat_items,
						null);
			ParseUser pus = getItem(position);

			TextView lb = (TextView) convertView.findViewById(R.id.textlebel);
			lb.setText(pus.getUsername());
			lb.setCompoundDrawablesWithIntrinsicBounds(
					pus.getBoolean("online") ? R.drawable.ic_online
							: R.drawable.ic_offline, 0, R.drawable.arrow, 0);
			return convertView;
		}

	}
}
