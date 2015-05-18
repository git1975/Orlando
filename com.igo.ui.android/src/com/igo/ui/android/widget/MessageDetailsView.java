package com.igo.ui.android.widget;

import com.igo.ui.android.R;
import com.igo.ui.android.remote.Command;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessageDetailsView extends RelativeLayout /*implements OnClickListener, OnCommandEndListener*/{
	private String taskId = null;
	private String startDate = "-";
	private String endDate = "-";
	
	public MessageDetailsView(Context context) {
		super(context);
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.message_details_view, this, true);
		
		//LinearLayout msgView = (LinearLayout) findViewById(R.id.msg_view);
		//msgView.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.msg_view:
			/*Command command = new Command(Command.TASK_COMMIT);
			command.putParam("id", getTaskId());
			CommandConnector con = new CommandConnector(getContext(), command);
			con.setOnCommandEndListener(this);
			con.execute("");
			
			ImageView imgTaskStatus = (ImageView) findViewById(R.id.img_task_status);
			imgTaskStatus.setImageResource(R.drawable.ic_wait);*/
						
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
		TextView text = (TextView) findViewById(R.id.dt_start);
		text.setText(startDate);
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
		TextView text = (TextView) findViewById(R.id.dt_end);
		text.setText(endDate);
	}
}
