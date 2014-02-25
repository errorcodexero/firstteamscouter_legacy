package com.wilsonvillerobotics.firstteamscouter;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

public class TeamMatchAutoModeFragment extends Fragment implements OnClickListener{
	public static String myTitle;
	private Integer teamMatchID;
	private TeamMatchData tmData;
	
	protected int autoScore;
	protected TextView txtAutoScore;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	myTitle = "Auto Mode";
 
    	this.teamMatchID = getArguments() != null ? getArguments().getInt("tmID") : -1;
    	
        View rootView = inflater.inflate(R.layout.fragment_team_match_automode, container, false);

        txtAutoScore = (TextView) rootView.findViewById(R.id.txtAutoScore);
        
        this.updateAutoScore();
		
		Button btnAutoHighScoreHot = (Button) rootView.findViewById(R.id.btnAutoHiScoreHot);
		btnAutoHighScoreHot.setOnClickListener(this);

		Button btnAutoLowScoreHot = (Button) rootView.findViewById(R.id.btnAutoLoScoreHot);
		btnAutoLowScoreHot.setOnClickListener(this);

		Button btnAutoHighScoreCold = (Button) rootView.findViewById(R.id.btnAutoHiScoreCold);
		btnAutoHighScoreCold.setOnClickListener(this);

		Button btnAutoLowScoreCold = (Button) rootView.findViewById(R.id.btnAutoLoScoreCold);
		btnAutoLowScoreCold.setOnClickListener(this);

		Button btnAutoHiMiss = (Button) rootView.findViewById(R.id.btnAutoHiMiss);
		btnAutoHiMiss.setOnClickListener(this);

		Button btnAutoLoMiss = (Button) rootView.findViewById(R.id.btnAutoLoMiss);
		btnAutoLoMiss.setOnClickListener(this);

		Button btnAutoCollect = (Button) rootView.findViewById(R.id.btnAutoCollect);
		btnAutoCollect.setOnClickListener(this);

		Button btnAutoMove = (Button) rootView.findViewById(R.id.btnAutoMove);
		btnAutoMove.setOnClickListener(this);

		Button btnAutoDefend = (Button) rootView.findViewById(R.id.btnAutoDefend);
		btnAutoDefend.setOnClickListener(this);
		
        return rootView;
    }
    
    static TeamMatchAutoModeFragment newInstance(Integer tmID) {
    	TeamMatchAutoModeFragment f = new TeamMatchAutoModeFragment();

        // Supply num input as an argument.
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
    	
//    	if(this.tmData.hasSavedData()) {
//    		this.updateAutoScore();
//    	}
    }
    
	private void updateAutoScore() {
		if(this.txtAutoScore != null && this.tmData != null) {
			this.txtAutoScore.setText(String.valueOf(this.tmData.getAutoScore()));
		}
	}

	public void btnSaveOnClick(View v) {
		
	}
	
	public void btnAutoHiScoreHotOnClick(View v) {
		this.tmData.addAutoHiScore();
		this.tmData.addHiHotBonus();
		updateAutoScore();
	}
	
	public void btnAutoHiScoreColdOnClick(View v) {
		this.tmData.addAutoHiScore();
		updateAutoScore();
	}
	
	public void btnAutoLoScoreHotOnClick(View v) {
		this.tmData.addAutoLoScore();
		this.tmData.addLoHotBonus();
		updateAutoScore();
	}
	
	public void btnAutoLoScoreColdOnClick(View v) {
		this.tmData.addAutoLoScore();
		updateAutoScore();
	}
	
	public void btnAutoHiMissOnClick(View v) {
		
	}
	
	public void btnAutoLoMissOnClick(View v) {
		
	}
	
	protected void btnAutoDefendOnClick(View v) {
		this.tmData.addAutoDefend();
	}

	protected void btnAutoMoveOnClick(View v) {
		this.tmData.movedInAuto();
	}

	protected void btnAutoCollectOnClick(View v) {
		this.tmData.addAutoCollect();
	}
}
