package com.igo.ui.android.fragment;

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

import com.igo.ui.android.R;
import com.igo.ui.android.adapter.ChatViewAdapter;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;
import com.igo.ui.android.timer.ChatTimerTask;

public class ChatSectionFragment extends Fragment {
	private Timer timer = null;
	private ListView view;
	private ChatViewAdapter adapter;
	View rootView;

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
				
				Command command = new Command(Command.SEND_CHAT);
				command.putParam("body", etSendmsg.getText().toString());
				CommandConnector con = new CommandConnector(getActivity()
						.getApplicationContext(), command);
				con.execute("");
				
				etSendmsg.setText("");
			}
		});

		ChatTimerTask task = ChatTimerTask.getInstance(getActivity()
				.getApplicationContext(), adapter);
		timer = new Timer("ChatTimer");
		timer.schedule(task, 0, 5000);

		return rootView;
	}

	@Override
	public void onDestroyView() {
		if (timer != null) {
			timer.cancel();
		}
		super.onDestroyView();
	}

}
