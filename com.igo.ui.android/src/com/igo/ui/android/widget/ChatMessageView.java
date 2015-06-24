package com.igo.ui.android.widget;

import java.text.SimpleDateFormat;

import com.igo.ui.android.DataStorage;
import com.igo.ui.android.R;
import com.igo.ui.android.domain.ChatMessage;
import com.igo.ui.android.domain.Login;
import com.igo.ui.android.remote.Command;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChatMessageView extends RelativeLayout {
	private ChatMessage chatItem = null;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	public ChatMessageView(Context context, ChatMessage item) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.chat_message_object_view, this, true);

		setChatMessage(item);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_msg_view:
			/*
			 * Command command = new Command(Command.TASK_COMMIT);
			 * command.putParam("id", getTaskId()); CommandConnector con = new
			 * CommandConnector(getContext(), command);
			 * con.setOnCommandEndListener(this); con.execute("");
			 * 
			 * ImageView imgTaskStatus = (ImageView)
			 * findViewById(R.id.img_task_status);
			 * imgTaskStatus.setImageResource(R.drawable.ic_wait);
			 */

			break;
		case 2:
			break;
		}
	}

	public void OnCommandEnd(Command command, Object result) {
		ImageView imgTaskStatus = (ImageView) findViewById(R.id.img_task_status);
		imgTaskStatus.setImageResource(R.drawable.ic_alert);

		Toast.makeText(getContext(), result.toString(), Toast.LENGTH_LONG)
				.show();
	}

	public ChatMessage getChatMessage() {
		return chatItem;
	}

	public void setChatMessage(ChatMessage item) {
		this.chatItem = item;
		if (chatItem == null) {
			return;
		}
		
		DataStorage ds = (DataStorage) getContext();
		Login login = (Login) ds.getData("login");
		String from;
		if (item.getFrom().equals(login.getLogin())) {
			from = getResources().getString(R.string.str_iam);
		} else {
			from = item.getFrom();
		}
		
		String body = sdf.format(item.getSendDate()) + "-";
		body += from + "\r\n";
		body += item.getBody();

		TextView tvBody1 = (TextView) findViewById(R.id.tv_chat_body_1);
		TextView tvBody2 = (TextView) findViewById(R.id.tv_chat_body_2);
		TextView tvBody3 = (TextView) findViewById(R.id.tv_chat_body_3);
		tvBody2.setText(body);
		
		LinearLayout layoutChat = (LinearLayout) findViewById(R.id.layout_chat);
		if (item.getFrom().equals(login.getLogin())) {
			tvBody1.setBackground(getResources().getDrawable(R.drawable.ic_speech4_1));
			tvBody2.setBackground(getResources().getDrawable(R.drawable.ic_speech4_2));
			tvBody3.setBackground(getResources().getDrawable(R.drawable.ic_speech4_3));
			layoutChat.setGravity(Gravity.LEFT);
		}  else {
			tvBody1.setBackground(getResources().getDrawable(R.drawable.ic_speech5_1));
			tvBody2.setBackground(getResources().getDrawable(R.drawable.ic_speech5_2));
			tvBody3.setBackground(getResources().getDrawable(R.drawable.ic_speech5_3));
			layoutChat.setGravity(Gravity.RIGHT);
		}
	}
}
