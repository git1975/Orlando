package com.igo.ui.android.widget;

import com.igo.ui.android.R;
import com.igo.ui.android.domain.Task;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessageDetailsView extends RelativeLayout /*
														 * implements
														 * OnClickListener,
														 * OnCommandEndListener
														 */{
	private String taskId = null;
	private String startDate = "-";
	private String endDate = "-";
	private Task task = null;

	public MessageDetailsView(Context context, Task task) {
		super(context);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.message_details_view, this, true);

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

	public Task getTask() {
		return task;
	}

	/*
	 * В зависимости от поступившей информации показываем кнопки
	 */
	public void setTask(Task task) {
		this.task = task;
		setTaskId(task.getId());
		setStartDate(task.getStartDate());
		setEndDate(task.getEndDate());

		TextView text = (TextView) findViewById(R.id.txt_taskbody);
		text.setText(task.getName() + " - " + task.getBody());

		Button btnYes = (Button) findViewById(R.id.btnYes);
		Button btnNo = (Button) findViewById(R.id.btnNo);
		btnYes.setVisibility(View.INVISIBLE);
		btnNo.setVisibility(View.INVISIBLE);
		com.igo.ui.android.domain.Button[] buttons = task.getButtons();
		if (buttons != null) {
			for (com.igo.ui.android.domain.Button btn : buttons) {
				if (btn != null) {
					Button btnC = btnYes;
					if ("YES".equals(btn.getCode())) {
						btnC = btnYes;
					} else if ("NO".equals(btn.getCode())) {
						btnC = btnNo;
					} else if ("HAND".equals(btn.getCode())) {
						btnC = btnYes;
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
	
	//@Override
	protected void onDraw1(android.graphics.Canvas canvas)
    {
        Paint paint = new Paint();

        paint.setAlpha(255);
        canvas.translate(0, 30);
        paint.setColor(Color.BLUE);
        Path mPath = new Path();
        mPath.addRoundRect(new RectF(0, 0, 100,100),20,20, Path.Direction.CCW);
        //canvas.clipPath(mPath, Region.Op.INTERSECT);
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);
        canvas.drawRect(0, 0, 120,120, paint);

    }
}
