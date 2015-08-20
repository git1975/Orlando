package com.igo.ui.android.domain;

public class ChatsItem {
	private String code;
	private String name;
	private boolean ispersonal;
	private boolean ischild;
	
	public boolean isIschild() {
		return ischild;
	}
	public void setIschild(boolean ischild) {
		this.ischild = ischild;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isIspersonal() {
		return ispersonal;
	}
	public void setIspersonal(boolean ispersonal) {
		this.ispersonal = ispersonal;
	}

}
