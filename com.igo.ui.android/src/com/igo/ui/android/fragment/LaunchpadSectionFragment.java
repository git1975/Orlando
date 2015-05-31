package com.igo.ui.android.fragment;

import java.util.Timer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

import com.igo.ui.android.R;
import com.igo.ui.android.adapter.TaskExpViewAdapter;
import com.igo.ui.android.timer.MessageTimerTask;

public class LaunchpadSectionFragment extends Fragment {
	private Timer timer = null;
	private ExpandableListView view;
	private TaskExpViewAdapter adapter;
	private View statusView = null;
	
	public LaunchpadSectionFragment(View statusView){
		super();
		this.statusView = statusView;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("LaunchpadSectionFragment.onCreateView");
		
		View rootView = inflater.inflate(R.layout.fragment_explistview,
				container, false);

		view = (ExpandableListView) rootView.findViewById(R.id.explist_view);
		adapter = new TaskExpViewAdapter(getActivity().getApplicationContext(), statusView);
		view.setAdapter(adapter);
		view.setOnGroupExpandListener(new OnGroupExpandListener() {
			public void onGroupExpand(int groupPosition) {
				int len = adapter.getGroupCount();
				for (int i = 0; i < len; i++) {
					if (i != groupPosition) {
						view.collapseGroup(i);
					}
				}
			}
		});

		MessageTimerTask task = MessageTimerTask.getInstance(getActivity()
				.getApplicationContext(), adapter);
		timer = new Timer("JsonTimer");
		timer.schedule(task, 0, 5000);

		return rootView;
	}

	@Override
	public void onDestroyView() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		super.onDestroyView();
	}
}
