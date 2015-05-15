package com.igo.ui.android.remote;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Command {
	public static final String TASK_COMMIT = "taskCommit";
	public static final String LOGIN = "login";
	public static final String SHOW = "show";
	
	private String command = null;
	private Map<String, String> params = new HashMap<String, String>(0);
	
	public Command(String command){
		this.command = command;
	}
	
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	
	public String getParam(String key) {
		return (params == null)?null:params.get(key);
	}
	
	public void putParam(String key, String value) {
		this.params.put(key, value);
	}
	
	public String getParamsUrl(){
		String url = "";
		for(Iterator<String> itr = params.keySet().iterator(); itr.hasNext();){
			String key = itr.next();
			String value = params.get(key);
			
			url = ("".equals(url))?url:url + "&";
			url += key + "=" + value;
		}
		return url;
	}
}
