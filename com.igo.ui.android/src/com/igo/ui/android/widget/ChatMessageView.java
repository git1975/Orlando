package com.igo.ui.android.widget;

import java.text.SimpleDateFormat;

import com.igo.ui.android.DataStorage;
import com.igo.ui.android.R;
import com.igo.ui.android.domain.ChatMessage;
import com.igo.ui.android.domain.Login;
import com.igo.ui.android.remote.Command;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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

	public void setText(String text) {
		TextView tvTask = (TextView) findViewById(R.id.tv_chat_item);
		tvTask.setText(text);
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

	public void setImage(ChatMessage item) {
		ImageView imgTaskStatus = (ImageView) findViewById(R.id.img_chat_status);
		if (item == null) {
			imgTaskStatus.setImageResource(R.drawable.ic_info);
			return;
		}

		/*
		 * if("INIT".equals(task.getStatus())){
		 * imgTaskStatus.setImageResource(R.drawable.ic_info); } else
		 * if("REPLY_NO".equals(task.getStatus())){
		 * imgTaskStatus.setImageResource(R.drawable.ic_no); } else
		 * if("TIMEOUT".equals(task.getStatus())){
		 * imgTaskStatus.setImageResource(R.drawable.ic_time); } else
		 * if("REPLY_HAND".equals(task.getStatus())){
		 * imgTaskStatus.setImageResource(R.drawable.ic_hand); }
		 */
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
			setText("null");
			return;
		}

		TextView tvDate = (TextView) findViewById(R.id.tv_chat_item);
		if (item.getSendDate() == null) {
			tvDate.setText("?");
		} else {
			tvDate.setText(sdf.format(item.getSendDate()));
		}
		tvDate = (TextView) findViewById(R.id.tv_chat_body);
		tvDate.setText(item.getBody());

		DataStorage ds = (DataStorage) getContext();
		Login login = (Login) ds.getData("login");

		tvDate = (TextView) findViewById(R.id.tv_chat_from);
		if (item.getFrom().equals(login.getLogin())) {
			tvDate.setText(getResources().getString(R.string.str_iam));
		} else {
			tvDate.setText(item.getFrom());
		}

		setImage(item);
	}
}
