package com.igo.ui.android.adapter;

import com.igo.ui.android.R;
import com.igo.ui.android.domain.Task;
import com.igo.ui.android.remote.JsonConnector;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

public class TaskViewAdapter extends BaseAdapter implements ListAdapter {
	private Context mContext;
	private Task[] tasks = null;
	
	public TaskViewAdapter(Context c) {
		mContext = c;
	}

	public int getCount() {
		if(tasks != null){
			return tasks.length;
		}
		
		beginRefresh();
		if(tasks == null){
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
		ImageView imageView = new ImageView(mContext);
		imageView.setImageResource(R.drawable.ic_launcher);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setLayoutParams(new GridView.LayoutParams(120, 110));
		return imageView;
	}
	
	public void beginRefresh(){
		//JsonConnector conn = new JsonConnector(this);
		//conn.execute("http://172.25.101.160:8080/com.igo.server/json/show");
		//conn.execute("http://192.168.0.100:8080/com.igo.server/json/show");
	}
	
	public void endRefresh(Task[] tasks){
		this.tasks = tasks;
		this.notifyDataSetChanged();
	}
}
