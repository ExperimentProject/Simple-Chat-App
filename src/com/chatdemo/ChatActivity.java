package com.chatdemo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.rama.model.Conversation;
import com.ramananda.custom.CustomAcivity;
import com.ramananda.utils.Const;

public class ChatActivity extends CustomAcivity {

	private ArrayList<Conversation> convList;
	private ChatViewAdapter adapter;

	private EditText txtMassage;
	private String buddy;

	private Date lastMsgDate;

	private boolean isRunning;

	private static Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity);

		convList = new ArrayList<Conversation>();
		ListView lv = (ListView) findViewById(R.id.list);

		adapter = new ChatViewAdapter();
		lv.setAdapter(adapter);
		lv.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		lv.setStackFromBottom(true);

		txtMassage = (EditText) findViewById(R.id.txt);
		txtMassage.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);

		setTouchClick(R.id.btnSend);

		buddy = getIntent().getStringExtra(Const.EXTRA_DATA);
		getActionBar().setTitle(buddy);

		handler = new Handler();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isRunning = true;
		loadConversatonList();
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v.getId() == R.id.btnSend) {
			sendMessage();
		}
	}

	private void sendMessage() {
		if (txtMassage.length() == 0)
			return;

		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		imm.hideSoftInputFromWindow(txtMassage.getWindowToken(), 0);

		String ms = txtMassage.getText().toString();

		final Conversation c = new Conversation(ms, new Date(),
				UserListActivity.pUser.getUsername());

		c.setStatus(Conversation.STATUS_SENDING);
		convList.add(c);
		adapter.notifyDataSetChanged();
		txtMassage.setText(null);

		ParseObject parseObject = new ParseObject("Chat");

		parseObject.put("sender", UserListActivity.pUser.getUsername());

		parseObject.put("receiver", buddy);
		parseObject.put("message", ms);

		parseObject.saveEventually(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					c.setStatus(Conversation.STATUS_SEN);
				} else {
					c.setStatus(Conversation.STATUS_FAILED);
					adapter.notifyDataSetChanged();
				}
			}
		});

	}

	private void loadConversatonList() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Chat");

		if (convList.size() == 0) {
			ArrayList<String> all = new ArrayList<String>();
			all.add(buddy);
			all.add(UserListActivity.pUser.getUsername());

			query.whereContainedIn("sender", all);
			query.whereContainedIn("receiver", all);
		} else {
			if (lastMsgDate != null) {
				query.whereGreaterThan("createdAt", lastMsgDate);
				query.whereEqualTo("sender", buddy);
				query.whereEqualTo("receiver",
						UserListActivity.pUser.getUsername());
			}
		}

		query.orderByDescending("createdAt");
		query.setLimit(30);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> obj, ParseException e) {
				if (obj != null && obj.size() > 0) {
					for (int i = obj.size() - 1; i >= 0; i--) {
						ParseObject po = obj.get(i);
						Conversation cv = new Conversation(po
								.getString("message"), po.getCreatedAt(), po
								.getString("sender"));

						convList.add(cv);
						if (lastMsgDate == null
								|| lastMsgDate.before(cv.getDate()))
							lastMsgDate = cv.getDate();
						adapter.notifyDataSetChanged();
					}
				}
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						if (isRunning) {
							loadConversatonList();
						}
					}
				}, 1000);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	private class ChatViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return convList.size();
		}

		@Override
		public Conversation getItem(int position) {
			return convList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Conversation c = getItem(position);
			if (c.isSent()) {
				convertView = getLayoutInflater().inflate(
						R.layout.chat_item_receive, null);
			} else {
				convertView = getLayoutInflater().inflate(
						R.layout.chat_items_send, null);
			}

			TextView time = (TextView) convertView.findViewById(R.id.lbl1);
			time.setText(DateUtils.getRelativeDateTimeString(ChatActivity.this,
					c.getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
					DateUtils.DAY_IN_MILLIS, 0));

			TextView msg = (TextView) convertView.findViewById(R.id.lbl2);

			msg.setText(c.getMsg());
			TextView delevaryStatus = (TextView) convertView
					.findViewById(R.id.lbl3);

			if (c.isSent()) {
				if (c.getStatus() == Conversation.STATUS_SEN) {
					delevaryStatus.setText("Delivered");
				} else if (c.getStatus() == Conversation.STATUS_SENDING)
					delevaryStatus.setText("Sending...");
				else
					delevaryStatus.setText("Failed");
			} else {
				delevaryStatus.setText("");
			}

			return convertView;
		}

	}
}
