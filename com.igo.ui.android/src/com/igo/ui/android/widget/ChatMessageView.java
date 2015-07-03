package com.igo.ui.android.widget;

import java.text.SimpleDateFormat;

import com.igo.ui.android.DataStorage;
import com.igo.ui.android.R;
import com.igo.ui.android.domain.ChatMessage;
import com.igo.ui.android.domain.Login;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChatMessageView extends RelativeLayout {
	private ChatMessage chatItem = null;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	private String taskId = null;

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
		if ("auto".equals(from)) {
			from = "Система";
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
			tvBody1.setBackground(getResources().getDrawable(
					R.drawable.ic_speech4_3));
			tvBody2.setBackground(getResources().getDrawable(
					R.drawable.ic_speech4_2));
			tvBody3.setBackground(getResources().getDrawable(
					R.drawable.ic_speech4_1));
			layoutChat.setGravity(Gravity.RIGHT);
		} else if (item.getFrom().equals("auto")) {
			tvBody1.setBackground(getResources().getDrawable(
					R.drawable.ic_speech6_1));
			tvBody2.setBackground(getResources().getDrawable(
					R.drawable.ic_speech6_2));
			tvBody3.setBackground(getResources().getDrawable(
					R.drawable.ic_speech6_3));
			layoutChat.setGravity(Gravity.CENTER);
		} else {
			tvBody1.setBackground(getResources().getDrawable(
					R.drawable.ic_speech5_3));
			tvBody2.setBackground(getResources().getDrawable(
					R.drawable.ic_speech5_2));
			tvBody3.setBackground(getResources().getDrawable(
					R.drawable.ic_speech5_1));
			layoutChat.setGravity(Gravity.LEFT);
		}

		if (tvBody2.getTypeface() == null) {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
					"fonts/TAHOMA.TTF");
			tvBody2.setTypeface(tf);
		}
		
		Canvas canvas = new Canvas();
		canvas.translate(20, 20);
		Paint paint = new Paint();
		paint.setColor(Color.rgb(0, 0, 0));
		canvas.drawLine(0, 0, 20, 20, paint);
		tvBody2.draw(canvas);

		View viewChatCmd = findViewById(R.id.layout_chat_cmd);
		viewChatCmd.setVisibility(View.INVISIBLE);
		Button btnYes = (Button) findViewById(R.id.chat_btn1);
		Button btnNo = (Button) findViewById(R.id.chat_btn2);
		btnYes.setVisibility(View.INVISIBLE);
		btnNo.setVisibility(View.INVISIBLE);
		if (item.getTask() != null) {
			viewChatCmd.setVisibility(View.VISIBLE);
			com.igo.ui.android.domain.Button[] buttons = item.getTask()
					.getButtons();
			taskId = item.getTask().getId();
			if (buttons != null) {
				int counter = 0;
				for (com.igo.ui.android.domain.Button btn : buttons) {
					counter++;
					if (btn != null) {
						Button btnC = btnYes;
						switch (counter) {
						case 1:
							btnC = btnYes;
							break;
						case 2:
							btnC = btnNo;
							break;
						}
						btnC.setVisibility(View.VISIBLE);
						btnC.setText(btn.getName());
						btnC.setTag(btn.getReplystatus());
						btnC.setOnClickListener(new OnClickListener() {
							public void onClick(View v) {
								Command command = new Command(Command.REPLY);
								command.putParam("id", taskId);
								command.putParam("reply", v.getTag().toString());
								CommandConnector con = new CommandConnector(
										getContext(), command);

								con.execute("");
							}
						});
					}
				}
			}
		}
	}
}
