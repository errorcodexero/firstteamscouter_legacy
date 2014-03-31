package com.wilsonvillerobotics.firstteamscouter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class TeamMatchTeamNumberDisplayFragment extends Fragment {

	private TeamMatchData tmData;
	protected Long teamMatchID;
	public static String myTitle = "Team Number Display";
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

    	this.teamMatchID = getArguments() != null ? getArguments().getLong("tmID") : -1;
    	
        View rootView = inflater.inflate(R.layout.fragment_team_match_teamnumber_display, container, false);
		
        TextView txtTeamNumberDisplay = (TextView) rootView.findViewById(R.id.txtTeamNumberDisplay);
        txtTeamNumberDisplay.setText(this.tmData.getTeamNumber());
        
		return rootView;
	}
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    }
    
    static TeamMatchTeamNumberDisplayFragment newInstance(Long teamMatchID2) {
    	TeamMatchTeamNumberDisplayFragment f = new TeamMatchTeamNumberDisplayFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putLong("tmID", teamMatchID2);
        f.setArguments(args);

        return f;
    }
    
    public void setTeamMatchData(TeamMatchData tmD) {
    	this.tmData = tmD;
    }
}
