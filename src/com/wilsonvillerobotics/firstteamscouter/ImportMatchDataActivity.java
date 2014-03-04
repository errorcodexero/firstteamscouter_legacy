package com.wilsonvillerobotics.firstteamscouter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import com.wilsonvillerobotics.firstteamscouter.dbAdapters.MatchDataDBAdapter;
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
import android.widget.TextView;

public class ImportMatchDataActivity extends Activity {

	private MatchDataDBAdapter mDataDBAdapter;
	private TeamMatchDBAdapter tmDBAdapter;
	private Button btnOK;
	private TextView txtStatus;
	
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
		} catch(SQLException e) {
			e.printStackTrace();
			mDataDBAdapter = null;
			tmDBAdapter = null;
		}
		
		txtStatus = (TextView) findViewById(R.id.txtStatus);
	    txtStatus.setText("Press the 'Import' button to import matches from 'match_list_data.csv'\n");
	    
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
					        String line = inputReader.readLine();
					        inputReader.close();
					        FTSUtilities.printToConsole("ImportMatchDataActivity::btnOK.onClick :" + line + "\n"); 
					        txtStatus.setText(txtStatus.getText() + "\n\n" + line);
				        } else {
				        	txtStatus.setText("ERROR: could not find file:\n" + file.toString());
				        }
				    }
				} catch (Exception e) {
				    e.printStackTrace();
				}
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
        if(mDataDBAdapter == null) {
        	mDataDBAdapter = new MatchDataDBAdapter(this.getBaseContext());
        }
        mDataDBAdapter.open();
        
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
        FTSUtilities.printToConsole("ImportMatchDataActivity::onStop : CLOSING DB\n");
        mDataDBAdapter.close();
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
}
