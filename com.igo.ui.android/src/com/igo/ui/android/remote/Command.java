package com.igo.ui.android.remote;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class Command {
	public static final String TASK_COMMIT = "taskCommit";
	public static final String LOGIN = "login";
	public static final String SHOW = "show";
	public static final String REPLY = "reply";
	public static final String GET_CHAT = "getchat";
	public static final String SEND_CHAT = "sendchat";
	public static final String GET_CHATS = "getchats";
	public static final String GET_CHATSTATUS = "getchatstatus";
	
	public static final String PARAM_COMMAND = "command";
	public static final String PARAM_PARAMS = "params";
	public static final String PARAM_RESULT = "result";
	
	private String command = null;
	private Map<String, String> params = new HashMap<String, String>(0);
	
	public Command(){
	}
	
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

	/**
	 * Сериализация в строку Json<br/>
	 * example:{"command":"login","params":{"password":"1","login":"user1"}}
	 * @return
	 */
	public String getJson(){
		JSONObject jParams = new JSONObject();
		for(Iterator<String> itr = params.keySet().iterator(); itr.hasNext();){
			String key = itr.next();
			String value = params.get(key);
			
			try {
				jParams.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		JSONObject jCmd = new JSONObject();
		try {
			jCmd.put(PARAM_COMMAND, command);
			jCmd.put(PARAM_PARAMS, jParams);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jCmd.toString();
	}
	
	public void parseJson(String json){
		try {
			JSONObject jCmd = new JSONObject(json);
			setCommand((String)jCmd.get(PARAM_COMMAND));
			JSONObject jParams = jCmd.getJSONObject(PARAM_PARAMS);
			params.clear();
			for(Iterator<String> itr = jParams.keys(); itr.hasNext();){
				String key = itr.next();
				String value = jParams.getString(key);
				params.put(key, value);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
