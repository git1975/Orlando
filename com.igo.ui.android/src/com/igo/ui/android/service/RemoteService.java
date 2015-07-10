package com.igo.ui.android.service;

import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;
import com.igo.ui.android.remote.OnCommandEndListener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RemoteService extends Service implements OnCommandEndListener{
	public static final String IGO_SERVICE_ACTION = "com.igo.ui.android.service.RemoteService";

	public RemoteService() {
		super();
	}

	public void onCreate() {
		super.onCreate();
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		System.out.println("RemoteService.onStartCommand->");
		String json = intent.getStringExtra(Command.PARAM_COMMAND);
		Command command = new Command();
		command.parseJson(json);
		
		CommandConnector con = new CommandConnector(getApplicationContext(), command); 
		con.setOnCommandEndListener(this); 
		con.execute();
		
		return super.onStartCommand(intent, flags, startId);
	}

	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public void OnCommandEnd(Command command, Object result) {
		Intent intent = new Intent(IGO_SERVICE_ACTION);
		intent.putExtra(Command.PARAM_COMMAND, command.getJson());
		intent.putExtra(Command.PARAM_RESULT, result.toString());
        getApplicationContext().sendBroadcast(intent);
	}

	public String getLastHash() {
		return null;
	}
}
