package com.igo.ui.android.timer;

import java.util.TimerTask;

import android.content.Context;

import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;
import com.igo.ui.android.remote.OnCommandEndListener;

public class MessageTimerTask extends TimerTask{	
	private static MessageTimerTask mt = null;
	
	private OnCommandEndListener onCommandEndListener = null;
	private Context context = null;
	
	public static MessageTimerTask getInstance(Context context, OnCommandEndListener onCommandEndListener){
		if(mt == null){
			mt = new MessageTimerTask(context, onCommandEndListener);
		}
		return mt;
	}

	private MessageTimerTask(Context context, OnCommandEndListener onCommandEndListener) {
		super();
		this.context = context;
		this.onCommandEndListener = onCommandEndListener;
	}

	@Override
	public void run() {
		System.out.println("MessageTimerTask.run");
		
		Command command = new Command(Command.SHOW);
		CommandConnector con = new CommandConnector(context, command);
		con.setOnCommandEndListener(onCommandEndListener);
		con.execute("");
	}
}
