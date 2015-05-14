package com.igo.ui.android.remote;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.igo.ui.android.MainActivity;
import com.igo.ui.android.domain.Login;

import android.os.AsyncTask;

public class LoginConnector extends AsyncTask<String, String, String> {
	private MainActivity adapter = null;

	public LoginConnector(MainActivity adapter) {
		super();
		this.adapter = adapter;
	}

	@Override
	protected String doInBackground(String... params) {
		String urlString = params[0];
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
			// Base64.decode(input, flags)
		} catch (Exception e) {
			System.out.println(e.getMessage());
			data = "{'task': []}";
		}

		System.out.println("data=" + data);
		return data;
	}

	@Override
	protected void onPostExecute(String result) {
		System.out.println("LoginConnector.onPostExecute.result=" + result);
		try {
			JSONObject jObj = new JSONObject(result);
			Login login = new Login();
			login.setName(jObj.getString("username"));
			adapter.endLogin(login);
		} catch (JSONException e) {
			//e.printStackTrace();
			adapter.endLogin(null);
		}		
	}

}
