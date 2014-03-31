package com.wilsonvillerobotics.firstteamscouter;

import java.util.ArrayList;
import java.util.Hashtable;

import com.wilsonvillerobotics.firstteamscouter.TeamMatchData.BALL_CONTROL;
import com.wilsonvillerobotics.firstteamscouter.TeamMatchData.ROBOT_ROLE;
import com.wilsonvillerobotics.firstteamscouter.utilities.FTSUtilities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;

public class TeamMatchNotesFragment extends Fragment implements OnClickListener{
	public static String myTitle;
	private Integer teamMatchID;
	
	private ToggleButton tbtnBrokeDown;
	private ToggleButton tbtnNoMove;
	private ToggleButton tbtnLostConnection;
	
	private Integer robotRoleCheckBoxes[] = {
		R.id.chkRobotRolePasser,
    	R.id.chkRobotRoleCatcher,
    	R.id.chkRobotRoleShooter,
    	R.id.chkRobotRoleDefense,
    	R.id.chkRobotRoleGoalie
	};
	
	Hashtable<Integer, CheckBox> htRobotRoles;

	private Integer ballControlCheckBoxes[] = {
		R.id.chkBallControlGroundPickup,
		R.id.chkBallControlHumanLoad,
		R.id.chkBallControlHiToLo,
		R.id.chkBallControlLoToHi,
		R.id.chkBallControlHiToHi,
		R.id.chkBallControlLoToLo
	};
	
	private Hashtable<Integer, CheckBox> htBallControl;

	private EditText txtTMNotes;
	
	protected TeamMatchData tmData;
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	myTitle = "Notes";
 
    	this.teamMatchID = getArguments() != null ? getArguments().getInt("tmID") : -1;

    	View rootView = inflater.inflate(R.layout.fragment_team_match_notes, container, false);
        
    	this.tbtnBrokeDown = (ToggleButton) rootView.findViewById(R.id.tbtnBrokeDown);
    	this.tbtnNoMove = (ToggleButton) rootView.findViewById(R.id.tbtnNoMove);
    	this.tbtnLostConnection = (ToggleButton) rootView.findViewById(R.id.tbtnLostConnection);
    	
    	this.tbtnBrokeDown.setOnClickListener(this);
    	this.tbtnNoMove.setOnClickListener(this);
    	this.tbtnLostConnection.setOnClickListener(this);
    	
    	this.htRobotRoles = new Hashtable<Integer, CheckBox>();
    	
    	for(int rrID : this.robotRoleCheckBoxes) {
    		this.htRobotRoles.put(rrID, (CheckBox) rootView.findViewById(rrID));
    		this.htRobotRoles.get(rrID).setOnClickListener(this);
    	}

    	this.htBallControl = new Hashtable<Integer, CheckBox>();
    	
    	for(int bcID : this.ballControlCheckBoxes) {
    		this.htBallControl.put(bcID, (CheckBox) rootView.findViewById(bcID));
    		this.htBallControl.get(bcID).setOnClickListener(this);
    	}

    	this.txtTMNotes = (EditText) rootView.findViewById(R.id.txtTMNotes);
    	this.txtTMNotes.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tmData.setNoteText(s);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

			@Override
			public void afterTextChanged(Editable s) {}
        });
    	
    	this.updateToggleButtonStates();
    	this.updateNotes();
    	this.updateCheckboxes();
    	
        return rootView;
    }
    
    private void updateToggleButtonStates() {
    	if(this.tmData != null) {
	    	this.tbtnBrokeDown.setChecked(this.tmData.brokeDown);
	    	this.tbtnNoMove.setChecked(this.tmData.noMove);
	    	this.tbtnLostConnection.setChecked(this.tmData.lostConnection);
    	}
    }
    
    private void updateNotes() {
    	if(this.tmData != null) {
    		this.txtTMNotes.setText(this.tmData.getNoteText());
    	}
    }
    
    private void updateCheckboxes() {
   		this.htRobotRoles.get(R.id.chkRobotRolePasser).setChecked(this.tmData.isRobotRoleChecked(ROBOT_ROLE.PASSER));
   		this.htRobotRoles.get(R.id.chkRobotRoleCatcher).setChecked(this.tmData.isRobotRoleChecked(ROBOT_ROLE.CATCHER));
   		this.htRobotRoles.get(R.id.chkRobotRoleShooter).setChecked(this.tmData.isRobotRoleChecked(ROBOT_ROLE.SHOOTER));
   		this.htRobotRoles.get(R.id.chkRobotRoleDefense).setChecked(this.tmData.isRobotRoleChecked(ROBOT_ROLE.DEFENDER));
   		this.htRobotRoles.get(R.id.chkRobotRoleGoalie).setChecked(this.tmData.isRobotRoleChecked(ROBOT_ROLE.GOALIE));
   		
   		this.htBallControl.get(R.id.chkBallControlGroundPickup).setChecked(this.tmData.isBallControlChecked(BALL_CONTROL.GROUND_PICKUP));
   		this.htBallControl.get(R.id.chkBallControlHumanLoad).setChecked(this.tmData.isBallControlChecked(BALL_CONTROL.HUMAN_LOAD));
   		this.htBallControl.get(R.id.chkBallControlHiToLo).setChecked(this.tmData.isBallControlChecked(BALL_CONTROL.HI_TO_LO));
   		this.htBallControl.get(R.id.chkBallControlLoToHi).setChecked(this.tmData.isBallControlChecked(BALL_CONTROL.LO_TO_HI));
   		this.htBallControl.get(R.id.chkBallControlHiToHi).setChecked(this.tmData.isBallControlChecked(BALL_CONTROL.HI_TO_HI));
   		this.htBallControl.get(R.id.chkBallControlLoToLo).setChecked(this.tmData.isBallControlChecked(BALL_CONTROL.LO_TO_LO));
    }

    static TeamMatchNotesFragment newInstance(Long teamMatchID, Integer teamNumber, Integer matchNumber) {
    	TeamMatchNotesFragment f = new TeamMatchNotesFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putLong("tmID", teamMatchID);
        args.putInt("tNum", teamNumber);
        args.putInt("mNum", matchNumber);
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
        case R.id.chkRobotRolePasser:
        	chkRobotRolePasserOnClick(v);
        	break;
        case R.id.chkRobotRoleCatcher:
        	chkRobotRoleCatcherOnClick(v);
        	break;
        case R.id.chkRobotRoleShooter:
        	chkRobotRoleShooterOnClickl(v);
        	break;
        case R.id.chkRobotRoleDefense:
        	chkRobotRoleDefenseOnClick(v);
        	break;
        case R.id.chkRobotRoleGoalie:
        	chkRobotRoleGoalieOnClick(v);
        	break;
        case R.id.chkBallControlGroundPickup:
        	chkBallControlGroundPickupOnClick(v);
        	break;
        case R.id.chkBallControlHumanLoad:
        	chkBallControlHumanLoadOnClick(v);
        	break;
        case R.id.chkBallControlHiToLo:
        	chkBallControlHiToLoOnClick(v);
        	break;
        case R.id.chkBallControlLoToHi:
        	chkBallControlLoToHiOnClick(v);
        	break;
        case R.id.chkBallControlHiToHi:
        	chkBallControlHiToHiOnClick(v);
        	break;
        case R.id.chkBallControlLoToLo:
        	chkBallControlLoToLoOnClick(v);
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

	private void chkRobotRolePasserOnClick(View v) {
		if( ((CheckBox)v).isChecked() ) {
			this.tmData.addRobotRole(ROBOT_ROLE.PASSER);
		} else {
			this.tmData.removeRobotRole(ROBOT_ROLE.PASSER);
		}
	}

	private void chkRobotRoleCatcherOnClick(View v) {
		if( ((CheckBox)v).isChecked() ) {
			this.tmData.addRobotRole(ROBOT_ROLE.CATCHER);
		} else {
			this.tmData.removeRobotRole(ROBOT_ROLE.CATCHER);
		}
	}

	private void chkRobotRoleShooterOnClickl(View v) {
		if( ((CheckBox)v).isChecked() ) {
			this.tmData.addRobotRole(ROBOT_ROLE.SHOOTER);
		} else {
			this.tmData.removeRobotRole(ROBOT_ROLE.SHOOTER);
		}
	}

	private void chkRobotRoleDefenseOnClick(View v) {
		if( ((CheckBox)v).isChecked() ) {
			this.tmData.addRobotRole(ROBOT_ROLE.DEFENDER);
		} else {
			this.tmData.removeRobotRole(ROBOT_ROLE.DEFENDER);
		}
	}

	private void chkRobotRoleGoalieOnClick(View v) {
		if( ((CheckBox)v).isChecked() ) {
			this.tmData.addRobotRole(ROBOT_ROLE.GOALIE);
		} else {
			this.tmData.removeRobotRole(ROBOT_ROLE.GOALIE);
		}
	}

	private void chkBallControlGroundPickupOnClick(View v) {
		if( ((CheckBox)v).isChecked() ) {
			this.tmData.addBallControl(BALL_CONTROL.GROUND_PICKUP);
		} else {
			this.tmData.removeBallControl(BALL_CONTROL.GROUND_PICKUP);
		}
	}

	private void chkBallControlHumanLoadOnClick(View v) {
		if( ((CheckBox)v).isChecked() ) {
			this.tmData.addBallControl(BALL_CONTROL.HUMAN_LOAD);
		} else {
			this.tmData.removeBallControl(BALL_CONTROL.HUMAN_LOAD);
		}
	}

	private void chkBallControlHiToLoOnClick(View v) {
		if( ((CheckBox)v).isChecked() ) {
			this.tmData.addBallControl(BALL_CONTROL.HI_TO_LO);
		} else {
			this.tmData.removeBallControl(BALL_CONTROL.HI_TO_LO);
		}
	}

	private void chkBallControlLoToHiOnClick(View v) {
		if( ((CheckBox)v).isChecked() ) {
			this.tmData.addBallControl(BALL_CONTROL.LO_TO_HI);
		} else {
			this.tmData.removeBallControl(BALL_CONTROL.LO_TO_HI);
		}
	}

	private void chkBallControlHiToHiOnClick(View v) {
		if( ((CheckBox)v).isChecked() ) {
			this.tmData.addBallControl(BALL_CONTROL.HI_TO_HI);
		} else {
			this.tmData.removeBallControl(BALL_CONTROL.HI_TO_HI);
		}
	}

	private void chkBallControlLoToLoOnClick(View v) {
		if( ((CheckBox)v).isChecked() ) {
			this.tmData.addBallControl(BALL_CONTROL.LO_TO_LO);
		} else {
			this.tmData.removeBallControl(BALL_CONTROL.LO_TO_LO);
		}
	}
}