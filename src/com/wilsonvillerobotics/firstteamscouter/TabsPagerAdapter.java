package com.wilsonvillerobotics.firstteamscouter;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
 
public class TabsPagerAdapter extends FragmentPagerAdapter {
 
	private Integer teamMatchID;
	private String teamNumber;
	private String matchNumber;
	private TeamMatchData tmData;
	
    public TabsPagerAdapter(FragmentManager fm, TeamMatchData tmD) {  //, Integer tmID, String tNum, String mNum) {
        super(fm);
        this.tmData = tmD;
        
        if(this.tmData != null) {
        	this.teamMatchID = tmData.teamMatchID;
            this.teamNumber = tmData.teamNumber;
            this.matchNumber = tmData.matchNumber;
        } else {
	        this.teamMatchID = -1;
	        this.teamNumber = "";
	        this.matchNumber = "";
        	//this.teamMatchID = tmID;
	        //this.teamNumber = tNum;
	        //this.matchNumber = mNum;
        }
    }
 
    @Override
    public Fragment getItem(int index) {
 
        switch (index) {
        case 0:
            TeamMatchAutoModeFragment tmaFrag = TeamMatchAutoModeFragment.newInstance(this.teamMatchID);
            tmaFrag.setTeamMatchData(tmData);
            return tmaFrag;
        case 1:
        	TeamMatchTeleModeFragment tmtFrag =  TeamMatchTeleModeFragment.newInstance(this.teamMatchID);
        	tmtFrag.setTeamMatchData(tmData);
        	return tmtFrag;
        case 2:
        	return TeamMatchNotesFragment.newInstance(this.teamMatchID, this.teamNumber, this.matchNumber);
        }
 
        return null;
    }
    
    public String getTabText(int index) {
        switch (index) {
        case 0:
            return TeamMatchAutoModeFragment.myTitle;
        case 1:
            return TeamMatchTeleModeFragment.myTitle;
        case 2:
            return TeamMatchNotesFragment.myTitle;
        }
 
        return null;
    }
 
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }
 
}
