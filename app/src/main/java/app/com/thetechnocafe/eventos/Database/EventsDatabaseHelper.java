package app.com.thetechnocafe.eventos.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import app.com.thetechnocafe.eventos.Models.EventsModel;

/**
 * Created by gurleensethi on 15/10/16.
 */

public class EventsDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "events_database";
    private static final int VERSION = 1;

    private static final String TABLE_NAME = "events";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_VENUE = "venue";
    private static final String COLUMN_AVATAR_ID = "avatar_id";
    private static final String COLUMN_IMAGE = "id";


    public EventsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " VARCHAR PRIMARY KEY, " +
                COLUMN_TITLE + " VARCHAR, " +
                COLUMN_DESCRIPTION + " VARCHAR, " +
                COLUMN_DATE + " VARCHAR, " +
                COLUMN_VENUE + " VARCHAR, " +
                COLUMN_AVATAR_ID + " INTEGER, " +
                COLUMN_IMAGE + " VARCHAR, " +
                ");";
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int insertEvent(EventsModel event) {

    }
}
