package com.igo.ui.android.remote;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.igo.ui.android.adapter.TaskViewAdapter;
import com.igo.ui.android.domain.Task;

import android.os.AsyncTask;
import android.util.Base64;

public class JsonConnector extends AsyncTask<String, String, String> {
	private TaskViewAdapter adapter = null;

	public JsonConnector(TaskViewAdapter adapter) {
		super();
		this.adapter = adapter;
	}

	private void setProxy() {
		System.setProperty("http.proxyHost", "172.19.56.30");
		System.setProperty("http.proxyPort", "8080");

		System.setProperty("http.proxyUser", "u_m059r");
		System.setProperty("http.proxyPassword", "");
	}

	@Override
	protected String doInBackground(String... params) {
		// URL to call
		String urlString = params[0];
		String data = "";
		InputStream in = null;

		// HTTP Get
		try {
			System.out.println("try connect to " + urlString);
			URL url = new URL(urlString);

//			 setProxy();
//			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
//					"172.19.56.30", 8080));
//			Authenticator authenticator = new Authenticator() {
//
//				public PasswordAuthentication getPasswordAuthentication() {
//					return (new PasswordAuthentication("u_m059r",
//							"".toCharArray()));
//				}
//			};
//			Authenticator.setDefault(authenticator);
			HttpURLConnection con = (HttpURLConnection) url
					.openConnection();
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
			//Base64.decode(input, flags)
		} catch (Exception e) {
			System.out.println(e.getMessage());
			data = "{'task': []}";
		}

		// String json =
		// "{'task': [{'id': '1', 'name': 'task1'},{'id': '2', 'name': 'task2'}]}";

		System.out.println("data=" + data);
		return data;
	}

	@Override
	protected void onPostExecute(String result) {
		Task[] tasks = null;
		try {
			//JSONArray jArr = new JSONObject(result).getJSONArray("task");
			JSONArray jArr = new JSONArray(result);
			tasks = new Task[jArr.length()];
			for (int i = 0; i < jArr.length(); i++) {
				JSONObject jObj = jArr.getJSONObject(i);
				Task task = new Task();
				task.setId(jObj.getString("id"));
				task.setName(jObj.getString("name"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		adapter.endRefresh(tasks);
	}

}
