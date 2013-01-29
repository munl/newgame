package com.newgame;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class EventDetailsActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventdetails);
				
		ActionBar bar = getActionBar();
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		ActionBar.Tab eventgen = bar.newTab().setText("General");
		ActionBar.Tab eventatt = bar.newTab().setText("Attending");
		ActionBar.Tab eventinv = bar.newTab().setText("Invited");
		Fragment eventGenFragment = new EventsGeneralFragment();
		Fragment eventAttFragment = new EventsAttendingFragment();
		Fragment eventInvFragment = new EventsInvitedFragment();
		eventgen.setTabListener(new MyTabsListener(eventGenFragment));
		eventatt.setTabListener(new MyTabsListener(eventAttFragment));
		eventinv.setTabListener(new MyTabsListener(eventInvFragment));
		bar.addTab(eventgen);
		bar.addTab(eventatt);
		bar.addTab(eventinv);
		
		/*
		bar.addTab(bar.newTab().setText("General")
				);
		bar.addTab(bar.newTab().setText("Attending")
				);
		bar.addTab(bar.newTab().setText("invited")
				);

		*/
		
	}
	
	protected class MyTabsListener implements ActionBar.TabListener {
		
		private Fragment fragment;
		
		public MyTabsListener(Fragment fragment){
			
			this.fragment = fragment;
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.replace(R.id.details, fragment);
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			// TODO Auto-generated method stub
			
		}
		
		
	}

}
