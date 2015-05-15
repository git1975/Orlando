package com.igo.ui.android.widget;

import com.igo.ui.android.R;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;
import com.igo.ui.android.remote.OnCommandEndListener;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessageView extends LinearLayout implements OnClickListener, OnCommandEndListener{
	private String taskId = null;
	
	public MessageView(Context context) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.message_object_view, this, true);
		
		LinearLayout msgView = (LinearLayout) findViewById(R.id.msg_view);
		msgView.setOnClickListener(this);

		//init();

		//addView(imageView);
		//addView(textView);
	}
	
	public void setText(String text){
		TextView tvTask = (TextView) findViewById(R.id.tv_task);
		tvTask.setText(text);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.msg_view:
			Command command = new Command(Command.TASK_COMMIT);
			command.putParam("id", getTaskId());
			CommandConnector con = new CommandConnector(getContext(), command);
			con.setOnCommandEndListener(this);
			con.execute("");
			
			ImageView imgTaskStatus = (ImageView) findViewById(R.id.img_task_status);
			imgTaskStatus.setImageResource(R.drawable.ic_wait);
			
			break;
		case 2:			
			break;
		}
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void OnCommandEnd(Command command, Object result) {
		ImageView imgTaskStatus = (ImageView) findViewById(R.id.img_task_status);
		imgTaskStatus.setImageResource(R.drawable.ic_alert);
		
		Toast.makeText(getContext(), result.toString(), Toast.LENGTH_LONG).show();
	}
}
