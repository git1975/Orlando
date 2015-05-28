package com.igo.ui.android.adapter;

import com.igo.ui.android.R;
import com.igo.ui.android.domain.ChatMessage;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.OnCommandEndListener;
import com.igo.ui.android.widget.ChatMessageView;
import com.igo.ui.android.widget.MessageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ChatViewAdapter extends BaseAdapter implements ListAdapter,
		OnCommandEndListener {
	private Context context;
	private ListView listView;

	public Context getContext() {
		return context;
	}

	private ChatMessage[] items = null;

	public ChatViewAdapter(Context c, ListView listView) {
		this.context = c;
		this.listView = listView;
	}

	public int getCount() {
		if (items != null) {
			return items.length;
		}

		if (items == null) {
			return 0;
		}
		return items.length;
	}

	public Object getItem(int position) {
		return R.drawable.ic_launcher;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ChatMessageView view = new ChatMessageView(context, items[position]);

		return view;
	}

	public void OnCommandEnd(Command command, Object result) {
		this.items = (ChatMessage[]) result;

		this.notifyDataSetChanged();

		if (listView != null) {
			if (listView.getSelectedItemPosition() != listView.getCount() - 1) {
				listView.setSelection(listView.getCount() - 1);
			}
		}
	}
}
