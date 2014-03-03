package com.wilsonvillerobotics.firstteamscouter.dbAdapters;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

import com.wilsonvillerobotics.firstteamscouter.utilities.FTSUtilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class TeamMatchDBAdapter implements BaseColumns {
	public static final String TABLE_NAME = "team_match";
    public static final String COLUMN_NAME_TEAM_MATCH_ID = "team_match_id";
    public static final String COLUMN_NAME_TEAM_ID = "team_id";
    public static final String COLUMN_NAME_MATCH_ID = "match_id";
    public static final String COLUMN_NAME_MATCH_DATA_SAVED = "match_data_entered";
    public static final String COLUMN_NAME_AUTO_SCORE = "auto_score";
    public static final String COLUMN_NAME_AUTO_HI_SCORE = "auto_hi_score";
    public static final String COLUMN_NAME_AUTO_LO_SCORE = "auto_lo_score";
    public static final String COLUMN_NAME_AUTO_HI_MISS = "auto_hi_miss";
    public static final String COLUMN_NAME_AUTO_LO_MISS = "auto_lo_miss";
    public static final String COLUMN_NAME_AUTO_HI_HOT = "auto_hi_hot";
    public static final String COLUMN_NAME_AUTO_LO_HOT = "auto_lo_hot";
    public static final String COLUMN_NAME_AUTO_COLLECT = "auto_collect";
    public static final String COLUMN_NAME_AUTO_DEFEND = "auto_defend";
    public static final String COLUMN_NAME_AUTO_MOVE = "auto_move";
    public static final String COLUMN_NAME_TELE_SCORE = "tele_score";
    public static final String COLUMN_NAME_TELE_HI_SCORE = "tele_hi_score";
    public static final String COLUMN_NAME_TELE_LO_SCORE = "tele_lo_score";
    public static final String COLUMN_NAME_TELE_HI_MISS = "tele_hi_miss";
    public static final String COLUMN_NAME_TELE_LO_MISS = "tele_lo_miss";
    public static final String COLUMN_NAME_TRUSS_TOSS = "truss_toss";
    public static final String COLUMN_NAME_TRUSS_MISS = "truss_miss";
    public static final String COLUMN_NAME_TOSS_CATCH = "toss_catch";
    public static final String COLUMN_NAME_TOSS_MISS = "toss_miss";
    public static final String COLUMN_NAME_ASSIST_RED = "assist_red";
    public static final String COLUMN_NAME_ASSIST_WHITE = "assist_white";
    public static final String COLUMN_NAME_ASSIST_BLUE = "assist_blue";
    public static final String COLUMN_NAME_SHORT_PASS_SUCCESS = "short_pass_success";
    public static final String COLUMN_NAME_SHORT_PASS_MISS = "short_pass_miss";
    public static final String COLUMN_NAME_LONG_PASS_SUCCESS = "long_pass_success";
    public static final String COLUMN_NAME_LONG_PASS_MISS = "long_pass_miss";
    public static final String COLUMN_NAME_DEFEND_RED = "defend_red";
    public static final String COLUMN_NAME_DEFEND_WHITE = "defend_white";
    public static final String COLUMN_NAME_DEFEND_BLUE = "defend_blue";
    public static final String COLUMN_NAME_DEFEND_GOAL = "defend_goal";
    public static final String COLUMN_NAME_BROKE_DOWN = "broke_down";
    public static final String COLUMN_NAME_NO_MOVE = "no_move";
    public static final String COLUMN_NAME_LOST_CONNECTION = "lost_connection";
    public static final String COLUMN_NAME_ROLE_SHOOTER = "role_shooter";
    public static final String COLUMN_NAME_ROLE_DEFENDER = "role_defender";
    public static final String COLUMN_NAME_ROLE_PASSER = "role_passer";
    public static final String COLUMN_NAME_ROLE_CATCHER = "role_catcher";
    public static final String COLUMN_NAME_ROLE_GOALIE = "role_goalie";
    public static final String COLUMN_NAME_START_LOCATION = "starting_location";
    
    // These should be part of the robot data, not the match data
    public static final String COLUMN_NAME_BALL_CONTROL_GROUND_PICKUP = "ball_control_ground_pickup";
    public static final String COLUMN_NAME_BALL_CONTROL_HUMAN_LOAD = "ball_control_human_load";
    public static final String COLUMN_NAME_BALL_CONTROL_HI_TO_LO = "ball_control_hi_to_lo";
    public static final String COLUMN_NAME_BALL_CONTROL_LO_TO_HI = "ball_control_lo_to_hi";
    public static final String COLUMN_NAME_BALL_CONTROL_HI_TO_HI = "ball_control_hi_to_hi";
    public static final String COLUMN_NAME_BALL_CONTROL_LO_TO_LO = "ball_control_lo_to_lo";
    
    // This needs to be moved to the team_match_notes_data
    public static final String COLUMN_NAME_TEAM_MATCH_NOTES = "team_match_notes";

    private String[] allColumnNames = new String[]{
    		_ID,
    		COLUMN_NAME_TEAM_MATCH_ID,
    	    COLUMN_NAME_TEAM_ID,
    	    COLUMN_NAME_MATCH_ID,
    	    COLUMN_NAME_MATCH_DATA_SAVED,
    	    COLUMN_NAME_AUTO_SCORE,
    	    COLUMN_NAME_AUTO_HI_SCORE,
    	    COLUMN_NAME_AUTO_LO_SCORE,
    	    COLUMN_NAME_AUTO_HI_MISS,
    	    COLUMN_NAME_AUTO_LO_MISS,
    	    COLUMN_NAME_AUTO_HI_HOT,
    	    COLUMN_NAME_AUTO_LO_HOT,
    	    COLUMN_NAME_AUTO_COLLECT,
    	    COLUMN_NAME_AUTO_DEFEND,
    	    COLUMN_NAME_AUTO_MOVE,
    	    COLUMN_NAME_TELE_SCORE,
    	    COLUMN_NAME_TELE_HI_SCORE,
    	    COLUMN_NAME_TELE_LO_SCORE,
    	    COLUMN_NAME_TELE_HI_MISS,
    	    COLUMN_NAME_TELE_LO_MISS,
    	    COLUMN_NAME_TRUSS_TOSS,
    	    COLUMN_NAME_TRUSS_MISS,
    	    COLUMN_NAME_TOSS_CATCH,
    	    COLUMN_NAME_TOSS_MISS,
    	    COLUMN_NAME_ASSIST_RED,
    	    COLUMN_NAME_ASSIST_WHITE,
    	    COLUMN_NAME_ASSIST_BLUE,
    	    COLUMN_NAME_SHORT_PASS_SUCCESS,
    	    COLUMN_NAME_SHORT_PASS_MISS,
    	    COLUMN_NAME_LONG_PASS_SUCCESS,
    	    COLUMN_NAME_LONG_PASS_MISS,
    	    COLUMN_NAME_DEFEND_RED,
    	    COLUMN_NAME_DEFEND_WHITE,
    	    COLUMN_NAME_DEFEND_BLUE,
    	    COLUMN_NAME_DEFEND_GOAL,
    	    COLUMN_NAME_BROKE_DOWN,
    	    COLUMN_NAME_NO_MOVE,
    	    COLUMN_NAME_LOST_CONNECTION,
    	    COLUMN_NAME_ROLE_SHOOTER,
    	    COLUMN_NAME_ROLE_DEFENDER,
    	    COLUMN_NAME_ROLE_PASSER,
    	    COLUMN_NAME_ROLE_CATCHER,
    	    COLUMN_NAME_ROLE_GOALIE,
    	    COLUMN_NAME_START_LOCATION,
    	    COLUMN_NAME_BALL_CONTROL_GROUND_PICKUP,
    	    COLUMN_NAME_BALL_CONTROL_HUMAN_LOAD,
    	    COLUMN_NAME_BALL_CONTROL_HI_TO_LO,
    	    COLUMN_NAME_BALL_CONTROL_LO_TO_HI,
    	    COLUMN_NAME_BALL_CONTROL_HI_TO_HI,
    	    COLUMN_NAME_BALL_CONTROL_LO_TO_LO,
    	    COLUMN_NAME_TEAM_MATCH_NOTES
    };
    
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DBAdapter.DATABASE_NAME, null, DBAdapter.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	FTSUtilities.printToConsole("TeamMatchDBAdapter::DatabaseHelper::onUpgrade : running onUpgrade\n");
        }
        
        @Override
    	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param ctx
     *            the Context within which to work
     */
    public TeamMatchDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the FirstTeamScouter database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException
     *             if the database could be neither opened or created
     */
    public TeamMatchDBAdapter open() throws SQLException {
    	if(this.dbNotOpen()) {
    		if(this.mDbHelper == null) {
    			this.mDbHelper = new DatabaseHelper(this.mCtx);
    		}

    		try {
    			FTSUtilities.printToConsole("TeamMatchDBAdapter::open : GETTING WRITABLE DB\n");
    			this.mDb = this.mDbHelper.getWritableDatabase();
    		}
    		catch (SQLException e) {
    			FTSUtilities.printToConsole("TeamMatchDBAdapter::open : SQLException\n");
    			this.mDb = null;
    		}
    	} else {
    		FTSUtilities.printToConsole("TeamMatchDBAdapter::open : DB ALREADY OPEN\n");
    	}
    	return this;
    }
    
    public boolean dbNotOpen() {
    	if(this.mDb == null) {
    		return true;
    	} else {
    		return !this.mDb.isOpen();
    	}
    }

    /**
     * close return type: void
     */
    public void close() {
        this.mDbHelper.close();
        this.mDb = null;
    }

    /**
     * Create a new entry. If the entry is successfully created return the new
     * rowId for that entry, otherwise return a -1 to indicate failure.
     * 
     * @param team_match_id
     * @param team_id
     * @param match_id
     * @param auto_score
     * @param tele_score
     * @param other_score
     * @param offensive_rating
     * @param defensive_rating
     * @return rowId or -1 if failed
     */
    public long createTeamMatch(int team_match_id, String team_id, String match_id) {
        ContentValues args = new ContentValues();
        args.put(COLUMN_NAME_TEAM_MATCH_ID, team_match_id);
        args.put(COLUMN_NAME_TEAM_ID, team_id);
        args.put(COLUMN_NAME_MATCH_ID, match_id);
        args.put(COLUMN_NAME_MATCH_DATA_SAVED, Boolean.FALSE.toString());
        return this.mDb.insert(TABLE_NAME, null, args);
        /*

         */
    }

    /**
     * Update the entry.
     * 
     * @param team_id
     * @param team_number
     * @param team_name
     * @param team_location
     * @param num_team_members
     * @return true if the entry was successfully updated, false otherwise
     */
    public boolean updateTeamMatch(int team_match_id, String team_id, String match_id, String tmNotes, Hashtable<String, Boolean> boolVals, Hashtable<String, Integer> intVals) {
    		//int auto_score, int tele_score, int other_score, 
    		//int offensive_rating, int defensive_rating){
    	FTSUtilities.printToConsole("TeamMatchDBAdapter::updateTeamMatch\n");
        ContentValues args = new ContentValues();
        args.put(COLUMN_NAME_TEAM_MATCH_ID, team_match_id);
        args.put(COLUMN_NAME_TEAM_ID, team_id);
        args.put(COLUMN_NAME_MATCH_ID, match_id);
        args.put(COLUMN_NAME_TEAM_MATCH_NOTES, tmNotes);
//        args.put(COLUMN_NAME_MATCH_DATA_SAVED, Boolean.toString(tmSaved));
//        args.put(COLUMN_NAME_AUTO_MOVE, Boolean.toString(move));
//        args.put(COLUMN_NAME_BROKE_DOWN, Boolean.toString(move));
//        args.put(COLUMN_NAME_NO_MOVE, Boolean.toString(move));
//        args.put(COLUMN_NAME_LOST_CONNECTION, Boolean.toString(move));
        
        Enumeration<String> boolKeys = boolVals.keys();
        while(boolKeys.hasMoreElements()) {
        	String key = boolKeys.nextElement();
        	args.put(key, Boolean.toString(boolVals.get(key)));
        }

        Enumeration<String> intKeys = intVals.keys();
        while(intKeys.hasMoreElements()) {
        	String key = intKeys.nextElement();
        	args.put(key, intVals.get(key));
        }
        
        String WHERE = TeamMatchDBAdapter.COLUMN_NAME_TEAM_MATCH_ID + "=" + team_match_id;
        //WHERE += " AND " + TeamMatchDBAdapter.COLUMN_NAME_MATCH_ID + "=" + match_id;
        return this.mDb.update(TABLE_NAME, args, WHERE, null) >0; 
    }

    /**
     * Return a Cursor over the list of all entries in the database
     * 
     * @return Cursor over all Match Data entries
     */
    public Cursor getAllTeamMatches() {

        return this.mDb.query(TABLE_NAME, new String[] { _ID,
        		COLUMN_NAME_TEAM_MATCH_ID, COLUMN_NAME_TEAM_ID, COLUMN_NAME_MATCH_ID,
        		COLUMN_NAME_AUTO_SCORE, COLUMN_NAME_TELE_SCORE,
        		}, null, null, null, null, null);
    }

    /**
     * Return a Cursor over the list of all unique matches in the database
     * 
     * @return Cursor over all unique Match numbers
     */
    public Cursor getAllMatchNumbers() {
        return this.mDb.query(TABLE_NAME, new String[] { _ID,
        		COLUMN_NAME_TEAM_MATCH_ID, COLUMN_NAME_MATCH_ID},
        		null, null, COLUMN_NAME_MATCH_ID, null, null);
    }

    /**
     * Return a Cursor over the list of teams for a given match in the database
     * 
     * @return Cursor over all Team IDs for a given match number
     */
    public Cursor getTeamNumbersforMatch(String matchNum) {
        return this.mDb.query(TABLE_NAME, new String[] { _ID,
        		COLUMN_NAME_TEAM_MATCH_ID, COLUMN_NAME_TEAM_ID},
        		COLUMN_NAME_MATCH_ID + "=" + matchNum, null, null, null, null);
    }
    
    public Cursor getMatchesForTeam(String teamNum) {
    	String selection = COLUMN_NAME_TEAM_ID + "=" + teamNum;
    	Cursor mCursor = this.mDb.query(TABLE_NAME, this.allColumnNames, selection, null, null, null, COLUMN_NAME_MATCH_ID);
    	return mCursor;
    }

    /**
     * Return a Cursor positioned at the entry that matches the given rowId
     * @param rowId
     * @return Cursor positioned to matching entry, if found
     * @throws SQLException if entry could not be found/retrieved
     */
    public Cursor getTeamMatch(long rowId) throws SQLException {

        Cursor mCursor = this.mDb.query(true, TABLE_NAME, this.allColumnNames,
        		_ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Return a Cursor positioned at the entry that matches the given TeamMatchID
     * @param tmID
     * @return Cursor positioned to matching entry, if found
     * @throws SQLException if entry could not be found/retrieved
     */
    public Cursor getTeamMatch(int tmID) throws SQLException {

        Cursor mCursor = this.mDb.query(true, TABLE_NAME, this.allColumnNames,
        		COLUMN_NAME_TEAM_MATCH_ID + "=" + tmID, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
    
    /**
     * Delete the entry with the given rowId
     * 
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteTeamMatch(long rowId) {

        return this.mDb.delete(TABLE_NAME, _ID + "=" + rowId, null) > 0;
    }
    
    void deleteAllData()
    {
        mDb.delete(TABLE_NAME, null, null);
    }
    
    public void populateTestData() {
    	FTSUtilities.printToConsole("TeamMatchDBAdapter::populateTestData\n");
    	deleteAllData();
    	
    	Set<Integer> teamNums = FTSUtilities.getTestTeamNumbers(); // {1425, 1520, 2929, 1114, 500, 600, 700, 800, 900, 1000};
    	int teamOffset = 0;
    	for(int matchNum = 1; matchNum <= 10; matchNum++) {
	    	for(int i = 0; i < 6; i++) {
	    		int id = (100 * teamOffset) + (matchNum * i) + matchNum;
	    		int teamIndex = (i + teamOffset) % teamNums.size();
	    		this.createTeamMatch(id, String.valueOf(teamNums.toArray()[teamIndex]), String.valueOf(matchNum));
	    	}
	    	if(++teamOffset >= teamNums.size()) {
	    		teamOffset = 0;
	    	}
    	}
    }
}
