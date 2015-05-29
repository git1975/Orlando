package com.igo.ui.android.domain;

import java.util.Date;

public class ChatMessage {
	private String id;
	private String from;
	private String to;
	private Date sendDate;
	private String body;

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getId() {
		return id;
	}

	public long getIdLong() {
		try {
			return Long.parseLong(id);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public void setId(String id) {
		this.id = id;
	}
}
