package com.igo.ui.android;

import com.igo.ui.android.domain.Login;
import com.igo.ui.android.fragment.ChatSectionFragment;
import com.igo.ui.android.fragment.LaunchpadSectionFragment;
import com.igo.ui.android.fragment.ReportSectionFragment;

import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WorkActivityOld extends ActionBarActivity /*
													 * implements
													 * ActionBar.TabListener
													 */{

	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the
	 * app, one at a time.
	 */
	ViewPager mViewPager;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		DataStorage ds = (DataStorage) getApplicationContext();
		Login login = (Login) ds.getData("login");
		// TextView tvTitle = (TextView) findViewById(R.id.tv_title);
		// tvTitle.setText(login.getName());

		/*android.app.FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		android.app.Fragment fragment = new ChatSectionFragment();
		fragmentTransaction.add(R.layout.main, fragment);
		fragmentTransaction.commit();*/

		// Create the adapter that will return a fragment for each of the three
		// primary sections
		// of the app.
		/*
		 * mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
		 * getSupportFragmentManager());
		 * 
		 * // Set up the action bar. /* final ActionBar actionBar =
		 * getActionBar();
		 * 
		 * // Specify that the Home/Up button should not be enabled, since there
		 * is // no hierarchical // parent.
		 * actionBar.setHomeButtonEnabled(false);
		 * 
		 * // Specify that we will be displaying tabs in the action bar.
		 * actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		 * 
		 * // Set up the ViewPager, attaching the adapter and setting up a
		 * listener // for when the // user swipes between sections. mViewPager
		 * = (ViewPager) findViewById(R.id.pager);
		 * mViewPager.setAdapter(mAppSectionsPagerAdapter); mViewPager
		 * .setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
		 * 
		 * @Override public void onPageSelected(int position) {
		 * actionBar.setSelectedNavigationItem(position); } });
		 * 
		 * // For each of the sections in the app, add a tab to the action bar.
		 * for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
		 * actionBar.addTab(actionBar.newTab()
		 * .setText(mAppSectionsPagerAdapter.getPageTitle(i))
		 * .setTabListener(this)); }
		 */
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the primary sections of the app.
	 */
	public class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			/*
			 * case 0: View v = findViewById(R.id.v_status); return new
			 * LaunchpadSectionFragment(v); case 1: return new
			 * ChatSectionFragment(); case 2: return new
			 * ReportSectionFragment();
			 */
			case 0:
				//return new ChatSectionFragment();

			default:
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
				fragment.setArguments(args);
				return fragment;
			}
		}

		@Override
		public int getCount() {
			return 1;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position == 0) {
				return "Переписка";
			} else if (position == 1) {
				return "Переписка";
			} else if (position == 2) {
				return "Отчеты";
			}
			return "Section " + (position + 1);
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_section_dummy,
					container, false);
			Bundle args = getArguments();
			((TextView) rootView.findViewById(android.R.id.text1))
					.setText(getString(R.string.dummy_section_text,
							args.getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
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
}
