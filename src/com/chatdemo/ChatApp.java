package com.chatdemo;

import com.parse.Parse;

import android.app.Application;

public class ChatApp extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, "UeRUlZwoXf28AKu83BHk8186TKMZJkDM5zOHw6qv",
				"95Yu6IFYyaXaJ20E1Zu1DhwWjBOwBitFngBgbfrq");
	}
}
