/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.igo.ui.android;

import com.igo.ui.android.domain.Login;
import com.igo.ui.android.remote.LoginConnector;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
	Button btnLogin;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(this.getApplicationContext());
		String login = sharedPref.getString("login", "-");

		if ("-".equals(login)) {
			setContentView(R.layout.fragment_userlogin);
			btnLogin = (Button) findViewById(R.id.btnLogin);
			btnLogin.setOnClickListener(this);
			return;
		}

		startActivity(new Intent(this, WorkActivity.class));
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLogin:
			EditText etLogin = (EditText) findViewById(R.id.et_login);
			EditText etPassword = (EditText) findViewById(R.id.et_password);
			processLogin(etLogin.getText().toString(), etPassword.getText()
					.toString());
			break;
		case 2:
			// i = new Intent(this, SignUpActivity.class);
			break;
		}
	}

	private boolean processLogin(String login, String password) {
		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String prefServerAddress = sharedPref.getString("prefServerAddress",
				"192.168.0.101:8080");

		LoginConnector con = new LoginConnector(this);
		con.execute("http://" + prefServerAddress
				+ "/com.igo.server/json/login?login=" + login + "&password="
				+ password);

		return true;
	}

	public void endLogin(Login login) {
		System.out.println("endLogin." + login);
		if (login == null) {
			return;
		}
		Intent i = new Intent(this, WorkActivity.class);
		startActivity(i);
	}
}
