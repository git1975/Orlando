package com.igo.ui.android.adapter;

import com.igo.ui.android.R;
import com.igo.ui.android.domain.Task;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.OnCommandEndListener;
import com.igo.ui.android.widget.ReportDetailsView;
import com.igo.ui.android.widget.ReportItemView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class ReportViewAdapter extends BaseExpandableListAdapter implements
		OnCommandEndListener {
	private Context context;

	public Context getContext() {
		return context;
	}

	private Task[] tasks = null;

	public ReportViewAdapter(Context c) {
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
		ReportItemView view = null;
		if (tasks[groupPosition] != null) {
			view = new ReportItemView(context, tasks[groupPosition]);
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
		ReportDetailsView view = new ReportDetailsView(context,
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
			hash += "[" + task.getId() + ";" + task.getStatus() + ";" + task.getType() + "]";
		}
		return hash;
	}

}
