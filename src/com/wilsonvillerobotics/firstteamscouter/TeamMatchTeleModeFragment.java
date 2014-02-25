package com.wilsonvillerobotics.firstteamscouter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.Fragment;

public class TeamMatchTeleModeFragment extends Fragment implements OnClickListener{
	public static String myTitle;
	private Integer teamMatchID;
	private TeamMatchData tmData;
	
	protected int teleScore;
	protected TextView txtTeleScore;
	protected TextView txtTeleStat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	myTitle = "Tele Mode";

    	this.teamMatchID = getArguments() != null ? getArguments().getInt("tmID") : -1;

        View rootView = inflater.inflate(R.layout.fragment_team_match_telemode, container, false);

        this.txtTeleScore = (TextView) rootView.findViewById(R.id.txtTeleScore);
        this.txtTeleStat = (TextView) rootView.findViewById(R.id.txtTeleStat);
        
        this.updateTeleScore();
		
        Button btnTeleHighScore = (Button) rootView.findViewById(R.id.btnTeleHiScore);
		btnTeleHighScore.setOnClickListener(this);

		Button btnTeleLowScore = (Button) rootView.findViewById(R.id.btnTeleLoScore);
		btnTeleLowScore.setOnClickListener(this);

		Button btnTeleHiMiss = (Button) rootView.findViewById(R.id.btnTeleHiMiss);
		btnTeleHiMiss.setOnClickListener(this);

		Button btnTeleLoMiss = (Button) rootView.findViewById(R.id.btnTeleLoMiss);
		btnTeleLoMiss.setOnClickListener(this);

		Button btnTrussToss = (Button) rootView.findViewById(R.id.btnTrussToss);
		btnTrussToss.setOnClickListener(this);

		Button btnTrussMiss = (Button) rootView.findViewById(R.id.btnTrussMiss);
		btnTrussMiss.setOnClickListener(this);
		
		Button btnTossCaught = (Button) rootView.findViewById(R.id.btnTossCaught);
		btnTossCaught.setOnClickListener(this);

		Button btnTossMissed = (Button) rootView.findViewById(R.id.btnTossMiss);
		btnTossMissed.setOnClickListener(this);
		
		Button btnDefendRedZone = (Button) rootView.findViewById(R.id.btnDefendRedZone);
		btnDefendRedZone.setOnClickListener(this);

		Button btnDefendWhiteZone = (Button) rootView.findViewById(R.id.btnDefendWhiteZone);
		btnDefendWhiteZone.setOnClickListener(this);

		Button btnDefendBlueZone = (Button) rootView.findViewById(R.id.btnDefendBlueZone);
		btnDefendBlueZone.setOnClickListener(this);

		return rootView;
    }
    
    static TeamMatchTeleModeFragment newInstance(Integer tmID) {
    	TeamMatchTeleModeFragment f = new TeamMatchTeleModeFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("tmID", tmID);
        f.setArguments(args);

        return f;
    }
    
    public void setTeamMatchData(TeamMatchData tmD) {
    	this.tmData = tmD;
    	
//    	if(this.tmData.hasSavedData()) {
//    		this.updateTeleScore();
//    		this.updateTeleStat();
//    	}
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
        case R.id.btnTeleHiScore:
			btnTeleHighScoreOnClick(v);
			break;
		case R.id.btnTeleLoScore:
			btnTeleLowScoreOnClick(v);
			break;
		case R.id.btnTeleHiMiss:
			btnTeleHiMissOnClick(v);
			break;
		case R.id.btnTeleLoMiss:
			btnTeleLoMissOnClick(v);
			break;
		case R.id.btnTrussToss:
			btnTrussTossOnClick(v);
			break;
		case R.id.btnTrussMiss:
			btnTrussMissOnClick(v);
			break;
		case R.id.btnTossCaught:
			btnTossCaughtOnClick(v);
			break;
		case R.id.btnTossMiss:
			btnTossMissedOnClick(v);
			break;
		case R.id.btnDefendRedZone:
			btnDefendRedZoneOnClick(v);
			break;
		case R.id.btnDefendWhiteZone:
			btnDefendWhiteZoneOnClick(v);
			break;
		case R.id.btnDefendBlueZone:
			btnDefendBlueZoneOnClick(v);
		 	break;
		}
	}
	
    protected void btnDefendBlueZoneOnClick(View v) {
		// TODO Auto-generated method stub
	}

	protected void btnDefendWhiteZoneOnClick(View v) {
		// TODO Auto-generated method stub
	}

	protected void btnDefendRedZoneOnClick(View v) {
		// TODO Auto-generated method stub
	}

	protected void btnTossMissedOnClick(View v) {
		// TODO Auto-generated method stub
	}

	protected void btnTossCaughtOnClick(View v) {
		// TODO Auto-generated method stub
	}

	protected void btnTrussMissOnClick(View v) {
		// TODO Auto-generated method stub
	}

	protected void btnTrussTossOnClick(View v) {
		// TODO Auto-generated method stub
		
	}

	public void btnTeleHiMissOnClick(View v) {
		this.tmData.addTeleHiMiss();
		this.updateTeleStat();
	}
	
	public void btnTeleLoMissOnClick(View v) {
		this.tmData.addTeleLoMiss();
		this.updateTeleStat();
	}
	
	protected void btnTeleLowScoreOnClick(View v) {
		this.tmData.addTeleLoScore();
		this.updateTeleScore();
	}

	protected void btnTeleHighScoreOnClick(View v) {
		this.tmData.addTeleHiScore();
		this.updateTeleScore();
	}

	private void updateTeleScore() {
		if(this.txtTeleScore != null && this.tmData != null) {
			this.txtTeleScore.setText(String.valueOf(this.tmData.getTeleScore()));
			this.updateTeleStat();
		}
	}

	protected void updateTeleStat() {
		if(this.txtTeleStat != null && this.tmData != null) {
			this.txtTeleStat.setText(String.format("%.2f%%", this.tmData.getTeleShotPercentage()));
		}
	}
}