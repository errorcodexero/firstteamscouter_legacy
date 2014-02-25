package com.wilsonvillerobotics.firstteamscouter;

import com.wilsonvillerobotics.firstteamscouter.dbAdapters.DBAdapter;
import com.wilsonvillerobotics.firstteamscouter.utilities.FTSUtilities;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	private Button EnterDataButton;
	private Button ViewTeamDataButton;
	private Button ViewMatchDataButton;
	private Button SelectMatchTeamButton;
	private DBAdapter mDBAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FTSUtilities.printToConsole("Creating MainActivity");
        
        this.mDBAdapter = new DBAdapter(this).open();
        
        EnterDataButton = (Button) findViewById(R.id.btnEnterData);
        ViewTeamDataButton = (Button) findViewById(R.id.btnViewTeamData);
        ViewMatchDataButton = (Button) findViewById(R.id.btnViewMatchData);
        SelectMatchTeamButton = (Button) findViewById(R.id.btnSelectMatchTeam);
        
        EnterDataButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(), EnterTeamMatchDataActivity.class));
			}
		});
        
        ViewTeamDataButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//startActivity(new Intent(v.getContext(), ViewDataActivity.class));
				startActivity(new Intent(v.getContext(), TeamDataListActivity.class));
			}
		});
        
        ViewMatchDataButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//startActivity(new Intent(v.getContext(), ViewDataActivity.class));
				startActivity(new Intent(v.getContext(), TeamInformationActivity.class));
			}
		});

        SelectMatchTeamButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(v.getContext(), SelectMatchTeamActivity.class));
			}
		});
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mDBAdapter.open();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
    	FTSUtilities.printToConsole("Destroying MainActivity");
    	mDBAdapter.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
