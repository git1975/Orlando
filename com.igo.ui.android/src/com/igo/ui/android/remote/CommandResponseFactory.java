package com.igo.ui.android.remote;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.igo.ui.android.domain.Button;
import com.igo.ui.android.domain.ChatMessage;
import com.igo.ui.android.domain.Login;
import com.igo.ui.android.domain.Task;

public class CommandResponseFactory {

	public static Object getCommandResponseObject(String command, String result) {
		Object objResult = null;
		try {
			if (Command.LOGIN.equals(command)) {
				JSONObject jObj;
				jObj = new JSONObject(result);
				if (jObj.has("login")) {
					Login login = new Login();
					login.setLogin(jObj.getString("login"));
					login.setName(jObj.getString("username"));
					objResult = login;
				}
			} else if (Command.TASK_COMMIT.equals(command)) {
				JSONObject jObj = new JSONObject(result);
				objResult = jObj.getString("result");
			} else if (Command.SHOW.equals(command)) {
				Task[] tasks = null;
				JSONArray jArr = new JSONArray(result);
				tasks = new Task[jArr.length()];
				for (int i = 0; i < jArr.length(); i++) {
					JSONObject jObj = jArr.getJSONObject(i);
					JSONArray jButtons = null;
					Object jBut = jObj.get("buttons");
					if(jBut != null && !"null".equals(jBut.toString())){
						jButtons = jObj.getJSONArray("buttons");
					}
					Task task = new Task();
					
					String type = getJsonValue(jObj, "type");
					//Special message type CLEAR, if there are no messages
					if("CLEAR".equals(type) && jArr.length() == 0){
						task.setType(type);
						tasks[i] = task;
						break;
					}
					
					task.setId(getJsonValue(jObj, "id"));
					task.setName(getJsonValue(jObj, "name"));
					task.setStartDate(getJsonValue(jObj, "startdate"));
					task.setEndDate(getJsonValue(jObj, "enddate"));
					task.setType(getJsonValue(jObj, "type"));
					task.setBody(getJsonValue(jObj, "body"));
					task.setStatus(getJsonValue(jObj, "status"));
					if (jButtons != null) {
						Button[] b = new Button[jButtons.length()];
						for (int k = 0; k < jButtons.length(); k++) {
							JSONObject jBtn = jButtons.getJSONObject(k);
							b[k] = new Button();
							b[k].setCode(getJsonValue(jBtn, "code"));
							b[k].setName(getJsonValue(jBtn, "name"));
							b[k].setReplystatus(getJsonValue(jBtn,
									"replystatus"));
						}
						task.setButtons(b);
					}
					tasks[i] = task;
				}
				objResult = tasks;
			} else if (Command.GET_CHAT.equals(command) || Command.SEND_CHAT.equals(command)) {

				ChatMessage[] items = null;
				JSONArray jArr = new JSONArray(result);

				items = new ChatMessage[jArr.length()];
				for (int i = 0; i < jArr.length(); i++) {
					JSONObject jObj = jArr.getJSONObject(i);
					ChatMessage item = new ChatMessage();
					item.setId(getJsonValue(jObj, "id"));
					item.setFrom(getJsonValue(jObj, "sendfrom"));
					item.setTo(getJsonValue(jObj, "sendto"));
					item.setBody(getJsonValue(jObj, "body"));
					Long time = 0L;
					try {
						time = Long.decode(getJsonValue(jObj, "sendtime"));
					} catch (Exception e) {
						time = 0L;
					}
					Date d = new Date();
					d.setTime(time);
					item.setSendDate(d);

					items[i] = item;
				}
				objResult = items;
			}
		} catch (JSONException e) {
			e.printStackTrace();

			objResult = null;
		}

		return objResult;
	}

	private static String getJsonValue(JSONObject obj, String name) {
		try {
			return obj.getString(name);
		} catch (JSONException e) {
			return "";
		}
	}
}
