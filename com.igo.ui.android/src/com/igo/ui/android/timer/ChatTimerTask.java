package com.igo.ui.android.timer;

import java.util.TimerTask;

import android.content.Context;

import com.igo.ui.android.domain.ChatMessage;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;
import com.igo.ui.android.remote.OnCommandEndListener;

public class ChatTimerTask extends TimerTask implements OnCommandEndListener {
	private static ChatTimerTask mt = null;
	long maxid = 0;
	long minid = 0;

	public long getMinid() {
		return minid;
	}

	public void setMinid(long minid) {
		this.minid = minid;
	}

	private OnCommandEndListener onCommandEndListener = null;
	private Context context = null;

	public static ChatTimerTask getInstance(Context context,
			OnCommandEndListener onCommandEndListener) {
		if (mt == null) {
			mt = new ChatTimerTask(context, onCommandEndListener);
		}
		return mt;
	}

	public static ChatTimerTask getInstance() {
		return mt;
	}

	private ChatTimerTask(Context context,
			OnCommandEndListener onCommandEndListener) {
		super();
		this.context = context;
		this.onCommandEndListener = onCommandEndListener;
	}

	@Override
	public void run() {
		System.out.println("ChatTimerTask.run");

		Command command = new Command(Command.GET_CHAT);
		command.putParam("maxid", maxid + "");
		command.putParam("minid", minid + "");
		CommandConnector con = new CommandConnector(context, command);
		con.setOnCommandEndListener(this);
		con.execute("");
	}

	public void OnCommandEnd(Command command, Object result) {
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
}