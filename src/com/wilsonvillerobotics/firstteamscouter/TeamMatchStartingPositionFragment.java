package com.wilsonvillerobotics.firstteamscouter;

import java.util.Hashtable;

import com.wilsonvillerobotics.firstteamscouter.TeamMatchData.STARTING_LOC;
import com.wilsonvillerobotics.firstteamscouter.utilities.FTSUtilities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.support.v4.app.Fragment;

public class TeamMatchStartingPositionFragment extends Fragment implements OnClickListener {

	private TeamMatchData tmData;
	protected Long teamMatchID;
	public static String myTitle = "Starting Position";
	Hashtable<STARTING_LOC, ToggleButton> buttonHash;
	Hashtable<STARTING_LOC, String> helpHash;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

    	this.teamMatchID = getArguments() != null ? getArguments().getLong("tmID") : -1;
    	
        View rootView = inflater.inflate(R.layout.fragment_team_match_starting_position, container, false);
        
        this.helpHash = new Hashtable<STARTING_LOC, String>();
		
        buttonHash = new Hashtable<STARTING_LOC, ToggleButton>();
        
        //txtWidth = (TextView) rootView.findViewById(R.id.txtWidth);
        //txtHeight = (TextView) rootView.findViewById(R.id.txtHeight);

        ToggleButton btnStartGoal = (ToggleButton) rootView.findViewById(R.id.btnStartGoal);
		btnStartGoal.setOnClickListener(this);
		buttonHash.put(STARTING_LOC.FIELD_GOAL, btnStartGoal);
		helpHash.put(STARTING_LOC.FIELD_GOAL, "Positioned in opponents goalie zone");
        
		ToggleButton btnStartLeft = (ToggleButton) rootView.findViewById(R.id.btnStartLeft);
		btnStartLeft.setOnClickListener(this);
		buttonHash.put(STARTING_LOC.FIELD_LEFT, btnStartLeft);
		helpHash.put(STARTING_LOC.FIELD_LEFT, "Left of field, facing goal, truss at back.");
		
		ToggleButton btnStartLeftCenter = (ToggleButton) rootView.findViewById(R.id.btnStartLeftCenter);
		btnStartLeftCenter.setOnClickListener(this);
		buttonHash.put(STARTING_LOC.FIELD_LEFT_CENTER, btnStartLeftCenter);
		helpHash.put(STARTING_LOC.FIELD_LEFT_CENTER, "Left-Center of field, facing goal, truss at back.");
		
		ToggleButton btnStartCenter = (ToggleButton) rootView.findViewById(R.id.btnStartCenter);
		btnStartCenter.setOnClickListener(this);
		buttonHash.put(STARTING_LOC.FIELD_CENTER, btnStartCenter);
		helpHash.put(STARTING_LOC.FIELD_CENTER, "Center of field, facing goal, truss at back.");
		
		ToggleButton btnStartRightCenter = (ToggleButton) rootView.findViewById(R.id.btnStartRightCenter);
		btnStartRightCenter.setOnClickListener(this);
		buttonHash.put(STARTING_LOC.FIELD_RIGHT_CENTER, btnStartRightCenter);
		helpHash.put(STARTING_LOC.FIELD_RIGHT_CENTER, "Right-Center of field, facing goal, truss at back.");
		
		ToggleButton btnStartRight = (ToggleButton) rootView.findViewById(R.id.btnStartRight);
		btnStartRight.setOnClickListener(this);
		buttonHash.put(STARTING_LOC.FIELD_RIGHT, btnStartRight);
		helpHash.put(STARTING_LOC.FIELD_RIGHT, "Right of field, facing goal, truss at back.");

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
    
    static TeamMatchStartingPositionFragment newInstance(Long teamMatchID) {
    	TeamMatchStartingPositionFragment f = new TeamMatchStartingPositionFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putLong("tmID", teamMatchID);
        f.setArguments(args);

        return f;
    }
    
    public void setTeamMatchData(TeamMatchData tmD) {
    	this.tmData = tmD;
    }
    
    @Override
	public void onClick(View v) {
    	ToggleButton tb = (ToggleButton)v;
    	
    	switch (v.getId()) {
        case R.id.btnStartGoal:
        	positionButtonClick(STARTING_LOC.FIELD_GOAL);
        	//btnStartGoalOnClick(v);
        	break;
        case R.id.btnStartLeft:
        	positionButtonClick(STARTING_LOC.FIELD_LEFT);
        	//btnStartLeftOnClick(v);
        	break;
        case R.id.btnStartLeftCenter:
        	positionButtonClick(STARTING_LOC.FIELD_LEFT_CENTER);
        	//btnStartLeftCenterOnClick(v);
        	break;
        case R.id.btnStartCenter:
        	positionButtonClick(STARTING_LOC.FIELD_CENTER);
        	//btnStartCenterOnClick(v);
    		break;
        case R.id.btnStartRightCenter:
        	positionButtonClick(STARTING_LOC.FIELD_RIGHT_CENTER);
        	//btnStartRightCenterOnClick(v);
        	break;
        case R.id.btnStartRight:
        	positionButtonClick(STARTING_LOC.FIELD_RIGHT);
        	//btnStartRightOnClick(v);
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
		this.tmData.setSavedDataState(true, "setOrResetStartingLoc");
	}

	/*
	private void btnStartRightOnClick(View v) {
		ToggleButton tb = (ToggleButton)v;
		STARTING_LOC sl = STARTING_LOC.FIELD_RIGHT;
		EnterTeamMatchDataActivity act = ((EnterTeamMatchDataActivity)this.getActivity());
		
		if(act.helpActive) {
			String message = this.helpHash.get(sl);
			act.helpActive = false;
			act.btnHelp.setTextColor(Color.BLUE);
			Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
		} else {
			this.resetToggleButtonsExcept(sl);
			this.setOrResetStartingLoc(sl, tb.isChecked());
		}
	}


	private void btnStartRightCenterOnClick(View v) {
		ToggleButton tb = (ToggleButton)v;
		STARTING_LOC sl = STARTING_LOC.FIELD_RIGHT_CENTER;
		EnterTeamMatchDataActivity act = ((EnterTeamMatchDataActivity)this.getActivity());
		
		if(act.helpActive) {
			String message = this.helpHash.get(sl);
			act.helpActive = false;
			act.btnHelp.setTextColor(Color.BLUE);
			Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
		} else {
			this.resetToggleButtonsExcept(sl);
			this.setOrResetStartingLoc(sl, tb.isChecked());
		}
	}

	private void btnStartCenterOnClick(View v) {
		ToggleButton tb = (ToggleButton)v;
		STARTING_LOC sl = STARTING_LOC.FIELD_CENTER;
		EnterTeamMatchDataActivity act = ((EnterTeamMatchDataActivity)this.getActivity());
		
		if(act.helpActive) {
			String message = this.helpHash.get(sl);
			act.helpActive = false;
			act.btnHelp.setTextColor(Color.BLUE);
			Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
		} else {
			this.resetToggleButtonsExcept(sl);
			this.setOrResetStartingLoc(sl, tb.isChecked());
		}
	}

	private void btnStartLeftCenterOnClick(View v) {
		ToggleButton tb = (ToggleButton)v;
		STARTING_LOC sl = STARTING_LOC.FIELD_LEFT_CENTER;
		EnterTeamMatchDataActivity act = ((EnterTeamMatchDataActivity)this.getActivity());
		
		if(act.helpActive) {
			String message = this.helpHash.get(sl);
			act.helpActive = false;
			act.btnHelp.setTextColor(Color.BLUE);
			Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
		} else {
			this.resetToggleButtonsExcept(sl);
			this.setOrResetStartingLoc(sl, tb.isChecked());
		}
	}

	private void btnStartLeftOnClick(View v) {
		ToggleButton tb = (ToggleButton)v;
		STARTING_LOC sl = STARTING_LOC.FIELD_LEFT;
		EnterTeamMatchDataActivity act = ((EnterTeamMatchDataActivity)this.getActivity());
		
		if(act.helpActive) {
			String message = this.helpHash.get(sl);
			act.helpActive = false;
			act.btnHelp.setTextColor(Color.BLUE);
			Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
		} else {
			this.resetToggleButtonsExcept(sl);
			this.setOrResetStartingLoc(sl, tb.isChecked());
		}
	}

	private void btnStartGoalOnClick(View v) {
		ToggleButton tb = (ToggleButton)v;
		STARTING_LOC sl = STARTING_LOC.FIELD_GOAL;
		EnterTeamMatchDataActivity act = ((EnterTeamMatchDataActivity)this.getActivity());
		
		if(act.helpActive) {
			String message = this.helpHash.get(sl);
			act.helpActive = false;
			act.btnHelp.setTextColor(Color.BLUE);
			Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
		} else {
			this.resetToggleButtonsExcept(sl);
			this.setOrResetStartingLoc(sl, tb.isChecked());
		}
	}
	*/
	
	private void positionButtonClick(STARTING_LOC sl) {
		ToggleButton tb = buttonHash.get(sl);
		EnterTeamMatchDataActivity act = ((EnterTeamMatchDataActivity)this.getActivity());
		
		if(act.helpActive) {
			tb.setChecked(!tb.isChecked());
			act.disableHelp();
			String message = this.helpHash.get(sl);
			Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
		} else {
			this.resetToggleButtonsExcept(sl);
			this.setOrResetStartingLoc(sl, tb.isChecked());
		}
	}
}
