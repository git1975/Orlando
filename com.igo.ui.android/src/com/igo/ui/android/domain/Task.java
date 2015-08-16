package com.igo.ui.android.domain;

public class Task {
	private String id;
	private String name;
	private String body;
	private String type;
	private String forStatus;
	private String status;
	private String startDate;
	private String endDate;
	private Button[] buttons;
	private int color;
	private String replytext;
	private String registers;

	public String getId() {
		return id;
	}

	public String getReplytext() {
		return replytext;
	}

	public void setReplytext(String replytext) {
		this.replytext = replytext;
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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Button[] getButtons() {
		return buttons;
	}

	public void setButtons(Button[] buttons) {
		this.buttons = buttons;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getForStatus() {
		return forStatus;
	}

	public void setForStatus(String forStatus) {
		this.forStatus = forStatus;
	}

	public String getRegisters() {
		return registers;
	}

	public void setRegisters(String registers) {
		this.registers = registers;
	}
}
