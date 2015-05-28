package com.igo.ui.android.remote;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.igo.ui.android.domain.Button;
import com.igo.ui.android.domain.ChatMessage;
import com.igo.ui.android.domain.Login;
import com.igo.ui.android.domain.Task;

public class CommandResponseFactory {
	private static final SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.ROOT);

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
					JSONArray jButtons = jObj.getJSONArray("buttons");
					Task task = new Task();
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
			} else if (Command.GET_CHAT.equals(command)) {

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
					String d = getJsonValue(jObj, "senddate");
					d = d.replace("Z", "+0000");
					GregorianCalendar cal = new GregorianCalendar();
					cal.setTime(new Date());
					System.out.println(sdfFull.format(new Date()));
					if(!"".equals(d)){
						try {
							item.setSendDate(sdfFull.parse(d));
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
					
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
