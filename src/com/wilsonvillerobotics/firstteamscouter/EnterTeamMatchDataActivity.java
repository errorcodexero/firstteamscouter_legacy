package com.wilsonvillerobotics.firstteamscouter;

import com.wilsonvillerobotics.firstteamscouter.dbAdapters.TeamMatchDBAdapter;
import com.wilsonvillerobotics.firstteamscouter.utilities.FTSUtilities;

import android.os.Bundle;
import android.app.ActionBar.Tab;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.TextView;
import android.content.Intent;

public class EnterTeamMatchDataActivity extends FragmentActivity implements ActionBar.TabListener {

	protected TeamMatchData tmData;
	
	private ViewPager viewPager;
	private ActionBar actionBar;
	private TabsPagerAdapter mAdapter;
	private TextView txtTeamNumber;
	private TextView txtMatchNumber;
	private String tabletID;
	
	private int viewState = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_match_pager);
		
		FTSUtilities.printToConsole("EnterTeamMatchDataActivity::OnCreate : viewState: " + viewState + "\n");
		viewState++;
		
		Intent intent = getIntent();
		Integer teamMatchID = Integer.valueOf(intent.getStringExtra(TeamMatchDBAdapter._ID));
		this.tabletID = intent.getStringExtra("tablet_id");
		this.tabletID = (this.tabletID != null) ? this.tabletID : "Unknown Tablet ID";
		//String teamNumber = intent.getStringExtra(TeamMatchDBAdapter.COLUMN_NAME_TEAM_ID);
		//String matchNumber = intent.getStringExtra(TeamMatchDBAdapter.COLUMN_NAME_MATCH_ID);
		
		
		FTSUtilities.printToConsole("EnterTeamMatchDataActivity::OnCreate : Creating TeamMatchData\n");
		this.tmData = new TeamMatchData(this.getBaseContext(), this.tabletID, teamMatchID); //, teamNumber, matchNumber);
		
		// Initialization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager(), tmData); //, teamMatchID, teamNumber, matchNumber);
        
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        // Adding Tabs
        for (String tab_name : TabsPagerAdapter.tabTitles) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
        
        txtTeamNumber = (TextView) findViewById(R.id.txtTeamNumber);
        if(txtTeamNumber != null && this.tmData != null) {
			txtTeamNumber.setText(this.tmData.teamNumber);
		}
        
        txtMatchNumber = (TextView) findViewById(R.id.txtMatchNumber);
        if(txtMatchNumber != null && this.tmData != null) {
        	txtMatchNumber.setText(this.tmData.matchNumber);
		}
        
        
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        	 
            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }
         
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
         
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
	}

	@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
		FTSUtilities.printToConsole("EnterTeamMatchDataActivity::onRestoreInstanceState : viewState: " + viewState + "\n");
		viewState++;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FTSUtilities.printToConsole("EnterTeamMatchDataActivity::onRestart : viewState: " + viewState + "\n");
		viewState++;
		this.tmData.openDB();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FTSUtilities.printToConsole("EnterTeamMatchDataActivity::onStart : viewState: " + viewState + "\n");
		viewState++;
    }

    @Override
    protected void onResume() {
        super.onResume();
        FTSUtilities.printToConsole("EnterTeamMatchDataActivity::onResume : viewState: " + viewState + "\n");
		viewState++;
		this.tmData.openDB();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        FTSUtilities.printToConsole("EnterTeamMatchDataActivity::onSaveInstanceState : viewState: " + viewState + "\n");
		viewState++;
    }

    @Override
    protected void onPause() {
        super.onPause();
        FTSUtilities.printToConsole("EnterTeamMatchDataActivity::onPause : viewState: " + viewState + "\n");
		viewState++;
        FTSUtilities.printToConsole("EnterTeamMatchDataActivity::onPause : SAVING DATA TO DB\n");
		this.tmData.save();
		this.tmData.closeDB();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FTSUtilities.printToConsole("EnterTeamMatchDataActivity::onStop : viewState: " + viewState + "\n");
		viewState++;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FTSUtilities.printToConsole("EnterTeamMatchDataActivity::onDestroy : viewState: " + viewState + "\n");
		viewState++;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.enter_data, menu);
        FTSUtilities.printToConsole("EnterTeamMatchDataActivity::onCreateOptionsMenu : viewState: " + viewState + "\n");
		viewState++;
		return true;
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
        FTSUtilities.printToConsole("EnterTeamMatchDataActivity::onTabReselected : viewState: " + viewState + "\n");
		viewState++;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
        FTSUtilities.printToConsole("EnterTeamMatchDataActivity::onTabSelected : viewState: " + viewState + "\n");
		viewState++;
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        FTSUtilities.printToConsole("EnterTeamMatchDataActivity::onTabUnselected : viewState: " + viewState + "\n");
		viewState++;
	}
}
