package com.igo.ui.android.widget;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.igo.ui.android.R;
import com.igo.ui.android.domain.Task;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ReportDetailsView extends RelativeLayout /*
														 * implements
														 * OnClickListener,
														 * OnCommandEndListener
														 */{
	private String taskId = null;
	private String startDate = "-";
	private String endDate = "-";
	private Task task = null;
	public static SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-ddHH:mm:ssZ");
	private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	public ReportDetailsView(Context context, Task task) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.report_details_view, this, true);

		setTask(task);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.msg_view:
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

		Toast.makeText(getContext(), result.toString(), Toast.LENGTH_LONG)
				.show();
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
		TextView text = (TextView) findViewById(R.id.dt_start);
		text.setText("Старт в " + startDate);
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
		TextView text = (TextView) findViewById(R.id.dt_end);
		text.setText("Завершение в " + endDate);
	}

	public Task getTask() {
		return task;
	}

	/*
	 * В зависимости от поступившей информации показываем кнопки
	 */
	public void setTask(Task task) {
		this.task = task;
		setTaskId(task.getId());
		try {
			Date dt = sdfFull.parse(task.getStartDate());
			setStartDate(sdf.format(dt));
		} catch (ParseException e) {
			setStartDate(task.getStartDate());
		}
		try {
			Date dt = sdfFull.parse(task.getEndDate());
			setEndDate(sdf.format(dt));
		} catch (ParseException e) {
			setEndDate(task.getEndDate());
		}

		TextView text = (TextView) findViewById(R.id.txt_taskbody);
		text.setText(task.getBody());

		Button btnYes = (Button) findViewById(R.id.btnYes);
		Button btnNo = (Button) findViewById(R.id.btnNo);
		btnYes.setVisibility(View.INVISIBLE);
		btnNo.setVisibility(View.INVISIBLE);
		com.igo.ui.android.domain.Button[] buttons = task.getButtons();
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

		if ("INFO".equals(task.getType())) {
			btnYes.setVisibility(View.INVISIBLE);
			btnNo.setVisibility(View.INVISIBLE);
		}
	}

}
