package com.igo.ui.android.remote;

import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.igo.ui.android.domain.Button;
import com.igo.ui.android.domain.ChatMessage;
import com.igo.ui.android.domain.ChatsItem;
import com.igo.ui.android.domain.Login;
import com.igo.ui.android.domain.Register;
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
					login.setRole(jObj.getString("role"));
					objResult = login;
				}
			} else if (Command.TASK_COMMIT.equals(command)) {
				JSONObject jObj = new JSONObject(result);
				objResult = jObj.getString("result");
			} else if (Command.REPLY.equals(command)) {
				JSONObject jObj = new JSONObject(result);
				objResult = jObj.getString("result");
			} else if (Command.SHOW.equals(command)) {
				Task[] tasks = null;
				JSONArray jArr = new JSONArray(result);
				tasks = new Task[jArr.length()];
				for (int i = 0; i < jArr.length(); i++) {
					JSONObject jObj = jArr.getJSONObject(i);
					Task task = parseTaskJson(jObj);
					tasks[i] = task;
				}
				objResult = tasks;
			} else if (Command.GET_CHAT.equals(command)
					|| Command.SEND_CHAT.equals(command)) {

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
					item.setChatcode(getJsonValue(jObj, "chatcode"));

					String message = getJsonValue(jObj, "message");
					if (message != null && !"".equals(message)
							&& !"null".equals(message)) {
						JSONObject jTask = new JSONObject(message);
						item.setTask(parseTaskJson(jTask));
					}

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
			} else if (Command.GET_CHATS.equals(command)) {
				ChatsItem[] items = null;
				JSONArray jArr = new JSONArray(result);

				items = new ChatsItem[jArr.length()];
				for (int i = 0; i < jArr.length(); i++) {
					JSONObject jObj = jArr.getJSONObject(i);
					ChatsItem item = new ChatsItem();
					item.setCode(getJsonValue(jObj, "code"));
					item.setName(getJsonValue(jObj, "name"));
					item.setIspersonal(getJsonBoolean(jObj, "ispersonal"));

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

	public static String getJsonValue(JSONObject obj, String name) {
		try {
			return obj.getString(name);
		} catch (JSONException e) {
			return "";
		}
	}

	public static Boolean getJsonBoolean(JSONObject obj, String name) {
		try {
			return obj.getBoolean(name);
		} catch (JSONException e) {
			return false;
		}
	}

	public static int getJsonInt(JSONObject obj, String name) {
		try {
			return Integer.parseInt(obj.getString(name));
		} catch (JSONException e) {
			return 0;
		}
	}

	public static Task parseTaskJson(JSONObject jObj) throws JSONException {
		Task task = new Task();
		if (!"".equals(getJsonValue(jObj, "replytext"))) {
			task.setReplytext(getJsonValue(jObj, "replytext"));
			return task;
		}

		task.setId(getJsonValue(jObj, "id"));
		task.setName(getJsonValue(jObj, "name"));
		task.setStartDate(getJsonValue(jObj, "dt1"));
		task.setEndDate(getJsonValue(jObj, "dt2"));
		task.setType(getJsonValue(jObj, "type"));
		task.setBody(getJsonValue(jObj, "body"));
		task.setStatus(getJsonValue(jObj, "status"));
		task.setForStatus(getJsonValue(jObj, "forStatus"));
		task.setColor(getJsonInt(jObj, "color"));
		task.setRegisters(getJsonValue(jObj, "registers"));
		// Добавляем кнопки
		JSONArray jButtons = null;
		Object jBut = jObj.get("buttons");
		if (jBut != null && !"null".equals(jBut.toString())) {
			jButtons = jObj.getJSONArray("buttons");
		}
		if (jButtons != null) {
			Button[] arr = new Button[jButtons.length()];
			for (int k = 0; k < jButtons.length(); k++) {
				JSONObject jBtn = jButtons.getJSONObject(k);
				arr[k] = new Button();
				arr[k].setCode(getJsonValue(jBtn, "code"));
				arr[k].setName(getJsonValue(jBtn, "name"));
				arr[k].setReplystatus(getJsonValue(jBtn, "replystatus"));
				// Добавляем значение регистра для кнопки
				JSONObject jReg = null;
				try {
					jReg = jBtn.getJSONObject("register");
				} catch (JSONException e) {
					jReg = null;
				}
				if (jReg != null && !"null".equals(jReg.toString())) {
					Register register = new Register();
					register.setCode(getJsonValue(jReg, "code"));
					register.setName(getJsonValue(jReg, "name"));
					register.setValue(getJsonValue(jReg, "value"));

					arr[k].setRegister(register);
				}
			}
			task.setButtons(arr);
		}

		return task;
	}
}
