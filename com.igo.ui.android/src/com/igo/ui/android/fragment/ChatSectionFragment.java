package com.igo.ui.android.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Timer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.igo.ui.android.DataStorage;
import com.igo.ui.android.R;
import com.igo.ui.android.adapter.ChatViewAdapter;
import com.igo.ui.android.domain.ChatsItem;
import com.igo.ui.android.domain.Login;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;
import com.igo.ui.android.timer.ChatTimerTask;

public class ChatSectionFragment extends Fragment {
	final Timer timer = new Timer("ChatTimer");
	private ListView view;
	private ChatViewAdapter adapter;
	private View rootView;
	private ChatsItem chatsItem; 
	private String login;
	
	public ChatSectionFragment(ChatsItem chatsItem, String login){
		this.chatsItem = chatsItem;
		this.login = login;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("ChatSectionFragment.onCreateView");
		
		rootView = inflater.inflate(R.layout.fragment_chat, container, false);

		view = (ListView) rootView.findViewById(R.id.list_chat_view);
		adapter = new ChatViewAdapter(getActivity().getApplicationContext(), view);
		view.setAdapter(adapter);

		Button btnSend = (Button) rootView.findViewById(R.id.btn_sendmsg);
		btnSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText etSendmsg = (EditText) rootView.findViewById(R.id.et_sendmsg);
				
				String body = etSendmsg.getText().toString();
				try {
					body = URLEncoder.encode(body, "UTF-8");
				} catch (UnsupportedEncodingException e) {
					body = URLEncoder.encode(body);
					e.printStackTrace();
				}
				Command command = new Command(Command.SEND_CHAT);
				command.putParam("body", body);
				if(chatsItem.isIspersonal()){
					command.putParam("sendto", chatsItem.getCode());
					command.putParam("chatcode", chatsItem.getCode() + "-" + login);
				} else {
					command.putParam("sendto", "all");
					command.putParam("chatcode", chatsItem.getCode());
				}
				
				CommandConnector con = new CommandConnector(getActivity()
						.getApplicationContext(), command);
				con.setOnCommandEndListener(adapter);
				con.execute("");
				
				etSendmsg.setText("");
			}
		});

		timer.schedule(new ChatTimerTask(getActivity().getApplicationContext(), adapter, chatsItem.getCode()), 0, 5000);

		return rootView;
	}

	@Override
	public void onDestroyView() {
		if (timer != null) {
			timer.cancel();
			timer.purge();
		}
		super.onDestroyView();
	}

}
