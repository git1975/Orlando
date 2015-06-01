package com.igo.ui.android.adapter;

import com.igo.ui.android.DataStorage;
import com.igo.ui.android.R;
import com.igo.ui.android.domain.Login;
import com.igo.ui.android.domain.Task;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.OnCommandEndListener;
import com.igo.ui.android.widget.MessageDetailsView;
import com.igo.ui.android.widget.MessageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;

public class TaskExpViewAdapter extends BaseExpandableListAdapter implements
		OnCommandEndListener {
	private Context context;
	private View statusView = null;

	public Context getContext() {
		return context;
	}

	private Task[] tasks = null;

	public TaskExpViewAdapter(Context c, View statusView) {
		super();
		context = c;
		this.statusView = statusView;
	}

	public int getGroupCount() {
		if (tasks != null) {
			return tasks.length;
		}

		if (tasks == null) {
			return 0;
		}
		return tasks.length;
	}

	public Object getGroup(int position) {
		return tasks[position];
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		MessageView view = null;
		if (tasks[groupPosition] != null) {
			view = new MessageView(context, tasks[groupPosition]);
		}

		return view;
	}

	public void OnCommandEnd(Command command, Object result) {
		if(result == null || ((Object[]) result).length == 0){
			return;
		}
		this.tasks = (Task[]) result;
		
		if(tasks.length == 1 && "CLEAR".equals(tasks[0].getType())){
			tasks = null;
		}

		int[] counter = { 0, 0, 0 };// green, yellow, red
		if (tasks != null) {
			for (Task item : tasks) {
				if (item.getColor() == 1) {
					counter[0]++;
				} else if (item.getColor() == 2) {
					counter[1]++;
				} else if (item.getColor() == 3) {
					counter[2]++;
				}
			}
		}
		Button btn = (Button) statusView.findViewById(R.id.btn_green);
		btn.setText(counter[0] + "");
		btn = (Button) statusView.findViewById(R.id.btn_yellow);
		btn.setText(counter[1] + "");
		btn = (Button) statusView.findViewById(R.id.btn_red);
		btn.setText(counter[2] + "");
		
		this.notifyDataSetChanged();
	}

	public Object getChild(int groupPosition, int childPosition) {
		return R.drawable.ic_launcher;
	}

	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// System.out.println("getChildView---->>>>" + parent.getId());

		// if (convertView == null) {
		MessageDetailsView view = new MessageDetailsView(context,
				tasks[groupPosition]);

		convertView = view;
		// }

		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	public String getLastHash() {
		if (tasks == null) {
			return "";
		}
		String hash = "";
		for (Task task : tasks) {
			hash += "[" + task.getId() + ";" + task.getStatus() + "]";
		}
		return hash;
	}

}
