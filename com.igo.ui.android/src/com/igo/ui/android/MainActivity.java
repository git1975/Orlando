package com.igo.ui.android;

import com.igo.ui.android.domain.Login;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;
import com.igo.ui.android.remote.OnCommandEndListener;
import com.igo.ui.android.service.RemoteBroadcastReceiver;
import com.igo.ui.android.service.RemoteService;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Login main form
 *
 */
public class MainActivity extends ActionBarActivity implements OnClickListener,
		OnCommandEndListener {
	Button btnLogin;
	private final BroadcastReceiver rbr = new RemoteBroadcastReceiver(this);

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences sharedPref = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String login = sharedPref.getString("login", "");

		setContentView(R.layout.fragment_userlogin);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(this);

		EditText etLogin = (EditText) findViewById(R.id.et_login);
		etLogin.setText(login);

		registerReceiver(rbr,
				new IntentFilter(RemoteService.IGO_SERVICE_ACTION));
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(rbr);

		super.onDestroy();
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

	/**
	 * Server login method invocation
	 * 
	 * @param login
	 * @param password
	 * @return
	 */
	private boolean processLogin(String login, String password) {
		Command command = new Command(Command.LOGIN);
		command.putParam("login", login);
		command.putParam("password", password);

		/*
		 * CommandConnector con = new CommandConnector(getApplicationContext(),
		 * command); con.setOnCommandEndListener(this); con.execute("");
		 */

		startService(new Intent(this, RemoteService.class).putExtra(
				Command.PARAM_COMMAND, command.getJson()));

		return true;
	}

	/**
	 * When server response ends (login)
	 */
	public void OnCommandEnd(Command command, Object result) {
		if (Command.LOGIN.equals(command.getCommand())) {
			Login login = (Login) result;
			System.out.println("endLogin."
					+ ((login != null) ? login.getName() : "login failed"));
			TextView tvLoginmsg = (TextView) findViewById(R.id.tv_loginmsg);
			if (login == null) {
				Toast.makeText(getApplicationContext(),
						tvLoginmsg.getText().toString(), Toast.LENGTH_LONG)
						.show();
				tvLoginmsg.setVisibility(View.VISIBLE);
				return;
			}

			// Persist login info
			DataStorage ds = (DataStorage) getApplicationContext();
			ds.setData("login", login);

			SharedPreferences sharedPref = PreferenceManager
					.getDefaultSharedPreferences(getApplicationContext());
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString("login", login.getLogin());
			editor.commit();

			tvLoginmsg.setVisibility(View.INVISIBLE);
			Intent i = new Intent(this, WorkActivity.class);
			startActivity(i);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public String getLastHash() {
		return null;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mi_edit_settings: {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		case R.id.mi_app_quit: {
			finish();
			return true;
		}
		}

		return false;
	}

	@Override
	public void onBackPressed() {
		finish();
	}
}
