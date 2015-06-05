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
import com.igo.ui.android.adapter.ReportViewAdapter;
import com.igo.ui.android.adapter.TaskExpViewAdapter;

public class ReportSectionFragment extends Fragment {
	private ExpandableListView view;
	private ReportViewAdapter adapter;
	private View statusView = null;
	
	public ReportSectionFragment(){
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("ReportSectionFragment.onCreateView");
		
		View rootView = inflater.inflate(R.layout.fragment_reportview,
				container, false);

		view = (ExpandableListView) rootView.findViewById(R.id.report_view);
		adapter = new ReportViewAdapter(getActivity().getApplicationContext());
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

		return rootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}
