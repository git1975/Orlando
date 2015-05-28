package com.igo.ui.android.timer;

import java.util.TimerTask;

import android.content.Context;

import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;
import com.igo.ui.android.remote.OnCommandEndListener;

public class ChatTimerTask extends TimerTask{	
	private static ChatTimerTask mt = null;
	
	private OnCommandEndListener onCommandEndListener = null;
	private Context context = null;
	
	public static ChatTimerTask getInstance(Context context, OnCommandEndListener onCommandEndListener){
		if(mt == null){
			mt = new ChatTimerTask(context, onCommandEndListener);
		}
		return mt;
	}

	private ChatTimerTask(Context context, OnCommandEndListener onCommandEndListener) {
		super();
		this.context = context;
		this.onCommandEndListener = onCommandEndListener;
	}

	@Override
	public void run() {
		System.out.println("ChatTimerTask.run");
		
		Command command = new Command(Command.GET_CHAT);
		CommandConnector con = new CommandConnector(context, command);
		con.setOnCommandEndListener(onCommandEndListener);
		con.execute("");
	}
}
