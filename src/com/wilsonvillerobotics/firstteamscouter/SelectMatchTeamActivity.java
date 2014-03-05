package com.wilsonvillerobotics.firstteamscouter;

import com.wilsonvillerobotics.firstteamscouter.dbAdapters.TeamMatchDBAdapter;
import com.wilsonvillerobotics.firstteamscouter.utilities.FTSUtilities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class SelectMatchTeamActivity extends Activity {

	protected TeamMatchDBAdapter tmDBAdapter;
	protected String[] teamNumberArray;
	protected String teamNumber;
	protected String matchNumber;
	protected Button btnSubmit;
	protected Intent teamMatchIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_team_match);
		teamNumber = null;
		matchNumber = null;
		
		try {
			FTSUtilities.printToConsole("SelectTeamMatchActivity::onCreate : OPENING DB\n");
			tmDBAdapter = new TeamMatchDBAdapter(this.getBaseContext()).open();
		} catch(SQLException e) {
			e.printStackTrace();
			tmDBAdapter = null;
		}
		
		btnSubmit = (Button) findViewById(R.id.btnSubmitTeamMatch);
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				btnSubmitOnClick(v);
				//finish();
			}

			private void btnSubmitOnClick(View v) {
				FTSUtilities.printToConsole("SelectTeamMatchActivity::onCreate::btnSubmitOnClick : CLOSING DB\n");
				tmDBAdapter.close();
				startActivity(teamMatchIntent);
			}
		});
		
		tmDBAdapter.populateTestData();
		populateMatchNumberSpinner();
	}

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(tmDBAdapter == null) {
        	tmDBAdapter = new TeamMatchDBAdapter(this.getBaseContext());
        }
        tmDBAdapter.open();
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        FTSUtilities.printToConsole("SelectTeamMatchActivity::onStop : CLOSING DB\n");
		tmDBAdapter.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.enter_data, menu);
		return true;
	}

	private void populateMatchNumberSpinner() {
		if(tmDBAdapter == null) return;
		
		Cursor matchNumbers = tmDBAdapter.getAllMatchNumbers();
		
		Spinner spinMatchNum = (Spinner)findViewById(R.id.spinMatchNumber);
		
		// which columns map to which layout controls
		String[] matchFromColumns = new String[] {TeamMatchDBAdapter.COLUMN_NAME_MATCH_ID};
		int[] matchToControlIDs = new int[] {android.R.id.text1};

		// use a SimpleCursorAdapter
		SimpleCursorAdapter matchCA = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, matchNumbers,
			       matchFromColumns,
			       matchToControlIDs);

		
		matchCA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinMatchNum.setAdapter(matchCA);
		
		spinMatchNum.setOnItemSelectedListener(new OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                    int arg2, long arg3) {
            	
            	Spinner spinTeamNum = (Spinner)findViewById(R.id.spinTeamNumber);
            	
            	if(spinTeamNum != null) {
            		matchNumber = "0";
            		if(arg0.getItemAtPosition(arg2) != null) {
            			//FTSUtilities.printToConsole("Index: " + arg2 + " is match number: " + arg0.getItemAtPosition(arg2));
            			Cursor c = (Cursor)arg0.getItemAtPosition(arg2);
            			matchNumber = c.getString(c.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_MATCH_ID));
            		}
            		
	            	Cursor teamNumbers = tmDBAdapter.getTeamNumbersforMatch(matchNumber);
	            	
	            	String[] teamFromColumns = new String[] {TeamMatchDBAdapter.COLUMN_NAME_TEAM_ID, TeamMatchDBAdapter.COLUMN_NAME_TEAM_MATCH_ID};
	        		int[] teamToControlIDs = new int[] {android.R.id.text1, android.R.id.text2};
	            	
	        		SimpleCursorAdapter teamCA = new SimpleCursorAdapter(arg0.getContext(), android.R.layout.simple_spinner_item, teamNumbers,
	     			       teamFromColumns,
	     			       teamToControlIDs);
	
		     		teamCA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		     		spinTeamNum.setAdapter(teamCA);
		     		
		     		//FTSUtilities.printToConsole("Printing team numbers for match " + matchNumber + "\nNumber of Teams: " + teamCA.getCount());
		     		spinTeamNum.setOnItemSelectedListener(new OnItemSelectedListener() {

		                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		                	Cursor value = (Cursor)arg0.getItemAtPosition(arg2);
		                    teamNumber = value.getString(value.getColumnIndex(TeamMatchDBAdapter.COLUMN_NAME_TEAM_ID));
		                    String tmID = value.getString(value.getColumnIndex(TeamMatchDBAdapter.COLUMN_NAME_TEAM_MATCH_ID));
		                    // assuming string and if you want to get the value on click of list item
		                    // do what you intend to do on click of listview row
		                    teamMatchIntent = new Intent(arg1.getContext(), EnterTeamMatchDataActivity.class);
		                    teamMatchIntent.putExtra("position", arg3);
		                    teamMatchIntent.putExtra(TeamMatchDBAdapter.COLUMN_NAME_TEAM_ID, teamNumber);
		                    teamMatchIntent.putExtra(TeamMatchDBAdapter.COLUMN_NAME_MATCH_ID, matchNumber);
		                    teamMatchIntent.putExtra(TeamMatchDBAdapter.COLUMN_NAME_TEAM_MATCH_ID, tmID);
		                    //startActivityForResult(teamMatchIntent, 0);
		                }

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// do nothing
						}
            		});
            	}
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // do nothing
                
            }
                      
        });
	}
}
