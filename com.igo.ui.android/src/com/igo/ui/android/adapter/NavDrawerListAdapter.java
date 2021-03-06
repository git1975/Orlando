package com.igo.ui.android.adapter;

import java.util.ArrayList;

import com.igo.ui.android.R;
import com.igo.ui.android.domain.NavDrawerItem;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<NavDrawerItem> navDrawerItems;

	public NavDrawerListAdapter(Context context,
			ArrayList<NavDrawerItem> navDrawerItems) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	public int getCount() {
		return navDrawerItems.size();
	}

	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.drawer_img_item, parent, false);
        }
          
        ImageView imgIcon = (ImageView) convertView.findViewById(R.id.icon);
        TextView txtTitle = (TextView) convertView.findViewById(R.id.title);
        TextView txtCount = (TextView) convertView.findViewById(R.id.counter);
        
        int icon = 0;
        switch (navDrawerItems.get(position).getType()){
        	case NavDrawerItem.TYPE_PROCESS: icon = R.drawable.ic_process; break;
        	case NavDrawerItem.TYPE_PERSONAL: icon = R.drawable.ic_user; break;
        	case NavDrawerItem.TYPE_CHILD: icon = R.drawable.ic_link; break;
        }
        imgIcon.setImageResource(icon);        
        txtTitle.setText(navDrawerItems.get(position).getTitle());
         
        // displaying count
        // check whether it set visible or not
        if(navDrawerItems.get(position).getCounterVisibility()){
            txtCount.setText(navDrawerItems.get(position).getCount());
        }else{
            // hide the counter view
            txtCount.setVisibility(View.GONE);
        }
         
        return convertView;
    }
}