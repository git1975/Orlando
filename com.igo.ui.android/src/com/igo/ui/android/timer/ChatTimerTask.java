package com.igo.ui.android.timer;

import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.igo.ui.android.domain.ChatMessage;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;
import com.igo.ui.android.remote.OnCommandEndListener;
import com.igo.ui.android.service.RemoteBroadcastReceiver;
import com.igo.ui.android.service.RemoteService;

public class ChatTimerTask extends TimerTask implements OnCommandEndListener {
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

	public ChatTimerTask(Context context,
			OnCommandEndListener onCommandEndListener, String chatcode) {
		super();
		this.context = context;
		this.onCommandEndListener = onCommandEndListener;
		this.chatcode = chatcode;
		
		rbr.setCommandEndListener(this);
	}

	@Override
	public void run() {
		System.out.println("ChatTimerTask.run");

		Command command = new Command(Command.GET_CHAT);
		command.putParam("maxid", maxid + "");
		command.putParam("minid", minid + "");
		command.putParam("chatcode", chatcode);
		//CommandConnector con = new CommandConnector(context, command);
		//con.setOnCommandEndListener(this);
		//con.execute("");
		
		context.registerReceiver(rbr,
				new IntentFilter(RemoteService.IGO_SERVICE_ACTION));
		context.startService(
				new Intent(context, RemoteService.class)
						.putExtra(Command.PARAM_COMMAND,
								command.getJson()));
	}

	public void OnCommandEnd(Command command, Object result) {
		context.unregisterReceiver(rbr);
		
		ChatMessage[] items = (ChatMessage[]) result;

		if (items != null) {
			long id = 0;
			for (ChatMessage item : items) {
				try {
					id = Long.parseLong(item.getId());
				} catch (Exception e) {
				}

				if (id > maxid) {
					maxid = id;
				}
			}
		}

		if (onCommandEndListener != null) {
			onCommandEndListener.OnCommandEnd(command, result);
		}
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
