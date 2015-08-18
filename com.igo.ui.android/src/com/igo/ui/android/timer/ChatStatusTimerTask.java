package com.igo.ui.android.timer;

import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.igo.ui.android.domain.ChatMessage;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.OnCommandEndListener;
import com.igo.ui.android.service.RemoteBroadcastReceiver;
import com.igo.ui.android.service.RemoteService;

public class ChatStatusTimerTask extends TimerTask implements OnCommandEndListener {
	private String chatcode;
	private final RemoteBroadcastReceiver rbr = new RemoteBroadcastReceiver();
	private OnCommandEndListener onCommandEndListener = null;
	private Context context = null;

	public ChatStatusTimerTask(Context context,
			OnCommandEndListener onCommandEndListener, String chatcode) {
		super();
		this.context = context;
		this.onCommandEndListener = onCommandEndListener;
		this.chatcode = chatcode;
		
		rbr.setCommandEndListener(this);
	}

	@Override
	public void run() {
		System.out.println("ChatStatusTimerTask.run");

		Command command = new Command(Command.GET_CHATSTATUS);
		command.putParam("chatcode", chatcode);
		
		context.registerReceiver(rbr,
				new IntentFilter(RemoteService.IGO_SERVICE_ACTION));
		context.startService(
				new Intent(context, RemoteService.class)
						.putExtra(Command.PARAM_COMMAND,
								command.getJson()));
	}

	public void OnCommandEnd(Command command, Object result) {
		context.unregisterReceiver(rbr);
		
		if(!Command.GET_CHATSTATUS.equals(command.getCommand())){
			return;
		}
		String status = result.toString();		

		if (onCommandEndListener != null) {
			onCommandEndListener.OnCommandEnd(command, status);
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
