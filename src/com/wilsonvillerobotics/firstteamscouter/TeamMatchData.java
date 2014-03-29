package com.wilsonvillerobotics.firstteamscouter;

//import java.util.Hashtable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Environment;
import android.widget.TextView;
import android.content.ContextWrapper;

import com.wilsonvillerobotics.firstteamscouter.dbAdapters.MatchDataDBAdapter;
import com.wilsonvillerobotics.firstteamscouter.dbAdapters.TeamDataDBAdapter;
import com.wilsonvillerobotics.firstteamscouter.dbAdapters.TeamMatchDBAdapter;
import com.wilsonvillerobotics.firstteamscouter.utilities.FTSUtilities;

public class TeamMatchData {
	private int AUTO_SCORE_BONUS = 5;
	private int AUTO_SCORE_HOT_BONUS = 5;
	private int HI_POINTS = 10;
	private int LO_POINTS = 1;
	private int TRUSS_TOSS_POINTS = 10;
	private int TRUSS_CATCH_POINTS = 10;
	private String tabletID;
	private String saveFileName;
	protected String COMMA = ",";
	
	public enum ROBOT_ROLE {
		SHOOTER (0, "Shooter"),
		DEFENDER (1, "Defender"),
		PASSER (2, "Passer"),
		CATCHER (3, "Catcher"),
		GOALIE (4, "Goalie"),
		NOT_SET (5, "Not Set");
		
		private int id;
		private String strRobotRole;
		ROBOT_ROLE(int id, String robotRole) {
			this.id = id;
			this.strRobotRole = robotRole;
		}
		
		public String myRobotRole() {
			return this.strRobotRole;
		}
	}
	
	public enum BALL_CONTROL {
		GROUND_PICKUP (0, "Ground Pickup"),
		HUMAN_LOAD (1, "Human Load"),
		HI_TO_LO (2, "Hi to Lo"),
		LO_TO_HI (3, "Lo to Hi"),
		HI_TO_HI (4, "Hi to Hi"),
		LO_TO_LO (5, "Lo to Lo");
		
		private int id;
		private String strBallControl;
		BALL_CONTROL(int id, String ballControlType) {
			this.id = id;
			this.strBallControl = ballControlType;
		}
		
		public String myBallControlType() {
			return this.strBallControl;
		}
	}
	
	public enum STARTING_LOC {
		FIELD_RIGHT (0, "Field Right"),
		FIELD_RIGHT_CENTER (1, "Field Right Center"),
		FIELD_CENTER (2, "Field Center"),
		FIELD_LEFT_CENTER (3, "Field Left Center"),
		FIELD_LEFT (4, "Field Left"),
		FIELD_GOAL (5, "Field Goal"),
		FIELD_NOT_SET (6, "Field Position Not Set");
		
		private int id;
		private String positionName;
		STARTING_LOC(int id, String posName) {
			this.id = id;
			this.positionName = posName;
		}
		
		public static STARTING_LOC getLocForID(int ID) {
			for(STARTING_LOC sl : STARTING_LOC.values()) {
				if(sl.id == ID) {
					return sl;
				}
			}
			return STARTING_LOC.FIELD_NOT_SET;
		}
		
		public String myPositionName() {
			return this.positionName;
		}
	}
	
	public enum ZONE {
		RED_ZONE (0, TeamMatchDBAdapter.COLUMN_NAME_DEFEND_RED, TeamMatchDBAdapter.COLUMN_NAME_ASSIST_RED),
		WHITE_ZONE (1, TeamMatchDBAdapter.COLUMN_NAME_DEFEND_WHITE, TeamMatchDBAdapter.COLUMN_NAME_ASSIST_WHITE),
		BLUE_ZONE (2, TeamMatchDBAdapter.COLUMN_NAME_DEFEND_BLUE, TeamMatchDBAdapter.COLUMN_NAME_ASSIST_BLUE),
		GOAL_ZONE (3, TeamMatchDBAdapter.COLUMN_NAME_DEFEND_GOAL, "");
		
		private int id;
		private String dbDefendColName;
		private String dbAssistColName;
		ZONE(int id, String dColumnName, String aColumnName) {
			this.id = id;
			this.dbDefendColName = dColumnName;
			this.dbAssistColName = aColumnName;
		}
		
		public String dbDefendColumnName() {
			return this.dbDefendColName;
		}
		
		public String dbAssistColumnName() {
			return this.dbAssistColName;
		}
	}

    private Boolean tmDataSaved;
	
	protected TeamMatchDBAdapter tmDBAdapter;
	protected Context context;
	
	protected long teamMatchID;
	protected long teamID;
	protected long matchID;
	
	protected Integer teamNumber;
	protected Integer matchNumber;

	//protected Hashtable<String, Integer> statHash;

	/*****************************************
	 * 										 *
	 *				 MEASURES				 *
	 *										 *
	 *****************************************/

	/*****************************************
	 *			Starting Location			 *
	 *****************************************/
	protected STARTING_LOC startingLocation;
    
	/*****************************************
	 *			  Autonomous Mode			 *
	 *****************************************/
	protected int autoHiScore;
	protected int autoHiHot;
	protected int autoHiMiss;
	
	protected int autoLoScore;
	protected int autoLoHot;
	protected int autoLoMiss;
	
	protected Boolean autoMove;
	protected int autoCollect;
	protected int autoDefend;
	
	/*****************************************
	 *				Teleop Mode			 	 *
	 *****************************************/
	protected int teleHiScore;
	protected int teleHiMiss;
	
	protected int teleLoScore;
	protected int teleLoMiss;
	
	protected int[] zonePossessed;

	protected int[] zoneDefended;
	
	protected int trussToss;
	protected int trussMiss;
	
	protected int tossCatch;
	protected int tossMiss;
	
	protected int shortPassSuccess;
	protected int shortPassMiss;
	protected int longPassSuccess;
	protected int longPassMiss;

	/*****************************************
	 *				  Notes			 	 	 *
	 *****************************************/
	protected Boolean brokeDown;
	protected Boolean noMove;
	protected Boolean lostConnection;
	protected String teamMatchNotes;
	
	protected Boolean robotRole[];
	
    protected Boolean ballControl[];


	public TeamMatchData(Context c, String tID, Long teamMatchID) { //, String tnum, String mnum) {
		this.context = c;
		this.tabletID = tID;
		this.teamMatchID = teamMatchID;
		this.teamID = -1;
		this.matchID = -1;
		this.teamNumber = -1; //tnum;
		this.matchNumber = -1; //mnum;
		
		this.saveFileName = this.tabletID + "_match_data_export";
		
		//this.statHash = new Hashtable<String, Integer>();
		//this.statHash.put("autoHiScore", 0);
		this.autoHiScore = 0;
		this.autoHiHot = 0;
		this.autoHiMiss = 0;
		
		this.autoLoScore = 0;
		this.autoLoHot = 0;
		this.autoLoMiss = 0;
		
		this.autoCollect = 0;
		this.autoDefend = 0;
		this.autoMove = false;
		
		this.teleHiScore = 0;
		this.teleHiMiss = 0;
		this.teleLoScore = 0;
		this.teleLoMiss = 0;
		
		this.zonePossessed = new int[ZONE.values().length];
		this.zoneDefended = new int[ZONE.values().length];
		for (ZONE z : ZONE.values()) {
			this.zonePossessed[z.id] = 0;
			this.zoneDefended[z.id] = 0;
		}
		
		this.trussToss = 0;
		this.trussMiss = 0;
		
		this.tossCatch = 0;
		this.tossMiss = 0;
		
		this.shortPassSuccess = 0;
		this.shortPassMiss = 0;
		this.longPassSuccess = 0;
		this.longPassMiss = 0;
		
		this.brokeDown = false;
		this.noMove = false;
		this.lostConnection = false;
		
		this.tmDataSaved = false;

		this.robotRole = new Boolean[ROBOT_ROLE.values().length];
		for(ROBOT_ROLE rr : ROBOT_ROLE.values()) {
			this.robotRole[rr.id] = false;  
		}
		
		this.startingLocation = STARTING_LOC.FIELD_NOT_SET;
		
		this.ballControl = new Boolean[BALL_CONTROL.values().length];
		for(BALL_CONTROL bc : BALL_CONTROL.values()) {
			this.ballControl[bc.id] = false;  
		}
		
		this.teamMatchNotes = "";
		
		this.tmDBAdapter = null;
		this.openDB();
		this.loadTeamMatchData();
	}
	
	public void openDB() {
		if(this.tmDBAdapter == null) {
			this.tmDBAdapter = new TeamMatchDBAdapter(this.context);
		}
		
		try {
			FTSUtilities.printToConsole("TeamMatchData::openDB : OPENING DB\n");
			this.tmDBAdapter.open();
		} catch(SQLException e) {
			e.printStackTrace();
			this.tmDBAdapter = null;
		}
		
//		if(this.tmDBAdapter.dbNotOpen()) {
//			try {
//				FTSUtilities.printToConsole("TeamMatchData::openDB : OPENING DB\n");
//				this.tmDBAdapter.open();
//			} catch(SQLException e) {
//				e.printStackTrace();
//				this.tmDBAdapter = null;
//			}
//		} else {
//			FTSUtilities.printToConsole("TeamMatchData::openDB : DB ALREADY OPEN ?!?\n");
//		}
	}
	
	public void closeDB() {
		try {
			FTSUtilities.printToConsole("TeamMatchData::closeDB : CLOSING DB\n");
			this.tmDBAdapter.close();
		} catch (Exception e) {
			FTSUtilities.printToConsole("TeamMatchData::closeDB : EXCEPTION when closing DB\n");
		}
	}
	
	private Hashtable<String, Integer> getIntValueHash() {
		Hashtable<String, Integer> htIntValues = new Hashtable<String, Integer>();
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_START_LOCATION, this.startingLocation.id);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_SCORE, this.getAutoScore()); 
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_HI_SCORE, this.autoHiScore);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_HI_HOT, this.autoHiHot);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_HI_MISS, this.autoHiMiss);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_LO_SCORE, this.autoLoScore);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_LO_HOT, this.autoLoHot);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_LO_MISS, this.autoLoMiss);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_COLLECT, this.autoCollect);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_COLLECT, this.autoDefend);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TELE_SCORE, this.getTeleScore());
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TELE_HI_SCORE, this.teleHiScore);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TELE_HI_MISS, this.teleHiMiss);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TELE_LO_SCORE, this.teleLoScore);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TELE_LO_MISS, this.teleLoMiss);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_ASSIST_RED, this.getZoneAssists(ZONE.RED_ZONE));
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_ASSIST_WHITE, this.getZoneAssists(ZONE.WHITE_ZONE));
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_ASSIST_BLUE, this.getZoneAssists(ZONE.BLUE_ZONE));
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_DEFEND_RED, this.getZoneDefends(ZONE.RED_ZONE));
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_DEFEND_WHITE, this.getZoneDefends(ZONE.WHITE_ZONE));
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_DEFEND_BLUE, this.getZoneDefends(ZONE.BLUE_ZONE));
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_DEFEND_GOAL, this.getZoneDefends(ZONE.GOAL_ZONE));
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TRUSS_TOSS, this.trussToss);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TRUSS_MISS, this.trussMiss);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TOSS_CATCH, this.tossCatch);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TOSS_MISS, this.tossMiss);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_SHORT_PASS_SUCCESS, this.shortPassSuccess);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_SHORT_PASS_MISS, this.shortPassMiss);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_LONG_PASS_SUCCESS, this.longPassSuccess);
		htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_LONG_PASS_MISS, this.longPassMiss);
		
		return htIntValues;
	}
	
	private String getIntCSVHeader() {
		String retVal = "";
		retVal += TeamMatchDBAdapter.COLUMN_NAME_START_LOCATION + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_AUTO_SCORE + COMMA; 
		retVal += TeamMatchDBAdapter.COLUMN_NAME_AUTO_HI_SCORE + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_AUTO_HI_HOT + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_AUTO_HI_MISS + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_AUTO_LO_SCORE + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_AUTO_LO_HOT + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_AUTO_LO_MISS + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_AUTO_COLLECT + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_AUTO_COLLECT + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_TELE_SCORE + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_TELE_HI_SCORE + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_TELE_HI_MISS + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_TELE_LO_SCORE + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_TELE_LO_MISS + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_ASSIST_RED + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_ASSIST_WHITE + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_ASSIST_BLUE + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_DEFEND_RED + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_DEFEND_WHITE + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_DEFEND_BLUE + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_DEFEND_GOAL + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_TRUSS_TOSS + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_TRUSS_MISS + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_TOSS_CATCH + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_TOSS_MISS + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_SHORT_PASS_SUCCESS + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_SHORT_PASS_MISS + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_LONG_PASS_SUCCESS + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_LONG_PASS_MISS;
		
		return retVal;
	}
	
	private String getIntCSVString() {
		String retVal = "";
		retVal += this.startingLocation.id + COMMA;
		retVal += this.getAutoScore() + COMMA; 
		retVal += this.autoHiScore + COMMA;
		retVal += this.autoHiHot + COMMA;
		retVal += this.autoHiMiss + COMMA;
		retVal += this.autoLoScore + COMMA;
		retVal += this.autoLoHot + COMMA;
		retVal += this.autoLoMiss + COMMA;
		retVal += this.autoCollect + COMMA;
		retVal += this.autoDefend + COMMA;
		retVal += this.getTeleScore() + COMMA;
		retVal += this.teleHiScore + COMMA;
		retVal += this.teleHiMiss + COMMA;
		retVal += this.teleLoScore + COMMA;
		retVal += this.teleLoMiss + COMMA;
		retVal += this.getZoneAssists(ZONE.RED_ZONE) + COMMA;
		retVal += this.getZoneAssists(ZONE.WHITE_ZONE) + COMMA;
		retVal += this.getZoneAssists(ZONE.BLUE_ZONE) + COMMA;
		retVal += this.getZoneDefends(ZONE.RED_ZONE) + COMMA;
		retVal += this.getZoneDefends(ZONE.WHITE_ZONE) + COMMA;
		retVal += this.getZoneDefends(ZONE.BLUE_ZONE) + COMMA;
		retVal += this.getZoneDefends(ZONE.GOAL_ZONE) + COMMA;
		retVal += this.trussToss + COMMA;
		retVal += this.trussMiss + COMMA;
		retVal += this.tossCatch + COMMA;
		retVal += this.tossMiss + COMMA;
		retVal += this.shortPassSuccess + COMMA;
		retVal += this.shortPassMiss + COMMA;
		retVal += this.longPassSuccess + COMMA;
		retVal += this.longPassMiss;
		
		return retVal;
	}
	
	private Hashtable<String, Boolean> getBoolValueHash() {
		Hashtable<String, Boolean> htBoolValues = new Hashtable<String, Boolean>();
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_TEAM_MATCH_DATA_UPDATED, this.tmDataSaved);
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_MOVE, this.autoMove);
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_BROKE_DOWN, this.brokeDown);
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_NO_MOVE, this.noMove);
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_LOST_CONNECTION, this.lostConnection);
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_ROLE_SHOOTER, this.robotRole[ROBOT_ROLE.SHOOTER.id]);
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_ROLE_DEFENDER, this.robotRole[ROBOT_ROLE.DEFENDER.id]);
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_ROLE_PASSER, this.robotRole[ROBOT_ROLE.PASSER.id]);
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_ROLE_CATCHER, this.robotRole[ROBOT_ROLE.CATCHER.id]);
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_ROLE_GOALIE, this.robotRole[ROBOT_ROLE.GOALIE.id]);
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_GROUND_PICKUP, this.ballControl[BALL_CONTROL.GROUND_PICKUP.id]);
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_HUMAN_LOAD, this.ballControl[BALL_CONTROL.HUMAN_LOAD.id]);
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_HI_TO_LO, this.ballControl[BALL_CONTROL.HI_TO_LO.id]);
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_LO_TO_HI, this.ballControl[BALL_CONTROL.LO_TO_HI.id]);
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_HI_TO_HI, this.ballControl[BALL_CONTROL.HI_TO_HI.id]);
		htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_LO_TO_LO, this.ballControl[BALL_CONTROL.LO_TO_LO.id]);
		
		return htBoolValues;
	}

	private String getBoolCSVHeader() {
		String retVal = "";
		retVal += TeamMatchDBAdapter.COLUMN_NAME_TEAM_MATCH_DATA_UPDATED + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_AUTO_MOVE + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_BROKE_DOWN + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_NO_MOVE + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_LOST_CONNECTION + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_ROLE_SHOOTER + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_ROLE_DEFENDER + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_ROLE_PASSER + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_ROLE_CATCHER + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_ROLE_GOALIE + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_GROUND_PICKUP + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_HUMAN_LOAD + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_HI_TO_LO + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_LO_TO_HI + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_HI_TO_HI + COMMA;
		retVal += TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_LO_TO_LO;
		
		return retVal;
	}

	private String getBoolCSVString() {
		String retVal = "";
		retVal += this.tmDataSaved + COMMA;
		retVal += this.autoMove + COMMA;
		retVal += this.brokeDown + COMMA;
		retVal += this.noMove + COMMA;
		retVal += this.lostConnection + COMMA;
		retVal += this.robotRole[ROBOT_ROLE.SHOOTER.id] + COMMA;
		retVal += this.robotRole[ROBOT_ROLE.DEFENDER.id] + COMMA;
		retVal += this.robotRole[ROBOT_ROLE.PASSER.id] + COMMA;
		retVal += this.robotRole[ROBOT_ROLE.CATCHER.id] + COMMA;
		retVal += this.robotRole[ROBOT_ROLE.GOALIE.id] + COMMA;
		retVal += this.ballControl[BALL_CONTROL.GROUND_PICKUP.id] + COMMA;
		retVal += this.ballControl[BALL_CONTROL.HUMAN_LOAD.id] + COMMA;
		retVal += this.ballControl[BALL_CONTROL.HI_TO_LO.id] + COMMA;
		retVal += this.ballControl[BALL_CONTROL.LO_TO_HI.id] + COMMA;
		retVal += this.ballControl[BALL_CONTROL.HI_TO_HI.id] + COMMA;
		retVal += this.ballControl[BALL_CONTROL.LO_TO_LO.id];
		
		return retVal;
	}

	public boolean save() {
		this.savingData();
		if(this.tmDBAdapter != null) {
			try{
				Hashtable<String, Integer> htIntValues = getIntValueHash();
				Hashtable<String, Boolean> htBoolValues = getBoolValueHash();
				
				for (ZONE z : ZONE.values()) {
					FTSUtilities.printToConsole("TeamMatchData::save : Zone: " + z.name().toString() + "\nAssist: " + z.dbAssistColName +
							"\nDefend: " + z.dbDefendColName);
					htIntValues.put(z.dbDefendColName, this.getZoneDefends(z));
					
					if(z != ZONE.GOAL_ZONE) {
						htIntValues.put(z.dbAssistColName, this.getZoneAssists(z));
					}
				}
				
				/***
				 * Add new fields for robot role, starting position, and ball control
				 * 
				 * Create a Boolean hash like the int hash above to hold all of the Boolean values
				 */

				Boolean retVal = this.tmDBAdapter.updateTeamMatch(this.teamMatchID, this.teamID, this.matchID, this.teamMatchNotes, htBoolValues, htIntValues);
				FileOutputStream fo = null;
				boolean append = false;
				
				try {
				    String storageState = Environment.getExternalStorageState();
				    if (retVal && storageState.equals(Environment.MEDIA_MOUNTED)) {
				    	File filePath = context.getExternalFilesDir(null);
				        File file = new File(filePath, saveFileName);
				        FTSUtilities.printToConsole("TeamMatchData::save : file " + ((file == null) ? "IS NULL" : "IS VALID") + "\n");
				        
				        if(!file.exists()) {
				        	file.createNewFile();
				        } else {
				        	append = true;
				        }
			        	
				        fo = new FileOutputStream(file, append);
				        
				        if(!append) {
					        String headerOut = "tablet_id" + COMMA;
					        headerOut += TeamMatchDBAdapter._ID + COMMA + TeamMatchDBAdapter.COLUMN_NAME_TEAM_ID + COMMA;
					        headerOut += TeamMatchDBAdapter.COLUMN_NAME_MATCH_ID + COMMA;
					        headerOut += getIntCSVHeader() + COMMA;
					        headerOut += getBoolCSVHeader() + COMMA;
					        headerOut += TeamMatchDBAdapter.COLUMN_NAME_TEAM_MATCH_NOTES + "\n";
					        
					        fo.write(headerOut.getBytes());
				        }
		        	    
		        	    String csvOut = this.tabletID + COMMA;
		        	    csvOut += this.teamMatchID + COMMA + this.teamNumber + COMMA + this.matchNumber + COMMA;
		        	    csvOut += getIntCSVString() + COMMA;
		        	    csvOut += getBoolCSVString() + COMMA;
		        	    csvOut += this.teamMatchNotes.replaceAll(COMMA, ";").replaceAll("\n", " ");
		        	    csvOut += "\n";
			        	fo.write(csvOut.getBytes());
				    }
				}
				catch(Exception e) {
					FTSUtilities.printToConsole("TeamMatchData::save : Exception accessing file");
				}
				finally {
					try {
						if(fo != null) {
							fo.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				FTSUtilities.printToConsole("Update Successful? : " + retVal.toString());
				return retVal;
			} catch (NumberFormatException e) {
				this.teamNumber = -1;
				this.dataSaveFailed();
			}
		}
		return false;
	}

	private void savingData() {
		this.tmDataSaved = true;
	}
	
	private void dataSaveFailed() {
		this.tmDataSaved = false;
	}
	
	public Boolean hasSavedData() {
		return this.tmDataSaved;
	}

	public void loadTeamMatchData() {
		FTSUtilities.printToConsole("TeamMatchData::loadTeamMatchData : Loading Data for teamMatchID: " + String.valueOf(this.teamMatchID) + "\n");
		Cursor tmCursor = null;
		/*
		//this.statHash = new Hashtable<String, Integer>();
		//this.statHash.put("autoHiScore", 0);
		 */
		try {
			tmCursor = this.tmDBAdapter.getTeamMatch(this.teamMatchID);
			FTSUtilities.printToConsole("TeamMatchData::loadTeamMatchData : tmCursor Count: " + tmCursor.getCount());
			
			this.matchID = tmCursor.getLong(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_MATCH_ID));
			this.teamID = tmCursor.getLong(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TEAM_ID));
			this.tmDataSaved = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TEAM_MATCH_DATA_UPDATED)));
			
			Hashtable<String, Integer> nums = this.tmDBAdapter.getTeamAndMatchNumbersForTeamMatchID(teamMatchID);
			
			this.teamNumber = nums.get(TeamDataDBAdapter.COLUMN_NAME_TEAM_NUMBER);
			this.matchNumber = nums.get(MatchDataDBAdapter.COLUMN_NAME_MATCH_NUMBER);
			
			if(this.tmDataSaved) {
				FTSUtilities.printToConsole("TeamMatchData::loadTeamMatchData : Loading Saved Data\n");
				this.autoHiScore = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_HI_SCORE));
				this.autoHiHot = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_HI_HOT));
				this.autoHiMiss = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_HI_MISS));
				
				this.autoLoScore = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_LO_SCORE));
				this.autoLoHot = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_LO_HOT));
				this.autoLoMiss = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_LO_MISS));
				
				this.autoCollect = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_COLLECT));
				this.autoDefend = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_DEFEND));
				this.autoMove = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_MOVE)));
				
				this.teleHiScore = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TELE_HI_SCORE));
				this.teleHiMiss = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TELE_HI_MISS));
				
				this.teleLoScore = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TELE_LO_SCORE));
				this.teleLoMiss = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TELE_LO_MISS));
				
				for (ZONE z : ZONE.values()) {
					this.zonePossessed[z.id] = (z.dbAssistColName != "") ? tmCursor.getInt(tmCursor.getColumnIndexOrThrow(z.dbAssistColName)) : -1;
					this.zoneDefended[z.id] = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(z.dbDefendColName));
				}
				
				this.trussToss = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TRUSS_TOSS));
				this.trussMiss = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TRUSS_MISS));
				
				this.tossCatch = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TOSS_CATCH));
				this.tossMiss = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TRUSS_MISS));
				
				this.shortPassSuccess = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_SHORT_PASS_SUCCESS));
				this.shortPassMiss = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_SHORT_PASS_MISS));
				this.longPassSuccess = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_LONG_PASS_SUCCESS));
				this.longPassMiss = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_LONG_PASS_MISS));
				
				this.brokeDown = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_BROKE_DOWN)));
				this.noMove = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_NO_MOVE)));
				this.lostConnection = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_LOST_CONNECTION)));
				
				this.robotRole[ROBOT_ROLE.SHOOTER.id] = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_ROLE_SHOOTER)));
				this.robotRole[ROBOT_ROLE.DEFENDER.id] = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_ROLE_DEFENDER)));
				this.robotRole[ROBOT_ROLE.PASSER.id] = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_ROLE_PASSER)));
				this.robotRole[ROBOT_ROLE.CATCHER.id] = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_ROLE_CATCHER)));
				this.robotRole[ROBOT_ROLE.GOALIE.id] = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_ROLE_GOALIE)));

				this.startingLocation = STARTING_LOC.getLocForID(tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_START_LOCATION)));

				this.ballControl[BALL_CONTROL.GROUND_PICKUP.id] = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_GROUND_PICKUP)));
				this.ballControl[BALL_CONTROL.HUMAN_LOAD.id] = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_HUMAN_LOAD)));
				this.ballControl[BALL_CONTROL.HI_TO_LO.id] = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_HI_TO_LO)));
				this.ballControl[BALL_CONTROL.LO_TO_HI.id] = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_LO_TO_HI)));
				this.ballControl[BALL_CONTROL.HI_TO_HI.id] = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_HI_TO_HI)));
				this.ballControl[BALL_CONTROL.LO_TO_LO.id] = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_BALL_CONTROL_LO_TO_LO)));
				
				this.teamMatchNotes = tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TEAM_MATCH_NOTES));

			} else {
				FTSUtilities.printToConsole("TeamMatchData::loadTeamMatchData : No Saved Data : tmDataSaved:: " + this.tmDataSaved.toString() + "\n");
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		if(tmCursor != null) {
			tmCursor.close();
		}
	}
	
	/*************************************
	 *									 *
	 *             GETTERS				 *
	 * 									 *
	 *************************************/
	public String getTeamNumber() {
		return String.valueOf(this.teamNumber);
	}
	
	public String getMatchNumber() {
		return String.valueOf(this.matchNumber);
	}
	
	public int getAutoHiScore() {
		int score = (this.autoHiScore * this.HI_POINTS);
		score += (this.autoHiHot * this.AUTO_SCORE_HOT_BONUS);
		return score;
	}
	
	public int getAutoLoScore() {
		int score = (this.autoLoScore * this.LO_POINTS);
		score += (this.autoLoHot * this.AUTO_SCORE_HOT_BONUS);
		return score;
	}
	
	public int getAutoScore() {
		return this.getAutoHiScore() + this.getAutoLoScore() + this.getAutoScoreBonus();
	}
	
	public int getAutoScoreBonus() {
		return (this.autoHiScore + this.autoLoScore) * this.AUTO_SCORE_BONUS;
	}
	
	public int getTeleScore() {
		return (this.teleHiScore * this.HI_POINTS) + 
				(this.teleLoScore * this.LO_POINTS) + 
				(this.trussToss * this.TRUSS_TOSS_POINTS) +
				(this.tossCatch * this.TRUSS_CATCH_POINTS);
	}
	
	public int teleHiAttempts() {
		return this.teleHiScore + this.teleHiMiss;
	}
	
	public int teleLoAttempts() {
		return this.teleLoScore + this.teleLoMiss;
	}
	
	public int totalTeleAttempts() {
		return this.teleHiAttempts() + this.teleLoAttempts();
	}

	public double getTeleShotPercentage() {
		double teleAttempts = (double)this.totalTeleAttempts();
		double retVal = 0.0;
		if(teleAttempts > 0) {
			retVal = 100.0 * ((this.teleHiScore + this.teleLoScore) / teleAttempts);
		}
		
		return retVal;
	}
	
	public int getIntForBool(boolean boolVal) {
		return (boolVal ? 1 : 0);
	}

	private int getDefensiveRating() {
		int retVal = this.getZoneAssists(ZONE.RED_ZONE) + this.getZoneAssists(ZONE.WHITE_ZONE) + this.getZoneAssists(ZONE.BLUE_ZONE);
		return retVal;
	}

	public int getZoneAssists(ZONE z) {
		return this.zonePossessed[z.id];
	}
	
	public int getZoneDefends(ZONE z) {
		return this.zoneDefended[z.id];
	}

	public String getNoteText() {
		return this.teamMatchNotes;
	}
	
	public STARTING_LOC getStartingPosition() {
		return this.startingLocation;
	}
	
	public Boolean isRobotRoleChecked(ROBOT_ROLE rr) {
		return this.robotRole[rr.id];
	}
	
	public Boolean isBallControlChecked(BALL_CONTROL bc) {
		return this.ballControl[bc.id];
	}
	
	/*************************************
	 *									 *
	 *              SETTERS				 *
	 * 									 *
	 *************************************/
	public void setStartingLoc(STARTING_LOC sl) {
		FTSUtilities.printToConsole("TeamMatchData::setStartingLoc : Setting SL : " + sl.toString() + "\n");
		this.startingLocation = sl;
	}
	
	public void setMatchhNumber(int mnum) {
		this.matchNumber = mnum;
	}
	
	public void setTeamNumber(int tnum) {
		this.teamNumber = tnum;
	}
	
	public void clearStartingLoc() {
		this.startingLocation = STARTING_LOC.FIELD_NOT_SET;
	}

	public void addRobotRole(ROBOT_ROLE role) {
		this.robotRole[role.id] = true;
	}
	
	public void removeRobotRole(ROBOT_ROLE role) {
		this.robotRole[role.id] = false;
	}

	public void addBallControl(BALL_CONTROL bc) {
		this.ballControl[bc.id] = true;
	}
	
	public void removeBallControl(BALL_CONTROL bc) {
		this.ballControl[bc.id] = false;
	}
	
	public void setNoteText(CharSequence s) {
		this.teamMatchNotes = s.toString();
	}

	/*************************************
	 *									 *
	 *           INCREMENTERS			 *
	 * 									 *
	 *************************************/

	/*************************************
	 *            AUTONOMOUS			 *
	 *************************************/
	public void addAutoHiScore() {
		this.autoHiScore++;
	}
	
	public void addHiHotBonus() {
		this.autoHiHot++;
	}

	public void addAutoHiMiss() {
		this.autoHiMiss++;
	}

	public void addAutoLoScore() {
		this.autoLoScore++;
	}

	public void addLoHotBonus() {
		this.autoLoHot++;
	}
	
	public void addAutoLoMiss() {
		this.autoLoMiss++;
	}

	public void movedInAuto() {
		this.autoMove = true;
	}

	public void addAutoCollect() {
		this.autoCollect++;
	}

	public void addAutoDefend() {
		this.autoDefend++;
		defendedZone(ZONE.GOAL_ZONE);
	}

	/*************************************
	 *              TELEOP	 			 *
	 *************************************/
	public void addTeleHiScore() {
		this.teleHiScore++;
	}

	public void addTeleHiMiss() {
		this.teleHiMiss++;
	}

	public void addTeleLoScore() {
		this.teleLoScore++;
	}

	public void addTeleLoMiss() {
		this.teleLoMiss++;
	}
	
	public void addTeleLongPass() {
		this.longPassSuccess++;
	}
	
	public void addTeleLongPassMiss() {
		this.longPassMiss++;
	}
	
	public void addTeleShortPass() {
		this.shortPassSuccess++;
	}
	
	public void addTeleShortPassMiss() {
		this.shortPassMiss++;
	}
	
	public void defendedZone(ZONE z) {
		this.zoneDefended[z.id]++;
	}
	
	public void possessedZone(ZONE z) {
		this.zonePossessed[z.id]++;
	}
	
	public void addTeleTrussToss() {
		this.trussToss++;
	}
	
	public void addTeleTrussMiss() {
		this.trussMiss++;
	}
	
	public void addTeleTossCatch() {
		this.tossCatch++;
	}
	
	public void addTeleTossMiss() {
		this.tossMiss++;
	}
	
	/*************************************
	 *									 *
	 *           DECREMENTERS			 *
	 * 									 *
	 *************************************/

	/*************************************
	 *            AUTONOMOUS			 *
	 *************************************/
	public void lowerAutoHiScore() {
		if(this.autoHiScore > 0) {
			this.autoHiScore--;
		}
	}
	
	public void lowerHiHotBonus() {
		if(this.autoHiHot > 0) {
			this.autoHiHot--;
		}
	}

	public void lowerAutoHiMiss() {
		if(this.autoHiMiss > 0) {
			this.autoHiMiss--;
		}
	}

	public void lowerAutoLoScore() {
		if(this.autoLoScore > 0) {
			this.autoLoScore--;
		}
	}

	public void lowerLoHotBonus() {
		if(this.autoLoHot > 0) {
			this.autoLoHot--;
		}
	}
	
	public void lowerAutoLoMiss() {
		if(this.autoLoMiss > 0) {
			this.autoLoMiss--;
		}
	}

	public void didNotMoveInAuto() {
		this.autoMove = false;
	}

	public void lowerAutoCollect() {
		if(this.autoCollect > 0) {
			this.autoCollect--;
		}
	}

	public void lowerAutoDefend() {
		if(this.autoDefend > 0) {
			this.autoDefend--;
			didNotDefendZone(ZONE.GOAL_ZONE);
		}
	}

	/*************************************
	 *              TELEOP	 			 *
	 *************************************/
	public void lowerTeleHiScore() {
		if(this.teleHiScore > 0) {
			this.teleHiScore--;
		}
	}

	public void lowerTeleHiMiss() {
		if(this.teleHiMiss > 0) {
			this.teleHiMiss--;
		}
	}

	public void lowerTeleLoScore() {
		if(this.teleLoScore > 0) {
			this.teleLoScore--;
		}
	}

	public void lowerTeleLoMiss() {
		if(this.teleLoMiss > 0) {
			this.teleLoMiss--;
		}
	}
	
	public void lowerTeleLongPass() {
		if(this.longPassSuccess > 0) {
			this.longPassSuccess--;
		}
	}
	
	public void lowerTeleLongPassMiss() {
		if(this.longPassMiss > 0) {
			this.longPassMiss--;		}
	}
	
	public void lowerTeleShortPass() {
		if(this.shortPassSuccess > 0) {
			this.shortPassSuccess--;
		}
	}
	
	public void lowerTeleShortPassMiss() {
		if(this.shortPassMiss > 0) {
			this.shortPassMiss--;
		}
	}
	
	public void didNotDefendZone(ZONE z) {
		if(this.zoneDefended[z.id] > 0) {
			this.zoneDefended[z.id]--;
		}
	}
	
	public void didNotPossessZone(ZONE z) {
		if(this.zonePossessed[z.id] > 0) {
			this.zonePossessed[z.id]--;
		}
	}

	public void lowerTeleTrussToss() {
		if(this.trussToss > 0) {
			this.trussToss--;
		}
	}
	
	public void lowerTeleTrussMiss() {
		if(this.trussMiss > 0) {
			this.trussMiss--;
		}
	}
	
	public void lowerTeleTossCatch() {
		if(this.tossCatch > 0) {
			this.tossCatch--;
		}
	}
	
	public void lowerTeleTossMiss() {
		if(this.tossMiss > 0) {
			this.tossMiss--;
		}
	}
}
