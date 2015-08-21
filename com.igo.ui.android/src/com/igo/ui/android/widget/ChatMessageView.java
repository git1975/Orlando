package com.igo.ui.android.widget;

import java.text.SimpleDateFormat;

import org.json.JSONException;
import org.json.JSONObject;

import com.igo.ui.android.DataStorage;
import com.igo.ui.android.MainActivity;
import com.igo.ui.android.R;
import com.igo.ui.android.domain.ChatMessage;
import com.igo.ui.android.domain.Login;
import com.igo.ui.android.domain.Register;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;
import com.igo.ui.android.remote.OnCommandEndListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatMessageView extends RelativeLayout implements
		OnCommandEndListener {
	private ChatMessage chatItem = null;
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
	private Activity activity;
	boolean dialogOk = false;
	private String dialogValue;
	private Login login;

	public ChatMessageView(Context context, Activity activity, ChatMessage item) {
		super(context);
		this.activity = activity;
		DataStorage ds = (DataStorage) getContext();
		login = (Login) ds.getData("login");

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
		viewChatCmd.setVisibility(LinearLayout.VISIBLE);

		TextView tvChatCmd = (TextView) findViewById(R.id.tv_chat_cmd);
		
		String strResult = null;
		try {
			JSONObject jObj = new JSONObject(result.toString());
			strResult = jObj.getString("result");
		} catch (JSONException e) {
			e.printStackTrace();
			strResult = result.toString();
		}
		
		String body = tvChatCmd.getText() + " (" + strResult + ")";
		tvChatCmd.setText(body);
		tvChatCmd.setVisibility(LinearLayout.VISIBLE);

		getChatMessage().getTask().setReplytext(strResult);
		getChatMessage().getTask().setButtons(null);

		//this.invalidate();
		setChatMessage(getChatMessage());

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

		String from;
		if (item.getFrom().equals(login.getLogin())) {
			from = getResources().getString(R.string.str_iam);
		} else {
			from = item.getFrom();
		}
		if ("auto".equals(from)) {
			from = "Система:";
		}

		String time = sdf.format(item.getSendDate());
		String body = "";
		body += from + "\r\n";
		body += item.getBody();
		if (item.getTask() != null && item.getTask().getReplytext() != null
				&& !"".equals(item.getTask().getReplytext())) {
			body += " (" + item.getTask().getReplytext() + ")";
		}

		//TextView tvBody1 = (TextView) findViewById(R.id.tv_chat_body_1);
		//TextView tvBody2 = (TextView) findViewById(R.id.tv_chat_body_2);
		//TextView tvBody3 = (TextView) findViewById(R.id.tv_chat_body_3);
		//tvBody2.setText(body);

		LinearLayout layoutChat = (LinearLayout) findViewById(R.id.layout_chat);
		LinearLayout layoutChatCmd = (LinearLayout) findViewById(R.id.layout_chat_cmd);
		View viewFromImg = findViewById(R.id.view_from_img);
		//View viewFromIam = findViewById(R.id.view_from_iam);
		//View viewFromOther = findViewById(R.id.view_from_other);
		viewFromImg.setVisibility(LinearLayout.GONE);
		//viewFromIam.setVisibility(LinearLayout.GONE);
		//viewFromOther.setVisibility(LinearLayout.GONE);
		if (item.getFrom().equals(login.getLogin())) {
			layoutChatCmd.setBackground(getResources().getDrawable(
					R.drawable.rounded_rect2));
			layoutChat.setGravity(Gravity.RIGHT);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(20, 0, 0, 0);
			layoutChatCmd.setLayoutParams(lp);
		} else if (item.getFrom().equals("auto")) {
			layoutChatCmd.setBackground(getResources().getDrawable(
					R.drawable.rounded_rect));
			layoutChat.setGravity(Gravity.LEFT);
			viewFromImg.setVisibility(LinearLayout.VISIBLE);
			viewFromImg.setBackground(getResources().getDrawable(
					R.drawable.ic_social));
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 0, 20, 0);
			layoutChatCmd.setLayoutParams(lp);
		} else {
			layoutChatCmd.setBackground(getResources().getDrawable(
					R.drawable.rounded_rect3));
			layoutChat.setGravity(Gravity.LEFT);
			viewFromImg.setVisibility(LinearLayout.VISIBLE);
			viewFromImg.setBackground(getResources().getDrawable(
					R.drawable.ic_user));
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(0, 0, 20, 0);
			layoutChatCmd.setLayoutParams(lp);
		}

		Button btnYes = (Button) findViewById(R.id.chat_btn1);
		Button btnNo = (Button) findViewById(R.id.chat_btn2);
		btnYes.setVisibility(View.GONE);
		btnNo.setVisibility(View.GONE);

		TextView tvBodyCmd = (TextView) findViewById(R.id.tv_chat_cmd);
		TextView tvChatTime = (TextView) findViewById(R.id.tv_chat_time);
		if (tvBodyCmd.getTypeface() == null) {
			Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
					"fonts/TAHOMA.TTF");
			tvBodyCmd.setTypeface(tf);
			tvChatTime.setTypeface(tf);
		}
		tvBodyCmd.setText(body);
		tvChatTime.setText(time);
			
		if (item.getTask() != null && item.getTask().getButtons() != null
				&& item.getTask().getButtons().length > 0) {
			com.igo.ui.android.domain.Button[] buttons = item.getTask()
					.getButtons();
			//tvBody2.setVisibility(LinearLayout.GONE);
			layoutChatCmd.setVisibility(View.VISIBLE);

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
							// Если к кнопке привязан регистр, то покажем
							// модальный диалог редактирования регистра
							final Register register = (Register) v
									.getTag(R.string.tag_reg);
							if (register != null) {
								AlertDialog.Builder builder;
								AlertDialog dialog;
								Context context = v.getContext();

								dialogOk = false;

								LayoutInflater inflater = (LayoutInflater) context
										.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
								final View layout = inflater
										.inflate(
												R.layout.fragment_chat_dialog,
												(LinearLayout) findViewById(R.id.chat_dialog));

								builder = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK);
								builder.setView(layout);
								dialog = builder.create();
								dialog.setCancelable(true);
								dialog.setMessage(getResources().getString(
										R.string.str_edit_value));
								TextView tvCaption = (TextView) layout
										.findViewById(R.id.tv_chat_dialog);
								tvCaption.setText(register.getName());
								EditText txtName = (EditText) layout
										.findViewById(R.id.et_chat_dialog_register);
								txtName.setText(register.getValue());

								final Command command = new Command(Command.REPLY);
								command.putParam("id", v
										.getTag(R.string.tag_id).toString());
								command.putParam("reply",
										v.getTag(R.string.tag_reply).toString());
								command.putParam("forStatus",
										v.getTag(R.string.tag_forStatus)
												.toString());
								command.putParam("chatid",
										v.getTag(R.string.tag_chatid)
												.toString());

								dialog.setButton(
										DialogInterface.BUTTON_POSITIVE,
										getResources().getString(
												R.string.str_ok),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												EditText txtName = (EditText) layout
														.findViewById(R.id.et_chat_dialog_register);
												command.putParam(register.getCode(), txtName.getText().toString());
												sendCommand(command);

												dialog.dismiss();
											}
										});

								dialog.setButton(
										DialogInterface.BUTTON_NEGATIVE,
										getResources().getString(
												R.string.str_cancel),
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int which) {
												dialog.cancel();
											}
										});

								dialog.show();
								
								return;
							}

							Command command = new Command(Command.REPLY);
							command.putParam("id", v.getTag(R.string.tag_id)
									.toString());
							command.putParam("reply",
									v.getTag(R.string.tag_reply).toString());
							command.putParam("forStatus",
									v.getTag(R.string.tag_forStatus).toString());
							command.putParam("chatid",
									v.getTag(R.string.tag_chatid).toString());
							if (register != null) {
								command.putParam(register.getCode(),
										dialogValue);
							}
							sendCommand(command);
						}
					});
				}
			}
		} else {
			layoutChatCmd.setVisibility(LinearLayout.VISIBLE);
		}
	}

	private void sendCommand(Command command) {
		CommandConnector con = new CommandConnector(getContext(), command);
		con.setOnCommandEndListener(this);
		con.execute("");
	}

	public String getLastHash() {
		// TODO Auto-generated method stub
		return null;
	}
}
