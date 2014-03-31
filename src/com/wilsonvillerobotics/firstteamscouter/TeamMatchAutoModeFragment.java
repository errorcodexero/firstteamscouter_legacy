package com.wilsonvillerobotics.firstteamscouter;

import java.util.Hashtable;

import com.wilsonvillerobotics.firstteamscouter.utilities.FTSUtilities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.support.v4.app.Fragment;

public class TeamMatchAutoModeFragment extends Fragment implements OnClickListener{
	public static String myTitle;
	private Integer teamMatchID;
	private TeamMatchData tmData;
	
	private Integer buttonIDs[] = {
			R.id.btnAutoHiScoreHot,
			R.id.btnAutoLoScoreHot,
			R.id.btnAutoHiScoreCold,
			R.id.btnAutoLoScoreCold,
			R.id.btnAutoHiMiss,
			R.id.btnAutoLoMiss,
			R.id.btnAutoMove,
			R.id.btnAutoCollect,
			R.id.btnAutoDefend
	};
	
	private Hashtable<Integer, Button> buttonHash;
	
	protected int autoScore;
	protected TextView txtAutoScore;
	protected Switch switchUndo;
	
	protected Boolean undo;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	myTitle = "Auto Mode";
    	
    	this.teamMatchID = getArguments() != null ? getArguments().getInt("tmID") : -1;
    	
        View rootView = inflater.inflate(R.layout.fragment_team_match_automode, container, false);
        
        buttonHash = new Hashtable<Integer, Button>(); 

        txtAutoScore = (TextView) rootView.findViewById(R.id.txtAutoScore);

        switchUndo = (Switch) rootView.findViewById(R.id.switchUndo);
        switchUndo.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				undo = isChecked;
				FTSUtilities.setButtonStyles(buttonHash, undo);
			}
        	
        });
        
        this.updateAutoScore();
        
        this.undo = false;
		
        for(int ID : buttonIDs) {
	        buttonHash.put(ID, (Button) rootView.findViewById(ID));
	        buttonHash.get(ID).setOnClickListener(this);
        }

        return rootView;
    }
    
    static TeamMatchAutoModeFragment newInstance(Long teamMatchID) {
    	TeamMatchAutoModeFragment f = new TeamMatchAutoModeFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putLong("tmID", teamMatchID);
        f.setArguments(args);

        return f;
    }
    
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.btnAutoHiScoreHot:
        	btnAutoHiScoreHotOnClick(v);
        	break;
        case R.id.btnAutoHiScoreCold:
        	btnAutoHiScoreColdOnClick(v);
        	break;
        case R.id.btnAutoLoScoreHot:
        	btnAutoLoScoreHotOnClick(v);
        	break;
        case R.id.btnAutoLoScoreCold:
        	btnAutoLoScoreColdOnClick(v);
    		break;
        case R.id.btnAutoHiMiss:
        	btnAutoHiMissOnClick(v);
        	break;
        case R.id.btnAutoLoMiss:
        	btnAutoLoMissOnClick(v);
        	break;
        case R.id.btnAutoCollect:
        	btnAutoCollectOnClick(v);
        	break;
        case R.id.btnAutoMove:
        	btnAutoMoveOnClick(v);
        	break;
        case R.id.btnAutoDefend:
        	btnAutoDefendOnClick(v);
        	break;
        }
    }
    
    public void setTeamMatchData(TeamMatchData tmD) {
    	this.tmData = tmD;
    }
    
	private void updateAutoScore() {
		if(this.txtAutoScore != null && this.tmData != null) {
			this.txtAutoScore.setText(String.valueOf(this.tmData.getAutoScore()));
		}
	}

	public void btnAutoHiScoreHotOnClick(View v) {
		if(this.undo) {
			this.tmData.lowerAutoHiScore();
			this.tmData.lowerHiHotBonus();
		} else {
			this.tmData.addAutoHiScore();
			this.tmData.addHiHotBonus();
		}
		updateAutoScore();
	}
	
	public void btnAutoHiScoreColdOnClick(View v) {
		if(this.undo) {
			this.tmData.lowerAutoHiScore();
		} else {
			this.tmData.addAutoHiScore();
		}
		updateAutoScore();
	}
	
	public void btnAutoLoScoreHotOnClick(View v) {
		if(this.undo) {
			this.tmData.lowerAutoLoScore();
			this.tmData.lowerLoHotBonus();
		} else {
			this.tmData.addAutoLoScore();
			this.tmData.addLoHotBonus();
		}
		updateAutoScore();
	}
	
	public void btnAutoLoScoreColdOnClick(View v) {
		if(this.undo) {
			this.tmData.lowerAutoLoScore();
		} else {
			this.tmData.addAutoLoScore();
		}
		updateAutoScore();
	}
	
	public void btnAutoHiMissOnClick(View v) {
		if(this.undo) {
			this.tmData.lowerAutoHiMiss();
		} else {
			this.tmData.addAutoHiMiss();
		}
	}
	
	public void btnAutoLoMissOnClick(View v) {
		if(this.undo) {
			this.tmData.lowerAutoLoMiss();
		} else {
			this.tmData.addAutoLoMiss();
		}
	}
	
	protected void btnAutoDefendOnClick(View v) {
		if(this.undo) {
			this.tmData.lowerAutoDefend();	
		} else {
			this.tmData.addAutoDefend();
		}
	}

	protected void btnAutoMoveOnClick(View v) {
		if(this.undo) {
			this.tmData.didNotMoveInAuto();
		} else {
			this.tmData.movedInAuto();
		}
	}

	protected void btnAutoCollectOnClick(View v) {
		if(this.undo) {
			this.tmData.lowerAutoCollect();
		} else {
			this.tmData.addAutoCollect();
		}
	}
	
	private void setButtonStyles() {
		int color = Color.BLUE;
		//int style = R.style.btnStyleOrange;
		if(this.undo) {
			color = Color.RED;
			//style = R.style.btnStyleShakespeare;
		}
		
		for(Integer bID : buttonHash.keySet()) {
			//this.buttonHash.get(bID).setBackgroundColor(color);
			this.buttonHash.get(bID).setTextColor(color);
			//this.buttonHash.get(bID).setBackgroundResource(style);
		}
	}
}
