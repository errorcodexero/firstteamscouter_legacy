package com.wilsonvillerobotics.firstteamscouter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import com.wilsonvillerobotics.firstteamscouter.dbAdapters.MatchDataDBAdapter;
import com.wilsonvillerobotics.firstteamscouter.dbAdapters.TeamDataDBAdapter;
import com.wilsonvillerobotics.firstteamscouter.dbAdapters.TeamMatchDBAdapter;
import com.wilsonvillerobotics.firstteamscouter.utilities.FTSUtilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ImportMatchDataActivity extends Activity {

	protected static final int TIMER_RUNTIME = 10000; // in ms --> 10s
	
	private MatchDataDBAdapter mDataDBAdapter;
	private TeamMatchDBAdapter tmDBAdapter;
	private TeamDataDBAdapter tDataDBAdapter;
	private Button btnOK;
	private TextView txtStatus;
	protected ProgressBar mProgressBar;
	protected boolean mbActive;
	
	//getExternalStorageState()

	@SuppressLint("WorldReadableFiles")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import_match_data);
		
		try {
			FTSUtilities.printToConsole("ImportMatchDataActivity::onCreate : OPENING DB\n");
			mDataDBAdapter = new MatchDataDBAdapter(this.getBaseContext()).open();
			tmDBAdapter = new TeamMatchDBAdapter(this.getBaseContext()).open();
			tDataDBAdapter = new TeamDataDBAdapter(this.getBaseContext()).open();
		} catch(SQLException e) {
			e.printStackTrace();
			mDataDBAdapter = null;
			tmDBAdapter = null;
			tDataDBAdapter = null;
		}
		
		txtStatus = (TextView) findViewById(R.id.txtStatus);
	    txtStatus.setText("Press the 'Import' button to import matches from 'match_list_data.csv'\n");
	    mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
	    
		btnOK = (Button) findViewById(R.id.btnImportMatchData);
		btnOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
				    String storageState = Environment.getExternalStorageState();
				    if (storageState.equals(Environment.MEDIA_MOUNTED)) {
				    	FTSUtilities.printToConsole("ImportMatchDataActivity::btnOK.onClick : getting file\n");
				        File file = new File(getExternalFilesDir(null),
				                "match_list_data.csv");
				        FTSUtilities.printToConsole("ImportMatchDataActivity::btnOK.onClick : file " + ((file == null) ? "IS NULL" : "IS VALID") + "\n");
				        
				        if(file.exists() && file.isFile()) {
				        	txtStatus.setText("File Found, Import Commencing\n");
					        BufferedReader inputReader = new BufferedReader(
					                new InputStreamReader(new FileInputStream(file)));
					        String line = "";
					        int lineCount = 0;
					        int matchCount = 0;
					        int teamCount = 0;
					        inputReader.mark((int)file.length());
//					        while((line = inputReader.readLine()) != null) {
//					        	lineCount++;
//					        }
					        line = inputReader.readLine();
					        if(!line.startsWith("Time")) {
					        	FTSUtilities.printToConsole("ImportMatchDataActivity::btnOK.onClick : NO Heasder Row Detected");
					        	inputReader.reset();
					        } else {
					        	FTSUtilities.printToConsole("ImportMatchDataActivity::btnOK.onClick : Heasder Row Detected");
					        }
					        
					        while((line = inputReader.readLine()) != null) {
					        	if(line == null) break;
					        	
					        	lineCount += 1;
					        	String lineArray[] = line.split(",");
					        	
					        	if(lineArray.length > 7) {
					        		//FTSUtilities.printToConsole("ImportMatchDataActivity::btnOK.onClick : " + lineArray[0] + ":" + lineArray[1] + ":" + lineArray[2] + ":" + lineArray[3] + ":" + lineArray[4] + ":" + lineArray[5] + ":" + lineArray[6] + ":" + lineArray[7]);
					        		long matchID = mDataDBAdapter.createMatchData(lineArray[0], Integer.parseInt(lineArray[1]), lineArray[2], lineArray[3], lineArray[4], lineArray[5], lineArray[6], lineArray[7]);
					        		if(matchID >= 0) {
					        			matchCount += 1;
					        		}
					        		long teamMatchID = -1;
					        		long teamID = -1;
					        		for(int i = 2; i < 8; i++) {
					        			teamID = tDataDBAdapter.createTeamDataEntry(lineArray[i]);
					        			teamMatchID = tmDBAdapter.createTeamMatch(lineArray[i], matchID);
					        			if(teamMatchID >= 0) {
					        				teamCount += 1;
					        			}
					        		}
					        	} else {
					        		FTSUtilities.printToConsole("ImportMatchDataActivity::btnOK.onClick : line not in proper format: " + line);
					        	}
					        }
					        inputReader.close();

					        txtStatus.setText(txtStatus.getText() + "\nLines Parsed: " + lineCount + "\nMatches Created: " + matchCount + "\nTeamMatch Records Created: " + teamCount);
				        } else {
				        	txtStatus.setText("ERROR: could not find file:\n" + file.toString());
				        }
				    }
				} catch (Exception e) {
					FTSUtilities.printToConsole("ImportMatchDataActivity::btnOK.onClick : ERROR");
				    e.printStackTrace();
				}
			}
		});
		
//		final Thread timerThread = new Thread() {
//	          @Override
//	          public void run() {
//	              mbActive = true;
//	              try {
//	                  int waited = 0;
//	                  while(mbActive && (waited < TIMER_RUNTIME)) {
//	                      sleep(200);
//	                      if(mbActive) {
//	                          waited += 200;
//	                          updateProgress(waited);
//	                      }
//	                  }
//	          } catch(InterruptedException e) {
//	              // do nothing
//	          } finally {
//	              onContinue();
//	          }
//	        }
//	     };
//	     timerThread.start();
	}

	public void updateProgress(final int timePassed) {
       if(null != mProgressBar) {
           // Ignore rounding error here
           final int progress = mProgressBar.getMax() * timePassed / TIMER_RUNTIME;
           mProgressBar.setProgress(progress);
       }
   }

	public void onContinue() {
	     // perform any final actions here
   }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(mDataDBAdapter == null) {
        	mDataDBAdapter = new MatchDataDBAdapter(this.getBaseContext());
        }
        mDataDBAdapter.open();
        
        if(tmDBAdapter == null) {
        	tmDBAdapter = new TeamMatchDBAdapter(this.getBaseContext());
        }
        tmDBAdapter.open();

        if(tDataDBAdapter == null) {
        	tDataDBAdapter = new TeamDataDBAdapter(this.getBaseContext());
        }
        tDataDBAdapter.open();
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
        FTSUtilities.printToConsole("ImportMatchDataActivity::onStop : CLOSING DB\n");
        mDataDBAdapter.close();
        tmDBAdapter.close();
        tDataDBAdapter.close();
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
}
