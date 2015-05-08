package com.igo.ui.android.timer;

import java.util.TimerTask;

import com.igo.ui.android.adapter.TaskViewAdapter;
import com.igo.ui.android.remote.JsonConnector;

public class MessageTimerTask extends TimerTask {	
	private static MessageTimerTask mt = null;
	
	private TaskViewAdapter view = null;
	
	public static MessageTimerTask getInstance(TaskViewAdapter view){
		if(mt == null){
			mt = new MessageTimerTask(view);
		}
		return mt;
	}

	private MessageTimerTask(TaskViewAdapter view) {
		super();
		this.view = view;
	}

	@Override
	public void run() {
		System.out.println("MessageTimerTask.run");
		JsonConnector conn = new JsonConnector(view);
		conn.execute("http://172.25.101.160:8080/com.igo.server/json/show");
	}

}
