package com.igo.ui.android.remote;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.igo.ui.android.DataStorage;
import com.igo.ui.android.domain.Button;
import com.igo.ui.android.domain.Login;
import com.igo.ui.android.domain.Task;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class CommandConnector extends AsyncTask<String, String, String> {
	private OnCommandEndListener onCommandEndListener = null;
	private Command command = null;
	private Context context = null;

	public CommandConnector(Context context, Command command) {
		super();
		this.command = command;
		this.context = context;
	}

	@Override
	protected String doInBackground(String... params) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String prefServerAddress = sharedPref.getString("prefServerAddress",
				"192.168.0.101:8080");
		if (command.getParam("login") == null) {
			Login login = null;
			DataStorage ds = (DataStorage) context;
			login = (Login) ds.getData("login");
			String username = "nouser";
			if (login != null) {
				username = login.getLogin();
			} else {
				username = sharedPref.getString("login", "nouser");
			}
			command.putParam("login", username);
		}
		String paramsUrl = command.getParamsUrl();
		String urlString = "http://" + prefServerAddress
				+ "/com.igo.server/json/" + command.getCommand() + "?"
				+ paramsUrl;

		String data = "";
		InputStream in = null;

		// HTTP Get
		try {
			System.out.println("try connect to " + urlString);
			URL url = new URL(urlString);

			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setDoOutput(false);
			con.setDoInput(true);
			con.setRequestProperty("User-Agent", "Mozilla/5.0 ( compatible ) ");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");

			System.out.println("...ResponseCode=" + con.getResponseCode());

			in = new BufferedInputStream(con.getInputStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buffer = new byte[128];
			int length;
			while ((length = in.read(buffer)) != -1) {
				out.write(buffer, 0, length);
			}
			byte[] result = out.toByteArray();

			data = new String(result);
			con.disconnect();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			data = "{'error': '" + e.getMessage() + "'}";
		}

		System.out.println("data=" + data);
		return data;
	}

	@Override
	protected void onPostExecute(String result) {
		System.out.println("CommandConnector.onPostExecute.result=" + result);
		try {
			Object objResult = null;
			if (Command.LOGIN.equals(command.getCommand())) {
				JSONObject jObj = new JSONObject(result);
				if (jObj.has("login")) {
					Login login = new Login();
					login.setLogin(jObj.getString("login"));
					login.setName(jObj.getString("username"));
					objResult = login;
				}
			} else if (Command.TASK_COMMIT.equals(command.getCommand())) {
				JSONObject jObj = new JSONObject(result);
				objResult = jObj.getString("result");
			} else if (Command.SHOW.equals(command.getCommand())) {
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
					if(jButtons != null){
						Button[] b = new Button[jButtons.length()];
						for(int k = 0; k < jButtons.length(); k++){
							JSONObject jBtn = jButtons.getJSONObject(k);
							b[k] = new Button();
							b[k].setCode(getJsonValue(jBtn, "code"));
							b[k].setName(getJsonValue(jBtn, "name"));
							b[k].setReplystatus(getJsonValue(jBtn, "replystatus"));
						}
						task.setButtons(b);
					}
					tasks[i] = task;
				}
				objResult = tasks;
			}

			doCommandEnd(objResult);
		} catch (JSONException e) {
			e.printStackTrace();
			doCommandEnd(null);
		}
	}

	private String getJsonValue(JSONObject obj, String name) {
		try {
			return obj.getString(name);
		} catch (JSONException e) {
			return "";
		}
	}

	private void doCommandEnd(Object result) {
		if (onCommandEndListener == null) {
			System.out
					.println("CommandConnector.onCommandEndListener is undefined");
			return;
		}
		onCommandEndListener.OnCommandEnd(command, result);
	}

	public OnCommandEndListener getOnCommandEndListener() {
		return onCommandEndListener;
	}

	public void setOnCommandEndListener(
			OnCommandEndListener onCommandEndListener) {
		this.onCommandEndListener = onCommandEndListener;
	}

}
