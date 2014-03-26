package com.wilsonvillerobotics.firstteamscouter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class TeamMatchTeamNumberDisplayFragment extends Fragment {

	private TeamMatchData tmData;
	private int teamMatchID;
	public static String myTitle;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

    	this.myTitle = "Team Number Display";
    	this.teamMatchID = getArguments() != null ? getArguments().getInt("tmID") : -1;
    	
        View rootView = inflater.inflate(R.layout.fragment_team_match_teamnumber_display, container, false);
		
        TextView txtTeamNumberDisplay = (TextView) rootView.findViewById(R.id.txtTeamNumberDisplay);
        txtTeamNumberDisplay.setText(this.tmData.teamNumber);
        
		return rootView;
	}
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    }
    
    static TeamMatchTeamNumberDisplayFragment newInstance(Integer tmID) {
    	TeamMatchTeamNumberDisplayFragment f = new TeamMatchTeamNumberDisplayFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("tmID", tmID);
        f.setArguments(args);

        return f;
    }
    
    public void setTeamMatchData(TeamMatchData tmD) {
    	this.tmData = tmD;
    }
}
