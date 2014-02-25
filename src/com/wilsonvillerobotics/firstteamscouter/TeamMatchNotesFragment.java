package com.wilsonvillerobotics.firstteamscouter;

import com.wilsonvillerobotics.firstteamscouter.utilities.FTSUtilities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.support.v4.app.Fragment;

public class TeamMatchNotesFragment extends Fragment implements OnClickListener{
	public static String myTitle;
	private Integer teamMatchID;
	private String teamNumber;
	private String matchNumber;
	private ToggleButton tbtnBrokeDown;
	private ToggleButton tbtnNoMove;
	private ToggleButton tbtnLostConnection;
	private EditText txtTMNotes;
	
	private TeamMatchData tmData;
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	myTitle = "Notes";
 
    	this.teamMatchID = getArguments() != null ? getArguments().getInt("tmID") : -1;
    	this.teamNumber = getArguments() != null ? getArguments().getString("tNum") : "";
    	this.matchNumber = getArguments() != null ? getArguments().getString("mNum") : "";    	

    	View rootView = inflater.inflate(R.layout.fragment_team_match_notes, container, false);
        
    	this.tbtnBrokeDown = (ToggleButton) rootView.findViewById(R.id.tbtnBrokeDown);
    	this.tbtnNoMove = (ToggleButton) rootView.findViewById(R.id.tbtnNoMove);
    	this.tbtnLostConnection = (ToggleButton) rootView.findViewById(R.id.tbtnLostConnection);
    	this.txtTMNotes = (EditText) rootView.findViewById(R.id.txtTMNotes);
    	
    	this.tbtnBrokeDown.setOnClickListener(this);
    	this.tbtnNoMove.setOnClickListener(this);
    	this.tbtnLostConnection.setOnClickListener(this);
    	
    	this.updateToggleButtonStates();
    	
        return rootView;
    }
    
    private void updateToggleButtonStates() {
    	this.tbtnBrokeDown.setChecked(this.tmData.brokeDown);
    	this.tbtnNoMove.setChecked(this.tmData.noMove);
    	this.tbtnLostConnection.setChecked(this.tmData.lostConnection);
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
    
    public void setTeamMatchData(TeamMatchData tmD) {
    	this.tmData = tmD;
    }
    
    @Override
	public void onClick(View v) {
		switch (v.getId()) {
        case R.id.tbtnBrokeDown:
        	tbtnBrokeDownOnClick(v);
			break;
        case R.id.tbtnNoMove:
        	tbtnNoMoveOnClick(v);
			break;
        case R.id.tbtnLostConnection:
        	tbtnLostConnectionOnClick(v);
			break;
		}
	}
    
    private void tbtnLostConnectionOnClick(View v) {
    	boolean on = ((ToggleButton) v).isChecked();
    	FTSUtilities.printToConsole("Lost Connection is checked: " + String.valueOf(on) + "\n");
    	this.tmData.lostConnection = ((ToggleButton) v).isChecked();
	}

	private void tbtnNoMoveOnClick(View v) {
		this.tmData.noMove = ((ToggleButton) v).isChecked();
	}

	private void tbtnBrokeDownOnClick(View v) {
    	this.tmData.brokeDown = ((ToggleButton) v).isChecked();
	}
}