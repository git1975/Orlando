package com.igo.ui.android.service;

import java.util.Timer;

import com.igo.ui.android.remote.Command;
import com.igo.ui.android.timer.ChatCheckTimerTask;
import com.igo.ui.android.timer.ChatTimerTask;

import android.app.AlarmManager;
import android.app.PendingIntent;
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

		// timer.schedule(new ChatCheckTimerTask(context, null, "demo"), 0,
		// 5000);

		/*Toast.makeText(context, "BootBroadcastReceiver.onReceive",
				Toast.LENGTH_LONG).show();
		
		Intent intent2 = new Intent(context, RemoteBroadcastReceiver.class);

		final PendingIntent pIntent = PendingIntent.getBroadcast(context, 123,
				intent2, PendingIntent.FLAG_UPDATE_CURRENT);

		AlarmManager alarm = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, pIntent);*/

		// android.os.Debug.waitForDebugger();

		// Toast.makeText(context, "BootBroadcastReceiver.onReceive",
		// Toast.LENGTH_LONG).show();

		// context.startService(new Intent(context, RemoteService.class));

		// context.startService(new Intent(context,
		// RemoteService.class).putExtra(
		// Command.PARAM_COMMAND, "command.getJson()"));
	}

}
