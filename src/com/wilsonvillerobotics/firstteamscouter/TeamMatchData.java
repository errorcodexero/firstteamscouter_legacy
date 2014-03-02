package com.wilsonvillerobotics.firstteamscouter;

//import java.util.Hashtable;

import java.util.Hashtable;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.widget.TextView;

import com.wilsonvillerobotics.firstteamscouter.dbAdapters.TeamMatchDBAdapter;
import com.wilsonvillerobotics.firstteamscouter.utilities.FTSUtilities;

public class TeamMatchData {
	private int AUTO_SCORE_BONUS = 5;
	private int AUTO_SCORE_HOT_BONUS = 5;
	private int HI_POINTS = 10;
	private int LO_POINTS = 1;
	private int TRUSS_TOSS_POINTS = 10;
	private int TRUSS_CATCH_POINTS = 10;
	
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

	protected TeamMatchDBAdapter tmDBAdapter;
	protected Context context;
	
	protected int teamMatchID;
	
	protected String teamNumber;
	protected String matchNumber;
	
	//protected Hashtable<String, Integer> statHash;
	
	protected int autoHiScore;
	protected int autoLoScore;
	protected int autoHiMiss;
	protected int autoLoMiss;
	
	protected int autoHiHot;
	protected int autoLoHot;
	
	protected int autoCollect;
	protected Boolean autoMove;
	protected int autoDefend;
	
	protected int teleHiScore;
	protected int teleLoScore;
	protected int teleHiMiss;
	protected int teleLoMiss;
	
	protected int trussToss;
	protected int trussMiss;
	
	protected int tossCatch;
	protected int tossMiss;
	
	protected int shortPassSuccess;
	protected int shortPassMiss;
	protected int longPassSuccess;
	protected int longPassMiss;

	protected int[] zoneAssisted;

	protected int[] zoneDefended;
	
	protected Boolean brokeDown;
	protected Boolean noMove;
	protected Boolean lostConnection;
	
	protected Boolean robotRole[];
	/*protected Boolean roleShooter;
	protected Boolean roleDefender;
	protected Boolean rolePasser;
	protected Boolean roleCatcher;
	protected Boolean roleGoalie;*/
	
    protected STARTING_LOC startingLocation;
    
    protected Boolean ballControl[];

    /*protected Boolean ballControlGroundPickup;
    protected Boolean ballControlHumanLoad;
    protected Boolean ballControlHoToLo;
    protected Boolean ballControlLoToHi;
    protected Boolean ballControlHiToHi;
    protected Boolean ballControlLoToLo;*/
	
	private Boolean tmDataSaved;
	
	public TeamMatchData(Context c, int tmID) { //, String tnum, String mnum) {
		this.context = c;
		this.teamMatchID = tmID;
		this.teamNumber = ""; //tnum;
		this.matchNumber = ""; //mnum;
		
		//this.statHash = new Hashtable<String, Integer>();
		//this.statHash.put("autoHiScore", 0);
		this.autoHiScore = 0;
		this.autoLoScore = 0;
		this.autoHiMiss = 0;
		this.autoLoMiss = 0;
		
		this.autoHiHot = 0;
		this.autoLoHot = 0;
		
		this.autoCollect = 0;
		this.autoDefend = 0;
		this.autoMove = false;
		
		this.teleHiScore = 0;
		this.teleLoScore = 0;
		this.teleHiMiss = 0;
		this.teleLoMiss = 0;
		
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

		this.zoneAssisted = new int[ZONE.values().length];
		this.zoneDefended = new int[ZONE.values().length];
		for (ZONE z : ZONE.values()) {
			this.zoneAssisted[z.id] = 0;
			this.zoneDefended[z.id] = 0;
		}
		
		this.robotRole = new Boolean[ROBOT_ROLE.values().length];
		for(ROBOT_ROLE rr : ROBOT_ROLE.values()) {
			this.robotRole[rr.id] = false;  
		}
		
		this.startingLocation = STARTING_LOC.FIELD_NOT_SET;
		
		this.ballControl = new Boolean[BALL_CONTROL.values().length];
		for(BALL_CONTROL bc : BALL_CONTROL.values()) {
			this.ballControl[bc.id] = false;  
		}
		
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
	
	public void loadTeamMatchData() {
		FTSUtilities.printToConsole("TeamMatchData::loadTeamMatchData : Loading Data\n");
		Cursor tmCursor = null;
		try {
			tmCursor = this.tmDBAdapter.getTeamMatch(this.teamMatchID);
			
			this.matchNumber = tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_MATCH_ID));
			this.teamNumber = tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TEAM_ID));
			this.tmDataSaved = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_MATCH_DATA_SAVED)));
			
			if(this.tmDataSaved) {
				FTSUtilities.printToConsole("TeamMatchData::loadTeamMatchData : Loading Saved Data\n");
				this.autoHiScore = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_HI_SCORE));
				this.autoLoScore = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_LO_SCORE));
				this.autoHiMiss = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_HI_MISS));
				this.autoLoMiss = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_LO_MISS));
				
				this.autoHiHot = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_HI_HOT));
				this.autoLoHot = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_LO_HOT));
				
				this.autoCollect = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_COLLECT));
				this.autoDefend = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_DEFEND));
				this.autoMove = Boolean.parseBoolean(tmCursor.getString(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_AUTO_MOVE)));
				
				this.teleHiScore = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TELE_HI_SCORE));
				this.teleLoScore = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TELE_LO_SCORE));
				this.teleHiMiss = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TELE_HI_MISS));
				this.teleLoMiss = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(TeamMatchDBAdapter.COLUMN_NAME_TELE_LO_MISS));
				
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
				
				for (ZONE z : ZONE.values()) {
					this.zoneAssisted[z.id] = (z.dbAssistColName != "") ? tmCursor.getInt(tmCursor.getColumnIndexOrThrow(z.dbAssistColName)) : -1;
					this.zoneDefended[z.id] = tmCursor.getInt(tmCursor.getColumnIndexOrThrow(z.dbDefendColName));
				}
				
				/***
				 * TO DO - Add the new fields for ball control, robot role, and starting location
				 */
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
	
	public String getteamNumber() {
		return this.teamNumber;
	}
	
	public void setTeamNumber(String tnum) {
		this.teamNumber = tnum;
	}
	
	public String getMatchNumber() {
		return this.matchNumber;
	}
	
	public void setMatchhNumber(String mnum) {
		this.matchNumber = mnum;
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
	
	public void addAutoHiScore() {
		this.autoHiScore++;
	}
	
	public void addAutoHiMiss() {
		this.autoHiMiss++;
	}

	public void addAutoLoScore() {
		this.autoLoScore++;
	}

	public void addAutoLoMiss() {
		this.autoLoMiss++;
	}

	public int getTeleScore() {
		return (this.teleHiScore * this.HI_POINTS) + 
				(this.teleLoScore * this.LO_POINTS) + 
				(this.trussToss * this.TRUSS_TOSS_POINTS) +
				(this.tossCatch * this.TRUSS_CATCH_POINTS);
	}
	
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
		//return "0.0";
	}
	
	public int getIntForBool(boolean boolVal) {
		return (boolVal ? 1 : 0);
	}

	public boolean save() {
		this.savingData();
		if(this.tmDBAdapter != null) {
			try{
				Hashtable<String, Integer> htIntValues = new Hashtable<String, Integer>();
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_SCORE, this.getAutoScore()); 
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TELE_SCORE, this.getTeleScore());
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_HI_SCORE, this.autoHiScore);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_LO_SCORE, this.autoLoScore);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_HI_MISS, this.autoHiMiss);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_LO_MISS, this.autoLoMiss);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_HI_HOT, this.autoHiHot);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_LO_HOT, this.autoLoHot);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_COLLECT, this.autoCollect);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_AUTO_COLLECT, this.autoDefend);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TELE_HI_SCORE, this.teleHiScore);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TELE_LO_SCORE, this.teleLoScore);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TELE_HI_MISS, this.teleHiMiss);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TELE_LO_MISS, this.teleLoMiss);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TRUSS_TOSS, this.trussToss);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TRUSS_MISS, this.trussMiss);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TOSS_CATCH, this.tossCatch);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_TOSS_MISS, this.tossMiss);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_SHORT_PASS_SUCCESS, this.shortPassSuccess);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_SHORT_PASS_MISS, this.shortPassMiss);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_LONG_PASS_SUCCESS, this.longPassSuccess);
				htIntValues.put(TeamMatchDBAdapter.COLUMN_NAME_LONG_PASS_MISS, this.longPassMiss);
				
//				Hashtable<String, Boolean> htBoolValues = new Hashtable<String, Boolean>();
//				htBoolValues.put(TeamMatchDBAdapter.COLUMN_NAME_LONG_PASS_MISS, this.tmDataSaved);
//				htBoolValues.put(TeamMatchDBAdapter.this.autoMove
//						htBoolValues.put(TeamMatchDBAdapter. this.brokeDown
//								htBoolValues.put(TeamMatchDBAdapter. this.noMove
//										htBoolValues.put(TeamMatchDBAdapter. this.lostConnection
				
				
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

				Boolean retVal = this.tmDBAdapter.updateTeamMatch(this.teamMatchID, this.teamNumber, this.matchNumber, this.tmDataSaved, this.autoMove, this.brokeDown, this.noMove, this.lostConnection, htIntValues);
				FTSUtilities.printToConsole("Update Successful? : " + retVal.toString());
				return retVal;
			} catch (NumberFormatException e) {
				this.teamNumber = "";
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

	public void closeDB() {
		try {
			FTSUtilities.printToConsole("TeamMatchData::closeDB : CLOSING DB\n");
			this.tmDBAdapter.close();
		} catch (Exception e) {
			FTSUtilities.printToConsole("TeamMatchData::closeDB : EXCEPTION when closing DB\n");
		}
	}

	private int getDefensiveRating() {
		int retVal = this.getZoneAssists(ZONE.RED_ZONE) + this.getZoneAssists(ZONE.WHITE_ZONE) + this.getZoneAssists(ZONE.BLUE_ZONE);
		return retVal;
	}

	public void addAutoDefend() {
		this.autoDefend++;
	}

	public void movedInAuto() {
		this.autoMove = true;
	}

	public void addAutoCollect() {
		this.autoCollect++;
	}

	public void addHiHotBonus() {
		this.autoHiHot++;
	}

	public void addLoHotBonus() {
		this.autoLoHot++;
	}
	
	public void defendedZone(ZONE z) {
		this.zoneDefended[z.id]++;
	}
	
	public void assistedZone(ZONE z) {
		this.zoneAssisted[z.id]++;
	}
	
	public int getZoneAssists(ZONE z) {
		return this.zoneAssisted[z.id];
	}
	
	public int getZoneDefends(ZONE z) {
		return this.zoneDefended[z.id];
	}
	
	public void setStartingLoc(STARTING_LOC sl) {
		this.startingLocation = sl;
	}
}
