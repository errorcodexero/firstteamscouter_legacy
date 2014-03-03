package com.wilsonvillerobotics.firstteamscouter;

import java.util.Hashtable;

import com.wilsonvillerobotics.firstteamscouter.TeamMatchData.STARTING_LOC;
import com.wilsonvillerobotics.firstteamscouter.utilities.FTSUtilities;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.support.v4.app.Fragment;

public class TeamMatchStartingPositionFragment extends Fragment implements OnClickListener {

	private TeamMatchData tmData;
	private int teamMatchID;
	Hashtable<STARTING_LOC, ToggleButton> buttonHash;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

    	this.teamMatchID = getArguments() != null ? getArguments().getInt("tmID") : -1;
    	
        View rootView = inflater.inflate(R.layout.fragment_team_match_starting_position, container, false);
        
        buttonHash = new Hashtable<STARTING_LOC, ToggleButton>();
        
        //txtWidth = (TextView) rootView.findViewById(R.id.txtWidth);
        //txtHeight = (TextView) rootView.findViewById(R.id.txtHeight);

        ToggleButton btnStartGoal = (ToggleButton) rootView.findViewById(R.id.btnStartGoal);
		btnStartGoal.setOnClickListener(this);
		buttonHash.put(STARTING_LOC.FIELD_GOAL, btnStartGoal);
        
		ToggleButton btnStartLeft = (ToggleButton) rootView.findViewById(R.id.btnStartLeft);
		btnStartLeft.setOnClickListener(this);
		buttonHash.put(STARTING_LOC.FIELD_LEFT, btnStartLeft);
		
		ToggleButton btnStartLeftCenter = (ToggleButton) rootView.findViewById(R.id.btnStartLeftCenter);
		btnStartLeftCenter.setOnClickListener(this);
		buttonHash.put(STARTING_LOC.FIELD_LEFT_CENTER, btnStartLeftCenter);
		
		ToggleButton btnStartCenter = (ToggleButton) rootView.findViewById(R.id.btnStartCenter);
		btnStartCenter.setOnClickListener(this);
		buttonHash.put(STARTING_LOC.FIELD_CENTER, btnStartCenter);
		
		ToggleButton btnStartRightCenter = (ToggleButton) rootView.findViewById(R.id.btnStartRightCenter);
		btnStartRightCenter.setOnClickListener(this);
		buttonHash.put(STARTING_LOC.FIELD_RIGHT_CENTER, btnStartRightCenter);
		
		ToggleButton btnStartRight = (ToggleButton) rootView.findViewById(R.id.btnStartRight);
		btnStartRight.setOnClickListener(this);
		buttonHash.put(STARTING_LOC.FIELD_RIGHT, btnStartRight);

		updateToggleButtonStates();
		
		return rootView;
	}
    
    private void updateToggleButtonStates() {
    	FTSUtilities.printToConsole("TeamMatchStartingPositionFragment::updateToggleButtons\n");
    	if(this.tmData != null) {
    		STARTING_LOC startPos = this.tmData.getStartingPosition();
    		FTSUtilities.printToConsole("TeamMatchStartingPositionFragment::updateToggleButtons : SL: " + startPos.toString() + "\n");
    		if(startPos != STARTING_LOC.FIELD_NOT_SET) {
    			this.buttonHash.get(startPos).setChecked(true);
    		}
    	}
    }
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
/*    	final View myView = getView();
    	
        
    	myView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                fragmentWidth = getView().getWidth();
                fragmentHeight = getView().getHeight();
                if(fragmentWidth > 0)
                {
                    // Width greater than 0, layout should be done.
                    // Set the textviews and remove the listener.
                	txtWidth.setText(String.valueOf(fragmentWidth));
                	txtHeight.setText(String.valueOf(fragmentHeight));
                	
                    getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
          });*/
    }
    
    static TeamMatchStartingPositionFragment newInstance(Integer tmID) {
    	TeamMatchStartingPositionFragment f = new TeamMatchStartingPositionFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("tmID", tmID);
        f.setArguments(args);

        return f;
    }
    
    public void setTeamMatchData(TeamMatchData tmD) {
    	this.tmData = tmD;
    }
    
    @Override
	public void onClick(View v) {
    	switch (v.getId()) {
        case R.id.btnStartGoal:
        	btnStartGoalOnClick(v);
        	break;
        case R.id.btnStartLeft:
        	btnStartLeftOnClick(v);
        	break;
        case R.id.btnStartLeftCenter:
        	btnStartLeftCenterOnClick(v);
        	break;
        case R.id.btnStartCenter:
        	btnStartCenterOnClick(v);
    		break;
        case R.id.btnStartRightCenter:
        	btnStartRightCenterOnClick(v);
        	break;
        case R.id.btnStartRight:
        	btnStartRightOnClick(v);
        	break;
        }
    }
    
	private void resetToggleButtonsExcept(STARTING_LOC loc) {
		ToggleButton tempButton;

		for(STARTING_LOC l : buttonHash.keySet()) {
			if(l != loc) {
				FTSUtilities.printToConsole("TeamMatchStartingPositionFragment::resetToggleButtonsExcept : Resetting: " + l.toString() + "\n");
				tempButton = (ToggleButton) buttonHash.get(l);
				tempButton.setChecked(false);
				tempButton.setPressed(false);
			}
		}
	}
	
	private void setStartingLoc(STARTING_LOC sl) {
		if(this.tmData != null) {
			FTSUtilities.printToConsole("TeamMatchStartingPositionFragment::setStartingLoc : Setting SL in tmData: " + sl.toString() + "\n");
			this.tmData.setStartingLoc(sl);
		}
	}
	
	private void setOrResetStartingLoc(STARTING_LOC sl, boolean isChecked) {
		if(isChecked) {
			FTSUtilities.printToConsole("TeamMatchStartingPositionFragment::setOrResetStartingLoc : Setting SL: " + sl.toString() + "\n");
			this.setStartingLoc(sl);
		} else {
			FTSUtilities.printToConsole("TeamMatchStartingPositionFragment::setOrResetStartingLoc : Resetting SL: " + sl.toString() + "\n");
			this.setStartingLoc(STARTING_LOC.FIELD_NOT_SET);
		}
	}

	private void btnStartRightOnClick(View v) {
		this.resetToggleButtonsExcept(STARTING_LOC.FIELD_RIGHT);
		ToggleButton tb = (ToggleButton)v;
		this.setOrResetStartingLoc(STARTING_LOC.FIELD_RIGHT, tb.isChecked());
	}


	private void btnStartRightCenterOnClick(View v) {
		this.resetToggleButtonsExcept(STARTING_LOC.FIELD_RIGHT_CENTER);
		ToggleButton tb = (ToggleButton)v;
		this.setOrResetStartingLoc(STARTING_LOC.FIELD_RIGHT_CENTER, tb.isChecked());
	}

	private void btnStartCenterOnClick(View v) {
		this.resetToggleButtonsExcept(STARTING_LOC.FIELD_CENTER);
		ToggleButton tb = (ToggleButton)v;
		this.setOrResetStartingLoc(STARTING_LOC.FIELD_CENTER, tb.isChecked());
	}

	private void btnStartLeftCenterOnClick(View v) {
		this.resetToggleButtonsExcept(STARTING_LOC.FIELD_LEFT_CENTER);
		ToggleButton tb = (ToggleButton)v;
		this.setOrResetStartingLoc(STARTING_LOC.FIELD_LEFT_CENTER, tb.isChecked());
	}

	private void btnStartLeftOnClick(View v) {
		this.resetToggleButtonsExcept(STARTING_LOC.FIELD_LEFT);
		ToggleButton tb = (ToggleButton)v;
		this.setOrResetStartingLoc(STARTING_LOC.FIELD_LEFT, tb.isChecked());
	}

	private void btnStartGoalOnClick(View v) {
		this.resetToggleButtonsExcept(STARTING_LOC.FIELD_GOAL);
		ToggleButton tb = (ToggleButton)v;
		this.setOrResetStartingLoc(STARTING_LOC.FIELD_GOAL, tb.isChecked());
	}
}
