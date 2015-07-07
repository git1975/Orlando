package com.igo.ui.android;

import com.igo.ui.android.domain.ChatsItem;
import com.igo.ui.android.domain.Login;
import com.igo.ui.android.fragment.ChatSectionFragment;
import com.igo.ui.android.remote.Command;
import com.igo.ui.android.remote.CommandConnector;
import com.igo.ui.android.remote.OnCommandEndListener;

import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WorkActivity extends ActionBarActivity implements OnCommandEndListener{
	private String[] mScreenTitles;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ChatsItem[] chatsItems;

	private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

		mScreenTitles = new String[0];//getResources().getStringArray(R.array.screen_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		// Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mScreenTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setBackgroundResource(R.drawable.ic_abstract);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                //R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description */
                R.string.drawer_close /* "close drawer" description */
                ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
            	supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
        // Initialize the first fragment when the application first loads.
        if (savedInstanceState == null) {
            selectItem(0);
        }

        loadChats();
    }
    
    private void loadChats(){
    	DataStorage ds = (DataStorage) getApplicationContext();
		Login login = (Login) ds.getData("login");
		
    	Command command = new Command(Command.GET_CHATS);
		command.putParam("login", login.getLogin());
		CommandConnector con = new CommandConnector(getApplicationContext(), command);
		con.setOnCommandEndListener(this);
		con.execute("");
    }
    
     /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
    	if(position > mScreenTitles.length - 1){
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
 
            // Highlight the selected item, update the title, and close the drawer
    	    mDrawerList.setItemChecked(position, true);
    	    setTitle(mScreenTitles[position]);
    	    mDrawerLayout.closeDrawer(mDrawerList);
        }
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
		if(result == null){
			return;
		}
		chatsItems = (ChatsItem[]) result;
		
		mScreenTitles = new String[chatsItems.length];
		for(int i = 0; i < chatsItems.length; i++){
			mScreenTitles[i] = chatsItems[i].getName();
		}
		
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mScreenTitles));
		
		selectItem(0);
	}

	public String getLastHash() {
		// TODO Auto-generated method stub
		return null;
	}

}
