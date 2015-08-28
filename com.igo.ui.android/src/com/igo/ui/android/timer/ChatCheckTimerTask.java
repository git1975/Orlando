package com.igo.ui.android.timer;

import java.util.Random;
import java.util.TimerTask;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.igo.ui.android.WorkActivity;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.OnCommandEndListener;
import com.igo.ui.android.service.RemoteBroadcastReceiver;
import com.igo.ui.android.service.RemoteService;

public class ChatCheckTimerTask extends TimerTask implements
		OnCommandEndListener {
	private long maxid = 0;
	private long minid = 0;
	private String chatcode;
	private final RemoteBroadcastReceiver rbr = new RemoteBroadcastReceiver();

	public long getMinid() {
		return minid;
	}

	public void setMinid(long minid) {
		this.minid = minid;
	}

	private OnCommandEndListener onCommandEndListener = null;
	private Context context = null;

	public ChatCheckTimerTask(Context context,
			OnCommandEndListener onCommandEndListener, String chatcode) {
		super();
		this.context = context;
		this.onCommandEndListener = onCommandEndListener;
		this.chatcode = chatcode;

		rbr.setCommandEndListener(this);
	}

	@Override
	public void run() {
		System.out.println("ChatCheckTimerTask.run");
		
		Toast.makeText(context, "ChatCheckTimerTask.run", Toast.LENGTH_LONG).show();

		Command command = new Command(Command.GET_CHAT);
		command.putParam("maxid", maxid + "");
		command.putParam("minid", minid + "");
		command.putParam("chatcode", chatcode);

		context.registerReceiver(rbr, new IntentFilter(
				RemoteService.IGO_SERVICE_ACTION));
		context.startService(new Intent(context, RemoteService.class).putExtra(
				Command.PARAM_COMMAND, command.getJson()));
	}

	public void OnCommandEnd(Command command, Object result) {
		context.unregisterReceiver(rbr);

		createNotification("QQQqqq");
		/*
		 * if(!Command.GET_CHAT.equals(command.getCommand())){ return; }
		 * ChatMessage[] items = (ChatMessage[]) result;
		 * 
		 * if (items != null) { long id = 0; for (ChatMessage item : items) {
		 * try { id = Long.parseLong(item.getId()); } catch (Exception e) { }
		 * 
		 * if (id > maxid) { maxid = id; } } }
		 * 
		 * if (onCommandEndListener != null) {
		 * onCommandEndListener.OnCommandEnd(command, result); }
		 */
	}

	private void createNotification(String text) { //
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context);
		Intent resultIntent = new Intent(context, WorkActivity.class);

		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

		stackBuilder.addParentStack(WorkActivity.class);

		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mBuilder.setAutoCancel(true);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		mNotificationManager.notify(new Random().nextInt(1000),
				mBuilder.build());
	}

	public String getLastHash() {
		return null;
	}

	@Override
	public boolean cancel() {
		context.unregisterReceiver(rbr);

		return super.cancel();
	}
}
