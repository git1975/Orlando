package com.igo.ui.android.adapter;

import com.igo.ui.android.R;
import com.igo.ui.android.domain.Task;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.OnCommandEndListener;
import com.igo.ui.android.widget.MessageDetailsView;
import com.igo.ui.android.widget.MessageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class TaskExpViewAdapter extends BaseExpandableListAdapter implements
		OnCommandEndListener {
	private Context context;

	public Context getContext() {
		return context;
	}

	private Task[] tasks = null;

	public TaskExpViewAdapter(Context c) {
		super();
		context = c;
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
		//if (convertView == null) {
			MessageView view = new MessageView(context);
			if (tasks[groupPosition] != null) {
				view.setText(tasks[groupPosition].getName());
				view.setTaskId(tasks[groupPosition].getId());
			}
			convertView = view;
		//}

		return convertView;
	}

	public void OnCommandEnd(Command command, Object result) {
		this.tasks = (Task[]) result;

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
		//System.out.println("getChildView---->>>>" + parent.getId());
		
		//if (convertView == null) {
			MessageDetailsView view = new MessageDetailsView(context, tasks[groupPosition]);

			convertView = view;
		//}

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

}
