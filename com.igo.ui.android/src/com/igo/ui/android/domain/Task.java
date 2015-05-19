package com.igo.ui.android.domain;

public class Task {
	private String id;
	private String name;
	private String body;
	private String type;
	private String replyVariants;
	private String startDate;
	private String endDate;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getReplyVariants() {
		return replyVariants;
	}

	public void setReplyVariants(String replyVariants) {
		this.replyVariants = replyVariants;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
