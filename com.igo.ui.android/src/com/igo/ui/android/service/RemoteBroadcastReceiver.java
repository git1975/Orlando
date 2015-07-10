package com.igo.ui.android.service;

import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandResponseFactory;
import com.igo.ui.android.remote.OnCommandEndListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

/**
 * Это получатель рассылки сервиса RemoteService для Activity.<br/>
 * Вызывает метод OnCommandEnd (OnCommandEndListener)Activity.
 * @author den
 *
 */
public class RemoteBroadcastReceiver extends BroadcastReceiver {
	OnCommandEndListener commandEndListener;
	
	public RemoteBroadcastReceiver(){
		super();
	}
	
	public RemoteBroadcastReceiver(OnCommandEndListener commandEndListener) {
		super();
		this.commandEndListener = commandEndListener;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String result = intent.getStringExtra(Command.PARAM_RESULT);
		Command command = new Command();
		command.parseJson(intent.getStringExtra(Command.PARAM_COMMAND));
		System.out.println("RemoteBroadcastReceiver.onReceive->" + command.getCommand() + ":" + result);
		
		if (commandEndListener != null) {
			commandEndListener.OnCommandEnd(command, CommandResponseFactory.getCommandResponseObject(
							command.getCommand(), result));
		} else {
			Toast.makeText(context, "RemoteBroadcastReceiver.onReceive",
					Toast.LENGTH_SHORT).show();
		}
	}

	public OnCommandEndListener getCommandEndListener() {
		return commandEndListener;
	}

	public void setCommandEndListener(OnCommandEndListener commandEndListener) {
		this.commandEndListener = commandEndListener;
	}
}
