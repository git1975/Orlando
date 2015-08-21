package com.igo.ui.android.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.igo.ui.android.DataStorage;
import com.igo.ui.android.R;
import com.igo.ui.android.adapter.ChatViewAdapter;
import com.igo.ui.android.domain.ChatMessage;
import com.igo.ui.android.domain.ChatsItem;
import com.igo.ui.android.domain.Login;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;
import com.igo.ui.android.remote.OnCommandEndListener;
import com.igo.ui.android.service.RemoteBroadcastReceiver;
import com.igo.ui.android.service.RemoteService;
import com.igo.ui.android.timer.ChatStatusTimerTask;
import com.igo.ui.android.timer.ChatTimerTask;

public class ChatSectionFragment extends Fragment implements OnCommandEndListener{
	final Timer timer = new Timer("ChatTimer");
	private ListView view;
	private ChatViewAdapter adapter;
	private View rootView;
	private ChatsItem chatsItem;
	private Login login;
	private final RemoteBroadcastReceiver rbr = new RemoteBroadcastReceiver();
	private final RemoteBroadcastReceiver rbrStatus = new RemoteBroadcastReceiver();

	public ChatSectionFragment(ChatsItem chatsItem, Login login) {
		this.chatsItem = chatsItem;
		this.login = login;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("ChatSectionFragment.onCreateView");

		rootView = inflater.inflate(R.layout.fragment_chat, container, false);

		view = (ListView) rootView.findViewById(R.id.list_chat_view);
		adapter = new ChatViewAdapter(getActivity().getApplicationContext(),
				getActivity(), view);
		view.setAdapter(adapter);

		TextView tvLogin = (TextView) rootView.findViewById(R.id.tv_login);
		tvLogin.setText(login.getName() + " (" + login.getRole() + ")");

		Button btnSend = (Button) rootView.findViewById(R.id.btn_sendmsg);
		btnSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText etSendmsg = (EditText) rootView
						.findViewById(R.id.et_sendmsg);

				String body = etSendmsg.getText().toString();
				
				//InputMethodManager mgr = (InputMethodManager) v.getSystemService(Context.INPUT_METHOD_SERVICE);
			    //mgr.hideSoftInputFromWindow(etSendmsg.getWindowToken(), 0);
			    
				try {
					body = URLEncoder.encode(body, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					body = URLEncoder.encode(body);
					e.printStackTrace();
				}
				Command command = new Command(Command.SEND_CHAT);
				command.putParam("body", body);
				if (chatsItem.isIspersonal()) {
					command.putParam("sendto", chatsItem.getCode());
					command.putParam("chatcode", chatsItem.getCode() + "-"
							+ login.getLogin());
				} else {
					command.putParam("sendto", "all");
					command.putParam("chatcode", chatsItem.getCode());
				}

				// CommandConnector con = new CommandConnector(getActivity()
				// .getApplicationContext(), command);
				// con.setOnCommandEndListener(adapter);
				// con.execute("");

				getActivity().startService(
						new Intent(getActivity(), RemoteService.class)
								.putExtra(Command.PARAM_COMMAND,
										command.getJson()));

				etSendmsg.setText("");
			}
		});

		rbr.setCommandEndListener(adapter);
		getActivity().registerReceiver(rbr,
				new IntentFilter(RemoteService.IGO_SERVICE_ACTION));
		rbrStatus.setCommandEndListener(this);
		getActivity().registerReceiver(rbrStatus,
				new IntentFilter(RemoteService.IGO_SERVICE_ACTION));

		// Создадим расписание для задачи обновления списка чата
		timer.schedule(new ChatTimerTask(getActivity().getApplicationContext(),
				adapter, chatsItem.getCode()), 0, 5000);
		// Создадим расписание для задачи обновления статуса чата
		timer.schedule(new ChatStatusTimerTask(getActivity().getApplicationContext(), 
				adapter, chatsItem.getCode()), 0, 5000);

		return rootView;
	}
	
	public void OnCommandEnd(Command command, Object result) {
		if (result == null) {
			return;
		}
		// рисуем светофор
		if (Command.GET_CHATSTATUS.equals(command.getCommand())) {
			ImageView ivGreen2 = (ImageView) rootView
					.findViewById(R.id.iv_green2);
			ImageView ivYellow2 = (ImageView) rootView
					.findViewById(R.id.iv_yellow2);
			ImageView ivRed2 = (ImageView) rootView
					.findViewById(R.id.iv_red2);
			
			ivGreen2.setVisibility(View.GONE);
			ivYellow2.setVisibility(View.GONE);
			ivRed2.setVisibility(View.GONE);
			if("1".equals(result.toString())){
				ivGreen2.setVisibility(View.VISIBLE);
			} else if("2".equals(result.toString())){
				ivYellow2.setVisibility(View.VISIBLE);
			} else {
				ivRed2.setVisibility(View.VISIBLE);
			} 
		}
	}

	@Override
	public void onDestroyView() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
		getActivity().unregisterReceiver(rbr);
		getActivity().unregisterReceiver(rbrStatus);
		super.onDestroyView();
	}

	public String getLastHash() {
		// TODO Auto-generated method stub
		return null;
	}

}
