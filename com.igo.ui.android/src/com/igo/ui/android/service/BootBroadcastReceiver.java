package com.igo.ui.android.service;

import com.igo.ui.android.remote.Command;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootBroadcastReceiver extends BroadcastReceiver {
	public BootBroadcastReceiver() {
		super();
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("BootBroadcastReceiver.onReceive->");
		
		//android.os.Debug.waitForDebugger(); 
		
		//Toast.makeText(context, "BootBroadcastReceiver.onReceive",
		//		Toast.LENGTH_LONG).show();
		
		//context.startService(new Intent(context, RemoteService.class));
		
		//context.startService(new Intent(context, RemoteService.class).putExtra(
		//		Command.PARAM_COMMAND, "command.getJson()"));
	}

}
