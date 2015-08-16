package com.igo.ui.android.widget;

import java.text.SimpleDateFormat;

import com.igo.ui.android.DataStorage;
import com.igo.ui.android.MainActivity;
import com.igo.ui.android.R;
import com.igo.ui.android.domain.ChatMessage;
import com.igo.ui.android.domain.Login;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;
import com.igo.ui.android.remote.OnCommandEndListener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatMessageView extends RelativeLayout implements
		OnCommandEndListener {
	private ChatMessage chatItem = null;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

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
		View viewChatCmd = findViewById(R.id.layout_chat_cmd);
		viewChatCmd.setVisibility(LinearLayout.GONE);

		TextView tvBody2 = (TextView) findViewById(R.id.tv_chat_body_2);
		String body = tvBody2.getText() + " (" + result.toString() + ")";
		tvBody2.setText(body);
		tvBody2.setVisibility(LinearLayout.VISIBLE);

		getChatMessage().getTask().setReplytext(result.toString());
		getChatMessage().getTask().setButtons(null);

		this.invalidate();

		// Toast.makeText(getContext(), result.toString(), Toast.LENGTH_LONG)
		// .show();
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
			from = "Система:";
		}

		String body = sdf.format(item.getSendDate()) + "-";
		body += from + "\r\n";
		body += item.getBody();
		if (item.getTask() != null && item.getTask().getReplytext() != null
				&& !"".equals(item.getTask().getReplytext())) {
			body += " (" + item.getTask().getReplytext() + ")";
		}

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

			tvBody1.setWidth(12);
			tvBody3.setWidth(18);
		} else if (item.getFrom().equals("auto")) {
			tvBody1.setBackground(getResources().getDrawable(
					R.drawable.ic_speech6_1));
			tvBody2.setBackground(getResources().getDrawable(
					R.drawable.ic_speech6_2));
			tvBody3.setBackground(getResources().getDrawable(
					R.drawable.ic_speech6_3));
			layoutChat.setGravity(Gravity.LEFT);

			tvBody1.setWidth(18);
			tvBody3.setWidth(18);
		} else {
			tvBody1.setBackground(getResources().getDrawable(
					R.drawable.ic_speech5_3));
			tvBody2.setBackground(getResources().getDrawable(
					R.drawable.ic_speech5_2));
			tvBody3.setBackground(getResources().getDrawable(
					R.drawable.ic_speech5_1));
			layoutChat.setGravity(Gravity.LEFT);

			tvBody1.setWidth(18);
			tvBody3.setWidth(12);
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

		View viewFromImg = findViewById(R.id.view_from_img);
		if (!"auto".equals(item.getFrom())) {
			viewFromImg.setVisibility(LinearLayout.GONE);
		}

		View viewChatCmd = findViewById(R.id.layout_chat_cmd);

		Button btnYes = (Button) findViewById(R.id.chat_btn1);
		Button btnNo = (Button) findViewById(R.id.chat_btn2);
		btnYes.setVisibility(View.INVISIBLE);
		btnNo.setVisibility(View.INVISIBLE);

		if (item.getTask() != null && item.getTask().getButtons() != null
				&& item.getTask().getButtons().length > 0) {
			com.igo.ui.android.domain.Button[] buttons = item.getTask()
					.getButtons();
			tvBody2.setVisibility(LinearLayout.GONE);
			viewChatCmd.setVisibility(View.VISIBLE);
			TextView tvBodyCmd = (TextView) findViewById(R.id.tv_chat_cmd);
			tvBodyCmd.setText(body);

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
					btnC.setTag(R.string.tag_id, item.getTask().getId());
					btnC.setTag(R.string.tag_reply, btn.getReplystatus());
					btnC.setTag(R.string.tag_forStatus, item.getTask()
							.getForStatus());
					btnC.setTag(R.string.tag_parent, this);
					btnC.setTag(R.string.tag_chatid, item.getId());
					btnC.setTag(R.string.tag_reg, btn.getRegister());
					btnC.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							AlertDialog.Builder builder;
							AlertDialog dialog;
							Context context = v.getContext();

							LayoutInflater inflater = (LayoutInflater) context
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							final View layout = inflater.inflate(
									R.layout.fragment_chat_dialog,
									(LinearLayout) findViewById(R.id.chat_dialog));

							builder = new AlertDialog.Builder(context);
							builder.setView(layout);
							dialog = builder.create();
							dialog.setCancelable(true);
							dialog.setMessage("This is my custom dialog");

							/*dialog.setButton(DialogInterface.BUTTON_POSITIVE,
									"OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											EditText txtName = (EditText) layout
													.findViewById(R.id.et_chat_dialog_register);
											String name = txtName.getText()
													.toString();

											System.out
													.println("AlertDialog Negative OK");
											dialog.dismiss();
										}
									});

							dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
									"Cancel",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog,
												int which) {
											System.out
													.println("AlertDialog Negative Cancel");
										}
									});*/

							dialog.show();

							if (1 == 1)
								return;

							Command command = new Command(Command.REPLY);
							command.putParam("id", v.getTag(R.string.tag_id)
									.toString());
							command.putParam("reply",
									v.getTag(R.string.tag_reply).toString());
							command.putParam("forStatus",
									v.getTag(R.string.tag_forStatus).toString());
							command.putParam("chatid",
									v.getTag(R.string.tag_chatid).toString());
							// command.putParam("reg1",
							// v.getTag(R.string.tag_reg).toString());
							CommandConnector con = new CommandConnector(
									getContext(), command);

							con.setOnCommandEndListener((OnCommandEndListener) v
									.getTag(R.string.tag_parent));

							con.execute("");
						}
					});
				}
			}
		} else {
			viewChatCmd.setVisibility(LinearLayout.GONE);
		}
	}

	public String getLastHash() {
		// TODO Auto-generated method stub
		return null;
	}
}
