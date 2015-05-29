package com.igo.ui.android.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.igo.ui.android.R;
import com.igo.ui.android.domain.ChatMessage;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.OnCommandEndListener;
import com.igo.ui.android.timer.ChatTimerTask;
import com.igo.ui.android.widget.ChatMessageView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ChatViewAdapter extends BaseAdapter implements ListAdapter,
		OnCommandEndListener, OnScrollListener {
	private Context context;
	private ListView listView;
	private List<ChatMessage> items = new ArrayList<ChatMessage>(0);

	public Context getContext() {
		return context;
	}

	public ChatViewAdapter(Context c, ListView listView) {
		this.context = c;
		this.listView = listView;

		ChatTimerTask task = ChatTimerTask.getInstance(context, this);
		task.run();

		this.listView.setOnScrollListener(this);
	}

	public int getCount() {
		if (items != null) {
			return items.size();
		}

		if (items == null) {
			return 0;
		}
		return items.size();
	}

	public Object getItem(int position) {
		return R.drawable.ic_launcher;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ChatMessageView view = new ChatMessageView(context, items.get(position));

		return view;
	}

	public boolean contains(ChatMessage item) {
		for (ChatMessage it : items) {
			if (it.getId().equals(item.getId())) {
				return true;
			}
		}
		return false;
	}

	public void OnCommandEnd(Command command, Object result) {
		if (result == null) {
			return;
		}
		ChatMessage[] newItems = (ChatMessage[]) result;
		List<ChatMessage> newList = Arrays.asList(newItems);
		boolean hasNew = false;
		for (ChatMessage item : newList) {
			if (!contains(item)) {
				items.add(item);
				hasNew = true;
			}
		}

		if (hasNew) {
			Collections.sort(items, new Comparator<ChatMessage>() {
				public int compare(final ChatMessage item1, ChatMessage item2) {
					if (item1.getIdLong() > item2.getIdLong()) {
						return 1;
					} else if (item1.getIdLong() < item2.getIdLong()) {
						return -1;
					} else {
						return 0;
					}
				}
			});

			this.notifyDataSetChanged();
		}
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (view.getFirstVisiblePosition() == 0) {
			ChatTimerTask task = ChatTimerTask.getInstance(context, this);
			if (items.size() > 0) {
				task.setMinid(items.get(0).getIdLong());
			}
			task.run();
		}
	}
}
