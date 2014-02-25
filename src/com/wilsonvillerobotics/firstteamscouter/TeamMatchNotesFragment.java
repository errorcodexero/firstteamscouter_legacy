package com.wilsonvillerobotics.firstteamscouter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

public class TeamMatchNotesFragment extends Fragment {
	public static String myTitle;
	private Integer teamMatchID;
	private String teamNumber;
	private String matchNumber;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	myTitle = "Notes";
 
    	this.teamMatchID = getArguments() != null ? getArguments().getInt("tmID") : -1;
    	this.teamNumber = getArguments() != null ? getArguments().getString("tNum") : "";
    	this.matchNumber = getArguments() != null ? getArguments().getString("mNum") : "";    	

    	View rootView = inflater.inflate(R.layout.fragment_team_match_notes, container, false);
         
        return rootView;
    }

    static TeamMatchNotesFragment newInstance(Integer tmID, String tNum, String mNum) {
    	TeamMatchNotesFragment f = new TeamMatchNotesFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("tmID", tmID);
        args.putString("tNum", tNum);
        args.putString("mNum", mNum);
        f.setArguments(args);

        return f;
    }
}