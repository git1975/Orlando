package com.igo.ui.android.adapter;

import com.igo.ui.android.R;
import com.igo.ui.android.domain.Task;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.OnCommandEndListener;
import com.igo.ui.android.widget.MessageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

public class TaskViewAdapter extends BaseAdapter implements ListAdapter, OnCommandEndListener {
	private Context context;
	public Context getContext() {
		return context;
	}

	private Task[] tasks = null;

	public TaskViewAdapter(Context c) {
		context = c;
	}

	public int getCount() {
		if (tasks != null) {
			return tasks.length;
		}

		if (tasks == null) {
			return 0;
		}
		return tasks.length;
	}

	public Object getItem(int position) {
		return R.drawable.ic_launcher;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		MessageView view = new MessageView(context);
		if (tasks[position] != null) {
			view.setText(tasks[position].getName());
			view.setTaskId(tasks[position].getId());
		}
		return view;
	}

	public void OnCommandEnd(Command command, Object result) {
		this.tasks = (Task[]) result;
		
		this.notifyDataSetChanged();
	}
}
