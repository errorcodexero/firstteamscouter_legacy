package com.wilsonvillerobotics.firstteamscouter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;

import com.wilsonvillerobotics.firstteamscouter.dbAdapters.MatchDataDBAdapter;
import com.wilsonvillerobotics.firstteamscouter.dbAdapters.TeamDataDBAdapter;
import com.wilsonvillerobotics.firstteamscouter.dbAdapters.TeamMatchDBAdapter;
//import com.wilsonvillerobotics.firstteamscouter.BluetoothSetupActivity;
import com.wilsonvillerobotics.firstteamscouter.utilities.FTSUtilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ImportMatchDataActivity extends Activity {

	protected static final int TIMER_RUNTIME = 10000; // in ms --> 10s
	
	private MatchDataDBAdapter mDataDBAdapter;
	private TeamMatchDBAdapter tmDBAdapter;
	private TeamDataDBAdapter tDataDBAdapter;
	private Button btnOK;
	private TextView txtStatus;
	protected ProgressBar mProgressBar;
	protected boolean mbActive;
	protected String tabletID;
	
	private String exportFileName;
	private String tempFileName;
	
	private int BLUETOOTH_SEND = 32665;
	
	private Button btnConfigureBluetooth;
	private Intent bluetoothIntent;
	
	private File filePath;
	private File myExportFile;
	private File myTempFile;
	private File saveDir;
	
	//getExternalStorageState()

	@SuppressLint("WorldReadableFiles")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_import_match_data);
		
		Intent intent = getIntent();
		this.tabletID = intent.getStringExtra("tablet_id");
		this.tabletID = (this.tabletID != null) ? this.tabletID : "Unknown Tablet ID";
		
		this.exportFileName = tabletID + "_match_data_export";
		this.tempFileName = exportFileName + ".csv";
		
		this.filePath = getExternalFilesDir(null);
		this.myExportFile = new File(filePath, exportFileName);
		this.myTempFile = new File(filePath, tempFileName);
		this.saveDir = new File(filePath.getAbsolutePath() + "/sent");
		
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
	    txtStatus.setText("Press the 'Import' button to import matches from 'match_list_data.csv'\nExpected format is:\nTime : Match Type : Match Number : Red1 : Red2 : Red3 : Blue1 : Blue2 : Blue3");
	    mProgressBar = (ProgressBar)findViewById(R.id.progressBar1);
	    
		btnOK = (Button) findViewById(R.id.btnImportMatchData);
		btnOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
				    String storageState = Environment.getExternalStorageState();
				    if (storageState.equals(Environment.MEDIA_MOUNTED)) {
				    	FTSUtilities.printToConsole("ImportMatchDataActivity::btnOK.onClick : getting file\n");
				        File file = new File(getExternalFilesDir(null), "match_list_data.csv");
				        FTSUtilities.printToConsole("ImportMatchDataActivity::btnOK.onClick : file " + ((file == null) ? "IS NULL" : "IS VALID") + "\n");
				        
				        if(file.exists() && file.isFile()) {
				        	txtStatus.setText("File Found, Import Commencing\n");
					        BufferedReader inputReader = new BufferedReader(
					                new InputStreamReader(new FileInputStream(file)));
					        String line = "";
					        int lineCount = 0;
					        int matchCount = 0;
					        int teamCount = 0;
					        String lineArray[];
					        inputReader.mark((int)file.length());
					        line = inputReader.readLine();
					        lineArray = line.split(",");
					        String headerArray[] = {"Time", "Type", "#", "Red1", "Red2", "Red3", "Blue1", "Blue2", "Blue3"};
					        
					        if(lineArray[1].startsWith("Type")) {
					        	FTSUtilities.printToConsole("ImportMatchDataActivity::btnOK.onClick : Header Row Detected");
					        } else {
					        	FTSUtilities.printToConsole("ImportMatchDataActivity::btnOK.onClick : NO Heasder Row Detected");
					        	inputReader.reset();
					        }
					        
					        while((line = inputReader.readLine()) != null) {
					        	lineCount += 1;
					        	lineArray = line.split(",");
					        	
					        	if(lineArray.length > 8) {
					        		//FTSUtilities.printToConsole("ImportMatchDataActivity::btnOK.onClick : " + lineArray[0] + ":" + lineArray[1] + ":" + lineArray[2] + ":" + lineArray[3] + ":" + lineArray[4] + ":" + lineArray[5] + ":" + lineArray[6] + ":" + lineArray[7]);
					        		//Time : Type : MatchNum : Red1 : Red2 : Red3 : Blue1 : Blue2 : Blue3
					        		long matchID = mDataDBAdapter.createMatchData(lineArray[0], lineArray[1], lineArray[2], lineArray[3], lineArray[4], lineArray[5], lineArray[6], lineArray[7], lineArray[8]);
					        		if(matchID >= 0) {
					        			matchCount += 1;
					        		}
					        		long teamMatchID = -1;
					        		long teamID = -1;
					        		for(int i = 3; i < 9; i++) {
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
		
		btnConfigureBluetooth = (Button) findViewById(R.id.btnConfigureBluetooth);
		btnConfigureBluetooth.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//File filePath = getExternalFilesDir(null);
				//File myFile = new File(filePath, exportFileName);
				//File myTempFile = new File(filePath, tempFileName);
				
				if(!myTempFile.exists())
					try {
						myTempFile.createNewFile();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				if(myExportFile.exists()) {
					try {
						copy(myExportFile, myTempFile);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(saveDir.mkdir() || saveDir.isDirectory()) {
						int numFiles = (saveDir.list(new CSVFilenameFilter(".csv")).length) + 1;
						String saveFileName = exportFileName + "_" + numFiles + ".csv";
						File saveFile = new File(saveDir, saveFileName);
						myExportFile.renameTo(saveFile);
						Toast.makeText(getBaseContext(), "File Saved", Toast.LENGTH_SHORT).show();

						Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
						sharingIntent.setType("text/plain");
						sharingIntent.setComponent(new ComponentName("com.android.bluetooth", "com.android.bluetooth.opp.BluetoothOppLauncherActivity"));
						sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(saveFile));
						startActivityForResult(sharingIntent, BLUETOOTH_SEND);
						FTSUtilities.printToConsole("ImportMatchDataActivity::btnOK.onClick : Sharing Activity Started");
					}
				} else {
					String message = "File not found: " + myExportFile.getName();
					Toast.makeText(getBaseContext(), message , Toast.LENGTH_SHORT).show();
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		FTSUtilities.printToConsole("ImportMatchDataActivity::onActivityResult : requestCode: " + requestCode);
	    // Check which request we're responding to
	    if (requestCode == BLUETOOTH_SEND) {
	    	FTSUtilities.printToConsole("ImportMatchDataActivity::onActivityResult : Sharing Activity Returned");
	    	Toast.makeText(getBaseContext(), "File sent", Toast.LENGTH_SHORT).show();
//			if(saveDir.mkdir() || saveDir.isDirectory()) {
//				int numFiles = (saveDir.list(new CSVFilenameFilter(".csv")).length) + 1;
//				String saveFileName = exportFileName + "_" + numFiles + ".csv";
//				File saveFile = new File(saveDir, saveFileName);
//				if(myExportFile.exists()) {
//					myExportFile.renameTo(saveFile);
//					Toast.makeText(this, "File Saved", 3).show();
//				}
//			}
	    }
	}
	
	public void copy(File src, File dst) throws IOException {
	    FileInputStream inStream = new FileInputStream(src);
	    FileOutputStream outStream = new FileOutputStream(dst);
	    FileChannel inChannel = inStream.getChannel();
	    FileChannel outChannel = outStream.getChannel();
	    inChannel.transferTo(0, inChannel.size(), outChannel);
	    inStream.close();
	    outStream.close();
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
        if(myTempFile.exists()) myTempFile.delete();
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
	
	public class CSVFilenameFilter implements FilenameFilter {
		String ext;
		
		public CSVFilenameFilter(String ext) {
		    this.ext = ext; 

		}
		
		@Override
		public boolean accept(File dir, String filename) {
		   //If you want to perform a case-insensitive search
		   return filename.toLowerCase().endsWith(ext.toLowerCase());
		}
	}
}
