package app.com.thetechnocafe.eventos.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.com.thetechnocafe.eventos.Models.EventsModel;

/**
 * Created by gurleensethi on 15/10/16.
 */

public class EventsDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "eventos_database";
    private static final int VERSION = 1;
    private static final String TAG = "EventsDatabaseHelper";

    private static final String EVENTS_TABLE = "events";
    private static final String EVENT_COLUMN_ID = "id";
    private static final String EVENT_COLUMN_TITLE = "title";
    private static final String EVENT_COLUMN_DESCRIPTION = "description";
    private static final String EVENT_COLUMN_DATE = "date";
    private static final String EVENT_COLUMN_VENUE = "venue";
    private static final String EVENT_COLUMN_AVATAR_ID = "avatar_id";
    private static final String EVENT_COLUMN_IMAGE = "image";


    public EventsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + EVENTS_TABLE + " (" +
                EVENT_COLUMN_ID + " VARCHAR PRIMARY KEY, " +
                EVENT_COLUMN_TITLE + " VARCHAR, " +
                EVENT_COLUMN_DESCRIPTION + " VARCHAR, " +
                EVENT_COLUMN_DATE + " VARCHAR, " +
                EVENT_COLUMN_VENUE + " VARCHAR, " +
                EVENT_COLUMN_AVATAR_ID + " INTEGER, " +
                EVENT_COLUMN_IMAGE + " VARCHAR " +
                ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Insert New events data into Events Table
     * Take EventModel as input , extracts data from it and inerts into table
     */
    public void insertNewEvent(EventsModel event) {
        //Get database
        SQLiteDatabase database = getWritableDatabase();

        //Create content values and add data
        ContentValues contentValues = new ContentValues();
        contentValues.put(EVENT_COLUMN_ID, event.getId());
        contentValues.put(EVENT_COLUMN_TITLE, event.getTitle());
        contentValues.put(EVENT_COLUMN_DESCRIPTION, event.getDescription());
        contentValues.put(EVENT_COLUMN_VENUE, event.getVenue());
        contentValues.put(EVENT_COLUMN_IMAGE, event.getImage());
        contentValues.put(EVENT_COLUMN_DATE, event.getDate().toString());


        database.insert(EVENTS_TABLE, null, contentValues);
    }

    /**
     * Get a list of events stores in the database
     * Return a List of EventsModel objects
     */
    public List<EventsModel> getEventsList() {
        //Get Database
        SQLiteDatabase database = getReadableDatabase();

        //Create new EventsModel list
        List<EventsModel> eventsList = new ArrayList<>();

        //Set up the query
        String sql = "SELECT * FROM " + EVENTS_TABLE;

        //Run the query and obtain cursor
        Cursor cursor = database.rawQuery(sql, null);

        //Extract the values while looping over cursor
        while (cursor.moveToNext()) {
            EventsModel event = new EventsModel();

            event.setId(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_ID)));
            event.setVenue(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_VENUE)));
            event.setDate(new Date(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_DATE))));
            event.setAvatarId(cursor.getInt(cursor.getColumnIndex(EVENT_COLUMN_AVATAR_ID)));
            event.setImage(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_IMAGE)));
            event.setTitle(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_TITLE)));
            event.setDescription(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_DESCRIPTION)));

            //Add event to list
            eventsList.add(event);
        }
        //Close cursor after use
        cursor.close();

        return eventsList;
    }

    //Return the particular event corresponding to the id
    public EventsModel getEvent(String id) {
        //Check if event id is null
        if (id == null) {
            return null;
        }

        EventsModel eventsModel = new EventsModel();

        SQLiteDatabase database = getReadableDatabase();

        String sql = "SELECT * FROM " + EVENTS_TABLE + " WHERE " + EVENT_COLUMN_ID + " = '" + id + "'";

        //Run query
        Cursor cursor = database.rawQuery(sql, null);

        //Traverse the cursor
        while (cursor.moveToNext()) {
            eventsModel.setId(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_ID)));
            eventsModel.setTitle(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_TITLE)));
            eventsModel.setDescription(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_DESCRIPTION)));
            eventsModel.setAvatarId(cursor.getInt(cursor.getColumnIndex(EVENT_COLUMN_AVATAR_ID)));
            eventsModel.setImage(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_VENUE)));
            eventsModel.setDate(new Date());
        }

        //Close the cursor
        cursor.close();

        return eventsModel;
    }

    //Check if event is already in database
    public boolean doesEventAlreadyExists(String id) {
        //If event id is wrong return true
        if (id == null) {
            return true;
        }

        String sql = "SELECT * FROM " + EVENTS_TABLE + " WHERE " + EVENT_COLUMN_ID + " = '" + id + "'";

        //Execute query
        Cursor cursor = getReadableDatabase().rawQuery(sql, null);

        if (cursor.getCount() > 0) {
            //Close cursor
            cursor.close();

            return true;
        }

        //Close cursor
        cursor.close();

        return false;
    }
}
