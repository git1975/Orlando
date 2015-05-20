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
	private TaskExpViewAdapter tva;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// View rootView = inflater.inflate(R.layout.fragment_section_launchpad,
		// container, false);
		View rootView = inflater.inflate(R.layout.fragment_explistview,
				container, false);
		// View rootView = inflater.inflate(R.layout.message_object_view,
		// container, false);

		/*
		 * rootView.findViewById(R.id.demo_collection_button)
		 * .setOnClickListener(new View.OnClickListener() { public void
		 * onClick(View v) { Intent intent = new Intent(getActivity(),
		 * CollectionDemoActivity.class); startActivity(intent); } });
		 * 
		 * rootView.findViewById(R.id.demo_external_activity)
		 * .setOnClickListener(new View.OnClickListener() { public void
		 * onClick(View view) { Intent externalActivityIntent = new
		 * Intent(Intent.ACTION_PICK);
		 * externalActivityIntent.setType("image/*");
		 * externalActivityIntent.addFlags(
		 * Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		 * startActivity(externalActivityIntent); } });
		 */

		// GridView gridView = (GridView) rootView.findViewById(R.id.grid_view);
		// TaskViewAdapter tva = new TaskViewAdapter(container.getContext());
		// gridView.setAdapter(tva);
		view = (ExpandableListView) rootView.findViewById(R.id.explist_view);
		tva = new TaskExpViewAdapter(getActivity().getApplicationContext());
		view.setAdapter(tva);
		view.setOnGroupExpandListener(new OnGroupExpandListener() {
			public void onGroupExpand(int groupPosition) {
				int len = tva.getGroupCount();
				for (int i = 0; i < len; i++) {
					if (i != groupPosition) {
						view.collapseGroup(i);
					}
				}
			}
		});

		MessageTimerTask mtt = MessageTimerTask.getInstance(getActivity()
				.getApplicationContext(), tva);
		timer = new Timer("JsonTimer");
		timer.schedule(mtt, 0, 5000);

		return rootView;
	}
}
