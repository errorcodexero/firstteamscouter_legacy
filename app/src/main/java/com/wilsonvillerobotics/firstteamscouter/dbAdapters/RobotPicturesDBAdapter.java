package com.wilsonvillerobotics.firstteamscouter.dbAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class RobotPicturesDBAdapter implements BaseColumns {
	public static final String TABLE_NAME = "robot_pictures";
    public static final String COLUMN_NAME_ROBOT_PICTURE_ID = "robot_picture_id";
    public static final String COLUMN_NAME_ROBOT_ID = "robot_id";
    public static final String COLUMN_NAME_PICTURE_ID = "picture_id";

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
    public RobotPicturesDBAdapter(Context ctx) {
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
    public RobotPicturesDBAdapter open() throws SQLException {
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
     * @param robot_picture_id
     * @param robot_id
     * @param picture_id
     * @return rowId or -1 if failed
     */
    public long createRobotPicture(int robot_picture_id, int robot_id, int picture_id){
        ContentValues args = new ContentValues();
        args.put(COLUMN_NAME_ROBOT_PICTURE_ID, robot_picture_id);
        args.put(COLUMN_NAME_ROBOT_ID, robot_id);
        args.put(COLUMN_NAME_PICTURE_ID, picture_id);
        return this.mDb.insert(TABLE_NAME, null, args);
    }

    /**
     * Update the entry.
     * 
     * @param rowId
     * @param robot_picture_id
     * @param robot_id
     * @param picture_id
     * @return true if the entry was successfully updated, false otherwise
     */
    public boolean updateRobotPicture(int rowId, int robot_picture_id, int robot_id, int picture_id){
        ContentValues args = new ContentValues();
        args.put(COLUMN_NAME_ROBOT_PICTURE_ID, robot_picture_id);
        args.put(COLUMN_NAME_ROBOT_ID, robot_id);
        args.put(COLUMN_NAME_PICTURE_ID, picture_id);
        return this.mDb.update(TABLE_NAME, args, _ID + "=" + rowId, null) >0; 
    }

    /**
     * Delete the entry with the given rowId
     * 
     * @param rowId
     * @return true if deleted, false otherwise
     */
    public boolean deleteRobotPicture(long rowId) {

        return this.mDb.delete(TABLE_NAME, _ID + "=" + rowId, null) > 0;
    }

    /**
     * Return a Cursor over the list of all entries in the database
     * 
     * @return Cursor over all Match Data entries
     */
    public Cursor getAllRobotPictures() {

        return this.mDb.query(TABLE_NAME, new String[] { _ID,
        		COLUMN_NAME_ROBOT_PICTURE_ID, COLUMN_NAME_ROBOT_ID, COLUMN_NAME_PICTURE_ID
        		}, null, null, null, null, null);
    }

    /**
     * Return a Cursor positioned at the entry that matches the given rowId
     * @param rowId
     * @return Cursor positioned to matching entry, if found
     * @throws SQLException if entry could not be found/retrieved
     */
    public Cursor getRobotPicture(long rowId) throws SQLException {

        Cursor mCursor =

        this.mDb.query(true, TABLE_NAME, new String[] { _ID, 
        		COLUMN_NAME_ROBOT_PICTURE_ID, COLUMN_NAME_ROBOT_ID, COLUMN_NAME_PICTURE_ID
        		}, _ID + "=" + rowId, null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }
}
