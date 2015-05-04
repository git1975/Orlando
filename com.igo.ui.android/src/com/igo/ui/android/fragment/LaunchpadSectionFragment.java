package com.igo.ui.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.igo.ui.android.R;
import com.igo.ui.android.adapter.TaskViewAdapter;

public class LaunchpadSectionFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
        
        //rootView.

        /*rootView.findViewById(R.id.demo_collection_button)
                .setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), CollectionDemoActivity.class);
                        startActivity(intent);
					}
                });

        rootView.findViewById(R.id.demo_external_activity)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
                        externalActivityIntent.setType("image/*");
                        externalActivityIntent.addFlags(
                                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                        startActivity(externalActivityIntent);
                    }
                });*/
 
        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view);

		// устанавливаем адаптер через экземпляр класса ImageAdapter
		gridView.setAdapter(new TaskViewAdapter(container.getContext()));

        return rootView;
    }
}
