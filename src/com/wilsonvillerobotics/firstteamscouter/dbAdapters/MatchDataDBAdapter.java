package com.wilsonvillerobotics.firstteamscouter.dbAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class MatchDataDBAdapter implements BaseColumns {
	public static final String TABLE_NAME = "match_data";
    public static final String COLUMN_NAME_MATCH_DATA_ID              = "match_id";
    public static final String COLUMN_NAME_MATCH_NUMBER               = "match_number";
    public static final String COLUMN_NAME_MATCH_LOCATION             = "match_location";

    /*
    public static final String COLUMN_NAME_RED_TEAM_ONE_ID            = "red_team_one_id";
    public static final String COLUMN_NAME_RED_TEAM_TWO_ID            = "red_team_two_id";
    public static final String COLUMN_NAME_RED_TEAM_THREE_ID          = "red_team_three_id";
    public static final String COLUMN_NAME_BLUE_TEAM_ONE_ID           = "blue_team_one_id";
    public static final String COLUMN_NAME_BLUE_TEAM_TWO_ID           = "blue_team_two_id";
    public static final String COLUMN_NAME_BLUE_TEAM_THREE_ID         = "blue_team_three_id";
	*/
    
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
    public MatchDataDBAdapter(Context ctx) {
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
    public MatchDataDBAdapter open() throws SQLException {
        this.mDbHelper = new DatabaseHelper(this.mCtx);
        this.mDb = this.mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * close return type: void
     */
    public void close() {
        this.mDbHelper.close();
    }

    /**
     * Create a new entry. If the entry is successfully created return the new
     * rowId for that entry, otherwise return a -1 to indicate failure.
     * 
     * @param id
     * @param name
     * @param location
     * @return rowId or -1 if failed
     */

    public long createMatchData(int id, int match_id, int red_one_id, int red_two_id, int red_three_id,
    		int blue_one_id, int blue_two_id, int blue_three_id){
        ContentValues initialValues = new ContentValues();
        initialValues.put(COLUMN_NAME_MATCH_DATA_ID, id);
        initialValues.put(COLUMN_NAME_MATCH_NUMBER, match_id);
        /*
        initialValues.put(COLUMN_NAME_RED_TEAM_ONE_ID, red_one_id);
        initialValues.put(COLUMN_NAME_RED_TEAM_TWO_ID, red_two_id);
        initialValues.put(COLUMN_NAME_RED_TEAM_THREE_ID, red_three_id);
        initialValues.put(COLUMN_NAME_BLUE_TEAM_ONE_ID, blue_one_id);
        initialValues.put(COLUMN_NAME_BLUE_TEAM_TWO_ID, blue_two_id);
        initialValues.put(COLUMN_NAME_BLUE_TEAM_THREE_ID, blue_three_id);
        */
        return this.mDb.insert(TABLE_NAME, null, initialValues);
    }

    /**
     * Delete the entry with the given rowId
     * 
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteMatchDataEntry(long rowId) {

        return this.mDb.delete(TABLE_NAME, _ID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all entries in the database
     * 
     * @return Cursor over all Match Data entries
     */
    public Cursor getAllMatchDataEntries() {

        return this.mDb.query(TABLE_NAME, new String[] { _ID,
        		COLUMN_NAME_MATCH_DATA_ID, COLUMN_NAME_MATCH_NUMBER, COLUMN_NAME_MATCH_LOCATION, 
        		//COLUMN_NAME_RED_TEAM_ONE_ID, COLUMN_NAME_RED_TEAM_TWO_ID, COLUMN_NAME_RED_TEAM_THREE_ID,
        		//COLUMN_NAME_BLUE_TEAM_ONE_ID, COLUMN_NAME_BLUE_TEAM_TWO_ID, COLUMN_NAME_BLUE_TEAM_THREE_ID
        		}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the entry that matches the given rowId
     * @param rowId
     * @return Cursor positioned to matching entry, if found
     * @throws SQLException if entry could not be found/retrieved
     */
    public Cursor getMatchDataEntry(long rowId) throws SQLException {

        Cursor mCursor =

        this.mDb.query(true, TABLE_NAME, new String[] { _ID, 
        		COLUMN_NAME_MATCH_DATA_ID, COLUMN_NAME_MATCH_NUMBER, COLUMN_NAME_MATCH_LOCATION, 
        		//COLUMN_NAME_RED_TEAM_ONE_ID, COLUMN_NAME_RED_TEAM_TWO_ID, COLUMN_NAME_RED_TEAM_THREE_ID,
        		//COLUMN_NAME_BLUE_TEAM_ONE_ID, COLUMN_NAME_BLUE_TEAM_TWO_ID, COLUMN_NAME_BLUE_TEAM_THREE_ID 
        		}, _ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * Update the entry.
     * 
     * @param rowId
     * @param competition_match_id
     * @param competition_id
     * @param match_id
     * @return true if the entry was successfully updated, false otherwise
     */
    public boolean updateMatchDataEntry(int rowId, int id, int match_id, int red_one_id, int red_two_id, int red_three_id,
    		int blue_one_id, int blue_two_id, int blue_three_id){
        ContentValues args = new ContentValues();
    	args.put(COLUMN_NAME_MATCH_DATA_ID, id);
    	args.put(COLUMN_NAME_MATCH_NUMBER, match_id);
    	/*
    	args.put(COLUMN_NAME_RED_TEAM_ONE_ID, red_one_id);
    	args.put(COLUMN_NAME_RED_TEAM_TWO_ID, red_two_id);
    	args.put(COLUMN_NAME_RED_TEAM_THREE_ID, red_three_id);
    	args.put(COLUMN_NAME_BLUE_TEAM_ONE_ID, blue_one_id);
    	args.put(COLUMN_NAME_BLUE_TEAM_TWO_ID, blue_two_id);
    	args.put(COLUMN_NAME_BLUE_TEAM_THREE_ID, blue_three_id);
        */
        return this.mDb.update(TABLE_NAME, args, _ID + "=" + rowId, null) >0; 
    }

}
