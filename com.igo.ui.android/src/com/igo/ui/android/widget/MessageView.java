package com.igo.ui.android.widget;

import com.igo.ui.android.R;
import com.igo.ui.android.domain.Task;
import com.igo.ui.android.remote.Command;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessageView extends RelativeLayout /*
												 * implements OnClickListener,
												 * OnCommandEndListener
												 */{
	private String taskId = null;
	private Task task = null;

	public MessageView(Context context, Task task) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.message_object_view, this, true);

		setTask(task);
	}

	public void setText(String text) {
		TextView tvTask = (TextView) findViewById(R.id.tv_task);
		tvTask.setText(text);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.msg_view:
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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public void setImage(Task task) {
		ImageView imgTaskStatus = (ImageView) findViewById(R.id.img_task_status);
		if (task == null) {
			imgTaskStatus.setImageResource(R.drawable.ic_info);
			return;
		}

		if ("CMD".equals(task.getType())) {
			imgTaskStatus.setImageResource(R.drawable.ic_question2);
		} else if ("INFO".equals(task.getType())) {
			imgTaskStatus.setImageResource(R.drawable.ic_info);
		}
	}
	
	/**
	 * Example: getResourceId("myIcon", "drawable", getPackageName());
	 * @param pVariableName
	 * @param pResourcename
	 * @param pPackageName
	 * @return
	 */
	public int getResourceId(String pVariableName, String pResourcename, String pPackageName) 
	{
	    try {
	        return getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return R.drawable.ic_launcher;
	    } 
	}

	public void OnCommandEnd(Command command, Object result) {
		ImageView imgTaskStatus = (ImageView) findViewById(R.id.img_task_status);
		imgTaskStatus.setImageResource(R.drawable.ic_alert);

		Toast.makeText(getContext(), result.toString(), Toast.LENGTH_LONG)
				.show();
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
		if (task == null) {
			setTaskId("0");
			setText("null");
			return;
		}

		setTaskId(task.getId());
		setText(task.getName());
		setImage(task);
	}
}
