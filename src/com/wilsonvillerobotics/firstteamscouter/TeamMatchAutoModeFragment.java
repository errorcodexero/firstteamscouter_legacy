package com.wilsonvillerobotics.firstteamscouter;

import java.util.Hashtable;

import com.wilsonvillerobotics.firstteamscouter.TeamMatchData.STARTING_LOC;

import android.app.ActionBar;
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
import android.support.v4.view.ViewPager;

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
				setButtonBackgrounds();
			}
        	
        });
        
        this.updateAutoScore();
        
        this.undo = false;
		
        for(int ID : buttonIDs) {
	        buttonHash.put(ID, (Button) rootView.findViewById(ID));
	        buttonHash.get(ID).setOnClickListener(this);
        }
        updateText(R.id.btnAutoHiScoreHot, R.string.text_button_hot_score, 
        		tmData.autoHiHot);
		updateText(R.id.btnAutoHiScoreCold, R.string.text_button_cold_score, 
				tmData.autoHiScore - tmData.autoHiHot);
		updateText(R.id.btnAutoLoScoreHot, R.string.text_button_hot_score, 
				tmData.autoLoHot);
		updateText(R.id.btnAutoLoScoreCold, R.string.text_button_cold_score,
				tmData.autoLoScore - tmData.autoLoHot);
		updateText(R.id.btnAutoHiMiss, R.string.text_button_miss_score, 
				tmData.autoHiMiss);
		updateText(R.id.btnAutoLoMiss, R.string.text_button_miss_score, 
				tmData.autoLoMiss);
		updateText(R.id.btnAutoDefend, R.string.text_button_auto_defend,
				tmData.autoDefend);
		updateText(R.id.btnAutoCollect, R.string.text_button_auto_collect, 
				tmData.autoCollect);
		buttonHash.get(R.id.btnAutoMove).setText(getString(R.string.text_button_auto_move) + 
				"\n" + tmData.autoMove); //true or false..
        return rootView;
    }
    
    static TeamMatchAutoModeFragment newInstance(Integer tmID) {
    	TeamMatchAutoModeFragment f = new TeamMatchAutoModeFragment();

        // Supply num input as an argument
        Bundle args = new Bundle();
        args.putInt("tmID", tmID);
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
		updateText(R.id.btnAutoHiScoreHot, R.string.text_button_hot_score, tmData.autoHiHot);
	}
	
	public void btnAutoHiScoreColdOnClick(View v) {
		if(this.undo) {
			this.tmData.lowerAutoHiScore();
		} else {
			this.tmData.addAutoHiScore();
		}
		updateAutoScore();
		updateText(R.id.btnAutoHiScoreCold, R.string.text_button_cold_score, tmData.autoHiScore - tmData.autoHiHot);
		//buttonHash.get(buttonIDs[2]).setText("Cold\n" + (tmData.autoHiScore - tmData.autoHiHot));
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
		updateText(R.id.btnAutoLoScoreHot, R.string.text_button_hot_score, tmData.autoLoHot);
		//buttonHash.get(buttonIDs[1]).setText("Hot\n" + tmData.autoLoHot);
	}
	
	public void btnAutoLoScoreColdOnClick(View v) {
		if(this.undo) {
			this.tmData.lowerAutoLoScore();
		} else {
			this.tmData.addAutoLoScore();
		}
		updateAutoScore();
		updateText(R.id.btnAutoLoScoreCold, R.string.text_button_cold_score, tmData.autoLoScore - tmData.autoLoHot);
		//buttonHash.get(buttonIDs[3]).setText("Cold\n" + (tmData.autoLoScore - tmData.autoLoHot));
	}
	
	public void btnAutoHiMissOnClick(View v) {
		if(this.undo) {
			this.tmData.lowerAutoHiMiss();
		} else {
			this.tmData.addAutoHiMiss();
		}
		updateText(R.id.btnAutoHiMiss, R.string.text_button_miss_score, tmData.autoHiMiss);
		//buttonHash.get(buttonIDs[4]).setText("Miss\n" + tmData.autoHiMiss);
	}
	
	public void btnAutoLoMissOnClick(View v) {
		if(this.undo) {
			this.tmData.lowerAutoLoMiss();
		} else {
			this.tmData.addAutoLoMiss();
		}
		updateText(R.id.btnAutoLoMiss, R.string.text_button_miss_score, tmData.autoLoMiss);
		//buttonHash.get(buttonIDs[5]).setText("Miss\n" + tmData.autoLoMiss);
	}
	
	protected void btnAutoDefendOnClick(View v) {
		if(this.undo) {
			this.tmData.lowerAutoDefend();	
		} else {
			this.tmData.addAutoDefend();
		}
		updateText(R.id.btnAutoDefend, R.string.text_button_auto_defend, tmData.autoDefend);
		//buttonHash.get(buttonIDs[8]).setText("Defend\n" + tmData.autoDefend);
	}
	
	protected void btnAutoMoveOnClick(View v) {
		if(this.undo) {
			this.tmData.didNotMoveInAuto();
		} else {
			this.tmData.movedInAuto();
		}
		buttonHash.get(R.id.btnAutoMove).setText(getString(R.string.text_button_auto_move) + "\n" + tmData.autoMove);
	}

	protected void btnAutoCollectOnClick(View v) {
		if(this.undo) {
			this.tmData.lowerAutoCollect();
		} else {
			this.tmData.addAutoCollect();
		}
		updateText(R.id.btnAutoCollect, R.string.text_button_auto_collect, tmData.autoCollect);
	}
	
	private void setButtonBackgrounds() {
		int color = Color.LTGRAY;
		if(this.undo) {
			color = Color.DKGRAY;
		}
		
		for(Integer bID : buttonHash.keySet()) {
			this.buttonHash.get(bID).setBackgroundColor(color);
		}
	}
	
	public void updateText(int buttonID, int stringResource, int counter) {
		buttonHash.get(buttonID).setText(getString(stringResource) + "\n" + counter);
	}
	
}
