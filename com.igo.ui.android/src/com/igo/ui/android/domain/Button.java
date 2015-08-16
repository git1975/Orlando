package com.igo.ui.android.domain;

public class Button {
	private String code;
	private String name;
	private String replystatus;
	private Register register;
	
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
	public String getReplystatus() {
		return replystatus;
	}
	public void setReplystatus(String replystatus) {
		this.replystatus = replystatus;
	}
	public Register getRegister() {
		return register;
	}
	public void setRegister(Register register) {
		this.register = register;
	}
}
