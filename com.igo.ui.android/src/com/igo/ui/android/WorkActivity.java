package com.igo.ui.android;

import java.util.ArrayList;

import com.igo.ui.android.adapter.NavDrawerListAdapter;
import com.igo.ui.android.domain.ChatsItem;
import com.igo.ui.android.domain.Login;
import com.igo.ui.android.domain.NavDrawerItem;
import com.igo.ui.android.fragment.ChatSectionFragment;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;
import com.igo.ui.android.remote.OnCommandEndListener;
import com.igo.ui.android.service.RemoteBroadcastReceiver;
import com.igo.ui.android.service.RemoteService;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class WorkActivity extends ActionBarActivity implements
		OnCommandEndListener {
	private String[] mScreenTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ChatsItem[] chatsItems;
	private int lastChatItem = 0;

	private final BroadcastReceiver rbr = new RemoteBroadcastReceiver(this);

	private ArrayList<NavDrawerItem> navDrawerItems;

	private ActionBarDrawerToggle mDrawerToggle;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		mScreenTitles = new String[0];// getResources().getStringArray(R.array.screen_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Set the adapter for the list view
		// mDrawerList.setAdapter(new ArrayAdapter<String>(this,
		// R.layout.drawer_list_item, mScreenTitles));

		// Set the list's click listener
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mDrawerList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		mDrawerList.setBackgroundResource(R.drawable.ic_abstract);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.string.drawer_open, R.string.drawer_close) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getSupportActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Initialize the first fragment when the application first loads.
		if (savedInstanceState == null) {
			selectItem(0);
		}

		registerReceiver(rbr,
				new IntentFilter(RemoteService.IGO_SERVICE_ACTION));

		loadChats();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(rbr);

		super.onDestroy();
	}

	private void loadChats() {
		DataStorage ds = (DataStorage) getApplicationContext();
		Login login = (Login) ds.getData("login");

		Command command = new Command(Command.GET_CHATS);
		command.putParam("login", login.getLogin());

		startService(new Intent(this, RemoteService.class).putExtra(
				Command.PARAM_COMMAND, command.getJson()));
	}

	private void startSubСase(String code, String parentchat) {
		DataStorage ds = (DataStorage) getApplicationContext();
		Login login = (Login) ds.getData("login");

		Command command = new Command(Command.STARTSUBCASE);
		command.putParam("login", login.getLogin());
		command.putParam("process", code);
		command.putParam("parentchat", parentchat);

		startService(new Intent(this, RemoteService.class).putExtra(
				Command.PARAM_COMMAND, command.getJson()));
	}

	/* The click listener for ListView in the navigation drawer */
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	/* Swaps fragments in the main content view */
	private void selectItem(int position) {
		if (position > mScreenTitles.length - 1) {
			return;
		}
		// Если выбран элемент Старт подпроцесса, то отправить команду на старт подпроцесса
		if(chatsItems[position].isIschild()){			
			startSubСase(chatsItems[position].getCode(), chatsItems[lastChatItem].getCode());
			mDrawerList.setItemChecked(lastChatItem, true);
			mDrawerLayout.closeDrawer(mDrawerList);
			return;
		}
		// Update the main content by replacing fragments
		DataStorage ds = (DataStorage) getApplicationContext();
		Login login = (Login) ds.getData("login");
		Fragment fragment = new ChatSectionFragment(chatsItems[position], login);

		// Insert the fragment by replacing any existing fragment
		if (fragment != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();

			// Highlight the selected item, update the title, and close the
			// drawer
			mDrawerList.setItemChecked(position, true);
			setTitle(mScreenTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		}
		lastChatItem = position;
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggles
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch (item.getItemId()) {
		case R.id.mi_edit_settings: {
			startActivity(new Intent(this, SettingsActivity.class));
			return true;
		}
		case R.id.mi_app_quit: {
			finish();
			return true;
		}
		}

		return false;
	}

	@Override
	public void onBackPressed() {
		finish();
	}

	public void OnCommandEnd(Command command, Object result) {
		if (result == null) {
			return;
		}
		if (Command.GET_CHATS.equals(command.getCommand())) {
			chatsItems = (ChatsItem[]) result;

			mScreenTitles = new String[chatsItems.length];
			for (int i = 0; i < chatsItems.length; i++) {
				mScreenTitles[i] = chatsItems[i].getName();
			}

			// mDrawerList.setAdapter(new ArrayAdapter<String>(this,
			// R.layout.drawer_list_item, mScreenTitles));

			navDrawerItems = new ArrayList<NavDrawerItem>(chatsItems.length);
			for (int i = 0; i < chatsItems.length; i++) {
				int type = NavDrawerItem.TYPE_PROCESS;
				if (chatsItems[i].isIspersonal()) {
					type = NavDrawerItem.TYPE_PERSONAL;
				}
				if (chatsItems[i].isIschild()) {
					type = NavDrawerItem.TYPE_CHILD;
				}
				navDrawerItems.add(new NavDrawerItem(chatsItems[i].getName(),
						type, false, "333"));
			}

			mDrawerList.setAdapter(new NavDrawerListAdapter(this,
					navDrawerItems));

			selectItem(0);
		}
		if (Command.STARTSUBCASE.equals(command.getCommand())) {
			Toast.makeText(getApplicationContext(), result.toString(),
					Toast.LENGTH_SHORT).show();
		}
	}

	public String getLastHash() {
		return null;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

}
