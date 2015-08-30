package com.rama.model;

import java.util.Date;

import com.chatdemo.UserListActivity;

public class Conversation {
	public static final int STATUS_SENDING = 0;
	public static final int STATUS_SEN = 1;
	public static final int STATUS_FAILED = 2;

	private String msg;
	private int status = STATUS_SEN;

	private Date date;
	private String sender;

	public Conversation(String msg, Date date, String sender) {
		this.msg = msg;
		this.date = date;
		this.sender = sender;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Conversation() {
		// TODO Auto-generated constructor stub
	}

	public boolean isSent(){
		return UserListActivity.pUser.getUsername().equals(sender);
	}
	
}
