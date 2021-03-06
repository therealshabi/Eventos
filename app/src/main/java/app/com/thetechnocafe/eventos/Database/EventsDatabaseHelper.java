package app.com.thetechnocafe.eventos.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import app.com.thetechnocafe.eventos.Models.CommentsModel;
import app.com.thetechnocafe.eventos.Models.ContactsModel;
import app.com.thetechnocafe.eventos.Models.EventsModel;
import app.com.thetechnocafe.eventos.Models.LinksModel;

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
    private static final String EVENT_COLUMN_REQUIREMENTS = "requirements";
    private static final String EVENT_COLOUMN_SUBMITTED_BY = "submitted_by";
    private static final String EVENT_COLOUMN_VERIFIED = "verified";
    private static final String EVENT_COLOUMN_PEOPLE_INTERESTED = "people_interested";
    private static final String EVENT_COLOUMN_OUTSIDE_EVENT = "outside_event";

    private static final String OUTSIDE_EVENTS_TABLE = "outside_events";
    private static final String OUTSIDE_EVENT_COLUMN_ID = "id";
    private static final String OUTSIDE_EVENT_COLUMN_TITLE = "title";
    private static final String OUTSIDE_EVENT_COLUMN_DESCRIPTION = "description";
    private static final String OUTSIDE_EVENT_COLUMN_DATE = "date";
    private static final String OUTSIDE_EVENT_COLUMN_VENUE = "venue";
    private static final String OUTSIDE_EVENT_COLUMN_AVATAR_ID = "avatar_id";
    private static final String OUTSIDE_EVENT_COLUMN_IMAGE = "image";
    private static final String OUTSIDE_EVENT_COLUMN_REQUIREMENTS = "requirements";
    private static final String OUTSIDE_EVENT_COLOUMN_SUBMITTED_BY = "submitted_by";
    private static final String OUTSIDE_EVENT_COLOUMN_VERIFIED = "verified";
    private static final String OUTSIDE_EVENT_COLOUMN_PEOPLE_INTERESTED = "people_interested";
    private static final String OUTSIDE_EVENT_COLOUMN_OUTSIDE_EVENT = "outside_event";

    private static final String CONTACTS_TABLE = "event_contacts";
    private static final String CONTACTS_EVENT_ID = "event_id";
    private static final String CONTACTS_PHONE_NUMBER = "phone";
    private static final String CONTACTS_EMAIL_ID = "email";
    private static final String CONTACTS_NAME = "name";

    private static final String LINKS_TABLE = "links";
    private static final String LINKS_NAME = "name";
    private static final String LINKS_EVENT_ID = "event_id";
    private static final String LINKS_ADDRESS = "address";

    private static final String FAV_EVENTS_TABLE = "FavEvents";
    private static final String FAV_EVENT_COLUMN_ID = "id";

    private static final String COMMENTS_TABLE = "comments";
    private static final String COMMENTS_COLUMN_COMMENT = "comment";
    private static final String COMMENTS_COLUMN_TIME = "time";
    private static final String COMMENTS_COLUMN_FROM = "user";
    private static final String COMMENTS_COLUMN_EVENT_ID = "event_id";

    private static final String SUBMITTED_EVENTS_TABLE = "submitted_events";
    private static final String SUBMITTED_EVENT_COLUMN_ID = "id";
    private static final String SUBMITTED_EVENT_COLUMN_TITLE = "title";
    private static final String SUBMITTED_EVENT_COLUMN_DESCRIPTION = "description";
    private static final String SUBMITTED_EVENT_COLUMN_DATE = "date";
    private static final String SUBMITTED_EVENT_COLUMN_VENUE = "venue";
    private static final String SUBMITTED_EVENT_COLUMN_AVATAR_ID = "avatar_id";
    private static final String SUBMITTED_EVENT_COLUMN_IMAGE = "image";
    private static final String SUBMITTED_EVENT_COLUMN_REQUIREMENTS = "requirements";
    private static final String SUBMITTED_EVENT_COLOUMN_SUBMITTED_BY = "submitted_by";
    private static final String SUBMITTED_EVENT_COLOUMN_VERIFIED = "verified";
    private static final String SUBMITTED_EVENT_COLOUMN_PEOPLE_INTERESTED = "people_interested";
    private static final String SUBMITTED_EVENT_COLOUMN_OUTSIDE_EVENT = "outside_event";

    public EventsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Prepare sql statements
        String eventsTableSQL = "CREATE TABLE " + EVENTS_TABLE + " (" +
                EVENT_COLUMN_ID + " VARCHAR PRIMARY KEY, " +
                EVENT_COLUMN_TITLE + " VARCHAR, " +
                EVENT_COLUMN_DESCRIPTION + " VARCHAR, " +
                EVENT_COLUMN_DATE + " VARCHAR, " +
                EVENT_COLUMN_VENUE + " VARCHAR, " +
                EVENT_COLUMN_AVATAR_ID + " VARCHAR, " +
                EVENT_COLUMN_IMAGE + " VARCHAR, " +
                EVENT_COLUMN_REQUIREMENTS + " VARCHAR ," +
                EVENT_COLOUMN_SUBMITTED_BY + " VARCHAR ," +
                EVENT_COLOUMN_VERIFIED + " INTEGER DEFAULT 0 ," +
                EVENT_COLOUMN_PEOPLE_INTERESTED + " INTEGER , " +
                EVENT_COLOUMN_OUTSIDE_EVENT + " INTEGER DEFAULT 0" +
                ");";

        String OutsideEventsTableSQL = "CREATE TABLE " + OUTSIDE_EVENTS_TABLE + " (" +
                OUTSIDE_EVENT_COLUMN_ID + " VARCHAR PRIMARY KEY, " +
                OUTSIDE_EVENT_COLUMN_TITLE + " VARCHAR, " +
                OUTSIDE_EVENT_COLUMN_DESCRIPTION + " VARCHAR, " +
                OUTSIDE_EVENT_COLUMN_DATE + " VARCHAR, " +
                OUTSIDE_EVENT_COLUMN_VENUE + " VARCHAR, " +
                OUTSIDE_EVENT_COLUMN_AVATAR_ID + " VARCHAR, " +
                OUTSIDE_EVENT_COLUMN_IMAGE + " VARCHAR, " +
                OUTSIDE_EVENT_COLUMN_REQUIREMENTS + " VARCHAR ," +
                OUTSIDE_EVENT_COLOUMN_SUBMITTED_BY + " VARCHAR ," +
                OUTSIDE_EVENT_COLOUMN_VERIFIED + " INTEGER DEFAULT 0 ," +
                OUTSIDE_EVENT_COLOUMN_PEOPLE_INTERESTED + " INTEGER , " +
                OUTSIDE_EVENT_COLOUMN_OUTSIDE_EVENT + " INTEGER DEFAULT 0" +
                ");";

        String favEventsTableSQL = "CREATE TABLE " + FAV_EVENTS_TABLE + " (" +
                FAV_EVENT_COLUMN_ID + " VARCHAR PRIMARY KEY " +
                ");";

        String contactsTableSQL = "CREATE TABLE " + CONTACTS_TABLE + " (" +
                CONTACTS_EVENT_ID + " VARCHAR, " +
                CONTACTS_NAME + " VARCHAR, " +
                CONTACTS_PHONE_NUMBER + " VARCHAR, " +
                CONTACTS_EMAIL_ID + " VARCHAR" +
                ");";

        String linksTableSQL = "CREATE TABLE " + LINKS_TABLE + " (" +
                LINKS_EVENT_ID + " VARCHAR, " +
                LINKS_NAME + " VARCHAR, " +
                LINKS_ADDRESS + " VARCHAR" +
                ");";

        String commentsTableSQL = "CREATE TABLE " + COMMENTS_TABLE + " (" +
                COMMENTS_COLUMN_COMMENT + " VARCHAR, " +
                COMMENTS_COLUMN_TIME + " VARCHAR, " +
                COMMENTS_COLUMN_EVENT_ID + " VARCHAR, " +
                COMMENTS_COLUMN_FROM + " VARCHAR);";

        String submittedEventsTableSQL = "CREATE TABLE " + SUBMITTED_EVENTS_TABLE + " (" +
                SUBMITTED_EVENT_COLUMN_ID + " VARCHAR PRIMARY KEY, " +
                SUBMITTED_EVENT_COLUMN_TITLE + " VARCHAR, " +
                SUBMITTED_EVENT_COLUMN_DESCRIPTION + " VARCHAR, " +
                SUBMITTED_EVENT_COLUMN_DATE + " VARCHAR, " +
                SUBMITTED_EVENT_COLUMN_VENUE + " VARCHAR, " +
                SUBMITTED_EVENT_COLUMN_AVATAR_ID + " VARCHAR, " +
                SUBMITTED_EVENT_COLUMN_IMAGE + " VARCHAR, " +
                SUBMITTED_EVENT_COLUMN_REQUIREMENTS + " VARCHAR ," +
                SUBMITTED_EVENT_COLOUMN_SUBMITTED_BY + " VARCHAR ," +
                SUBMITTED_EVENT_COLOUMN_VERIFIED + " INTEGER DEFAULT 0 ," +
                SUBMITTED_EVENT_COLOUMN_PEOPLE_INTERESTED + " INTEGER , " +
                SUBMITTED_EVENT_COLOUMN_OUTSIDE_EVENT + " INTEGER DEFAULT 0" +
                ");";

        //Run the queries to create tables
        db.execSQL(eventsTableSQL);
        db.execSQL(contactsTableSQL);
        db.execSQL(linksTableSQL);
        db.execSQL(favEventsTableSQL);
        db.execSQL(commentsTableSQL);
        db.execSQL(submittedEventsTableSQL);
        db.execSQL(OutsideEventsTableSQL);
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
        contentValues.put(EVENT_COLUMN_DATE, String.valueOf(event.getDate().getTime()));
        contentValues.put(EVENT_COLUMN_REQUIREMENTS, event.getRequirements());
        contentValues.put(EVENT_COLOUMN_SUBMITTED_BY, event.getSubmittedBy());
        contentValues.put(EVENT_COLOUMN_PEOPLE_INTERESTED, event.getNumOfPeopleInterested());
        contentValues.put(EVENT_COLUMN_AVATAR_ID, event.getAvatar_id());
        if (event.getVerified() == true) {

            if (event.getOutsideEvent()) {
                contentValues.put(EVENT_COLOUMN_OUTSIDE_EVENT, 1);
            } else {
                contentValues.put(EVENT_COLOUMN_OUTSIDE_EVENT, 0);
            }

            if (event.getVerified()) {
                contentValues.put(EVENT_COLOUMN_VERIFIED, 1);
            } else {
                contentValues.put(EVENT_COLOUMN_VERIFIED, 0);
            }

            database.insert(EVENTS_TABLE, null, contentValues);
        }
    }


    /**
     * Insert New Outside events data into Events Table
     * Take EventModel as input , extracts data from it and inerts into table
     */
    public void insertNewOutsideEvent(EventsModel event) {
        //Get database
        SQLiteDatabase database = getWritableDatabase();

        //Create content values and add data
        ContentValues contentValues = new ContentValues();
        contentValues.put(OUTSIDE_EVENT_COLUMN_ID, event.getId());
        contentValues.put(OUTSIDE_EVENT_COLUMN_TITLE, event.getTitle());
        contentValues.put(OUTSIDE_EVENT_COLUMN_DESCRIPTION, event.getDescription());
        contentValues.put(OUTSIDE_EVENT_COLUMN_VENUE, event.getVenue());
        contentValues.put(OUTSIDE_EVENT_COLUMN_IMAGE, event.getImage());
        contentValues.put(OUTSIDE_EVENT_COLUMN_DATE, String.valueOf(event.getDate().getTime()));
        contentValues.put(OUTSIDE_EVENT_COLUMN_REQUIREMENTS, event.getRequirements());
        contentValues.put(OUTSIDE_EVENT_COLOUMN_SUBMITTED_BY, event.getSubmittedBy());
        contentValues.put(OUTSIDE_EVENT_COLOUMN_PEOPLE_INTERESTED, event.getNumOfPeopleInterested());
        contentValues.put(OUTSIDE_EVENT_COLUMN_AVATAR_ID, event.getAvatar_id());

        if (event.getOutsideEvent()) {
            contentValues.put(OUTSIDE_EVENT_COLOUMN_OUTSIDE_EVENT, 1);
        } else {
            contentValues.put(OUTSIDE_EVENT_COLOUMN_OUTSIDE_EVENT, 0);
        }

        if (event.getVerified()) {
            contentValues.put(OUTSIDE_EVENT_COLOUMN_VERIFIED, 1);
        } else {
            contentValues.put(OUTSIDE_EVENT_COLOUMN_VERIFIED, 0);
        }

        database.insert(OUTSIDE_EVENTS_TABLE, null, contentValues);
    }

    /**
     * Insert New Submitted Events By The User
     */

    public void insertNewSubmittedEvent(EventsModel event) {
        //Get database
        SQLiteDatabase database = getWritableDatabase();

        //Create content values and add data
        ContentValues contentValues = new ContentValues();
        contentValues.put(SUBMITTED_EVENT_COLUMN_ID, event.getId());
        contentValues.put(SUBMITTED_EVENT_COLUMN_TITLE, event.getTitle());
        contentValues.put(SUBMITTED_EVENT_COLUMN_DESCRIPTION, event.getDescription());
        contentValues.put(SUBMITTED_EVENT_COLUMN_VENUE, event.getVenue());
        contentValues.put(SUBMITTED_EVENT_COLUMN_IMAGE, event.getImage());
        contentValues.put(SUBMITTED_EVENT_COLUMN_DATE, String.valueOf(event.getDate().getTime()));
        contentValues.put(SUBMITTED_EVENT_COLUMN_REQUIREMENTS, event.getRequirements());
        contentValues.put(SUBMITTED_EVENT_COLOUMN_SUBMITTED_BY, event.getSubmittedBy());
        contentValues.put(SUBMITTED_EVENT_COLOUMN_PEOPLE_INTERESTED, event.getNumOfPeopleInterested());
        contentValues.put(SUBMITTED_EVENT_COLUMN_AVATAR_ID, event.getAvatar_id());


        if (event.getOutsideEvent()) {
            contentValues.put(SUBMITTED_EVENT_COLOUMN_OUTSIDE_EVENT, 1);
        } else {
            contentValues.put(SUBMITTED_EVENT_COLOUMN_OUTSIDE_EVENT, 0);
        }

        if (event.getVerified()) {
            contentValues.put(SUBMITTED_EVENT_COLOUMN_VERIFIED, 1);
        } else {
            contentValues.put(SUBMITTED_EVENT_COLOUMN_VERIFIED, 0);
        }

        database.insert(SUBMITTED_EVENTS_TABLE, null, contentValues);

    }

    public void insertNewFavEvent(EventsModel event) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FAV_EVENT_COLUMN_ID, event.getId());

        database.insert(FAV_EVENTS_TABLE, null, contentValues);
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
            event.setDate(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_DATE)))));
            event.setAvatarId(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_AVATAR_ID)));
            event.setImage(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_IMAGE)));
            event.setTitle(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_TITLE)));
            event.setDescription(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_DESCRIPTION)));
            event.setSubmittedBy(cursor.getString(cursor.getColumnIndex(EVENT_COLOUMN_SUBMITTED_BY)));
            event.setNumOfPeopleInterested(cursor.getInt(cursor.getColumnIndex(EVENT_COLOUMN_PEOPLE_INTERESTED)));

            if (cursor.getInt(cursor.getColumnIndex(EVENT_COLOUMN_OUTSIDE_EVENT)) == 0) {
                event.setOutsideEvent(false);
            } else {
                event.setOutsideEvent(true);
            }

            if (cursor.getInt(cursor.getColumnIndex(EVENT_COLOUMN_VERIFIED)) == 0) {
                event.setVerified(false);
            } else {
                event.setVerified(true);
            }

            //Add event to list
            eventsList.add(event);
        }
        //Close cursor after use
        cursor.close();

        //Sort event list
        Collections.sort(eventsList, new Comparator<EventsModel>() {
            @Override
            public int compare(EventsModel eventsModel, EventsModel t1) {
                if (eventsModel.getDate().getTime() > t1.getDate().getTime()) {
                    return 1;
                } else if (eventsModel.getDate().getTime() == t1.getDate().getTime()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        return eventsList;
    }

    /**
     * Get a list of outside events stores in the database
     * Return a List of EventsModel objects
     */
    public List<EventsModel> getOutsideEventsList() {
        //Get Database
        SQLiteDatabase database = getReadableDatabase();

        //Create new EventsModel list
        List<EventsModel> eventsList = new ArrayList<>();

        //Set up the query
        String sql = "SELECT * FROM " + OUTSIDE_EVENTS_TABLE;

        //Run the query and obtain cursor
        Cursor cursor = database.rawQuery(sql, null);

        //Extract the values while looping over cursor
        while (cursor.moveToNext()) {
            EventsModel event = new EventsModel();

            event.setId(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_ID)));
            event.setVenue(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_VENUE)));
            event.setDate(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_DATE)))));
            event.setAvatarId(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_AVATAR_ID)));
            event.setImage(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_IMAGE)));
            event.setTitle(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_TITLE)));
            event.setDescription(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_DESCRIPTION)));
            event.setSubmittedBy(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLOUMN_SUBMITTED_BY)));
            event.setNumOfPeopleInterested(cursor.getInt(cursor.getColumnIndex(OUTSIDE_EVENT_COLOUMN_PEOPLE_INTERESTED)));

            if (cursor.getInt(cursor.getColumnIndex(OUTSIDE_EVENT_COLOUMN_OUTSIDE_EVENT)) == 0) {
                event.setOutsideEvent(false);
            } else {
                event.setOutsideEvent(true);
            }

            if (cursor.getInt(cursor.getColumnIndex(OUTSIDE_EVENT_COLOUMN_VERIFIED)) == 0) {
                event.setVerified(false);
            } else {
                event.setVerified(true);
            }

            //Add event to list
            eventsList.add(event);
        }
        //Close cursor after use
        cursor.close();

        //Sort event list
        Collections.sort(eventsList, new Comparator<EventsModel>() {
            @Override
            public int compare(EventsModel eventsModel, EventsModel t1) {
                if (eventsModel.getDate().getTime() > t1.getDate().getTime()) {
                    return 1;
                } else if (eventsModel.getDate().getTime() == t1.getDate().getTime()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        return eventsList;
    }

    /**
     * Get a list of submitted events stores in the database
     * Return a List of EventsModel objects
     */
    public List<EventsModel> getSubmittedEventsList() {
        //Get Database
        SQLiteDatabase database = getReadableDatabase();

        //Create new EventsModel list
        List<EventsModel> eventsList = new ArrayList<>();

        //Set up the query
        String sql = "SELECT * FROM " + SUBMITTED_EVENTS_TABLE;

        //Run the query and obtain cursor
        Cursor cursor = database.rawQuery(sql, null);

        //Extract the values while looping over cursor
        while (cursor.moveToNext()) {
            EventsModel event = new EventsModel();

            event.setId(cursor.getString(cursor.getColumnIndex(SUBMITTED_EVENT_COLUMN_ID)));
            event.setVenue(cursor.getString(cursor.getColumnIndex(SUBMITTED_EVENT_COLUMN_VENUE)));
            event.setDate(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(SUBMITTED_EVENT_COLUMN_DATE)))));
            event.setAvatarId(cursor.getString(cursor.getColumnIndex(SUBMITTED_EVENT_COLUMN_AVATAR_ID)));
            event.setImage(cursor.getString(cursor.getColumnIndex(SUBMITTED_EVENT_COLUMN_IMAGE)));
            event.setTitle(cursor.getString(cursor.getColumnIndex(SUBMITTED_EVENT_COLUMN_TITLE)));
            event.setDescription(cursor.getString(cursor.getColumnIndex(SUBMITTED_EVENT_COLUMN_DESCRIPTION)));
            event.setSubmittedBy(cursor.getString(cursor.getColumnIndex(SUBMITTED_EVENT_COLOUMN_SUBMITTED_BY)));
            event.setNumOfPeopleInterested(cursor.getInt(cursor.getColumnIndex(SUBMITTED_EVENT_COLOUMN_PEOPLE_INTERESTED)));

            if (cursor.getInt(cursor.getColumnIndex(SUBMITTED_EVENT_COLOUMN_OUTSIDE_EVENT)) == 0) {
                event.setOutsideEvent(false);
            } else {
                event.setOutsideEvent(true);
            }

            if (cursor.getInt(cursor.getColumnIndex(SUBMITTED_EVENT_COLOUMN_VERIFIED)) == 0) {
                event.setVerified(false);
            } else {
                event.setVerified(true);
            }

            //Add event to list
            eventsList.add(event);
        }
        //Close cursor after use
        cursor.close();

        //Sort event list
        Collections.sort(eventsList, new Comparator<EventsModel>() {
            @Override
            public int compare(EventsModel eventsModel, EventsModel t1) {
                if (eventsModel.getDate().getTime() > t1.getDate().getTime()) {
                    return 1;
                } else if (eventsModel.getDate().getTime() == t1.getDate().getTime()) {
                    return 0;
                } else {
                    return -1;
                }
            }
        });

        return eventsList;
    }


    /**
     * Return the Particular event corresponding to the id
     */
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
            eventsModel.setAvatarId(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_AVATAR_ID)));
            eventsModel.setImage(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_IMAGE)));
            eventsModel.setVenue(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_VENUE)));
            eventsModel.setRequirements(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_REQUIREMENTS)));
            eventsModel.setDate(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_DATE)))));
            eventsModel.setSubmittedBy(cursor.getString(cursor.getColumnIndex(EVENT_COLOUMN_SUBMITTED_BY)));
            eventsModel.setNumOfPeopleInterested(cursor.getInt(cursor.getColumnIndex(EVENT_COLOUMN_PEOPLE_INTERESTED)));

            if (cursor.getInt(cursor.getColumnIndex(EVENT_COLOUMN_OUTSIDE_EVENT)) == 0) {
                eventsModel.setOutsideEvent(false);
            } else {
                eventsModel.setOutsideEvent(true);
            }

            if (cursor.getInt(cursor.getColumnIndex(EVENT_COLOUMN_VERIFIED)) == 0) {
                eventsModel.setVerified(false);
            } else {
                eventsModel.setVerified(true);
            }
        }

        //Close the cursor
        cursor.close();

        return eventsModel;
    }

    /**
     * Return the Particular Outside event corresponding to the id
     */
    public EventsModel getOutsideEvent(String id) {
        //Check if event id is null
        if (id == null) {
            return null;
        }

        EventsModel eventsModel = new EventsModel();

        SQLiteDatabase database = getReadableDatabase();

        String sql = "SELECT * FROM " + OUTSIDE_EVENTS_TABLE + " WHERE " + OUTSIDE_EVENT_COLUMN_ID + " = '" + id + "'";

        //Run query
        Cursor cursor = database.rawQuery(sql, null);

        //Traverse the cursor
        while (cursor.moveToNext()) {
            eventsModel.setId(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_ID)));
            eventsModel.setTitle(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_TITLE)));
            eventsModel.setDescription(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_DESCRIPTION)));
            eventsModel.setAvatarId(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_AVATAR_ID)));
            eventsModel.setImage(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_IMAGE)));
            eventsModel.setVenue(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_VENUE)));
            eventsModel.setRequirements(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_REQUIREMENTS)));
            eventsModel.setDate(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_DATE)))));
            eventsModel.setSubmittedBy(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLOUMN_SUBMITTED_BY)));
            eventsModel.setNumOfPeopleInterested(cursor.getInt(cursor.getColumnIndex(OUTSIDE_EVENT_COLOUMN_PEOPLE_INTERESTED)));

            if (cursor.getInt(cursor.getColumnIndex(OUTSIDE_EVENT_COLOUMN_OUTSIDE_EVENT)) == 0) {
                eventsModel.setOutsideEvent(false);
            } else {
                eventsModel.setOutsideEvent(true);
            }

            if (cursor.getInt(cursor.getColumnIndex(OUTSIDE_EVENT_COLOUMN_VERIFIED)) == 0) {
                eventsModel.setVerified(false);
            } else {
                eventsModel.setVerified(true);
            }
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

    //Check if outside event is already in database
    public boolean doesOutsideEventAlreadyExists(String id) {
        //If event id is wrong return true
        if (id == null) {
            return true;
        }

        String sql = "SELECT * FROM " + OUTSIDE_EVENTS_TABLE + " WHERE " + OUTSIDE_EVENT_COLUMN_ID + " = '" + id + "'";

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

    public boolean doesFavEventAlreadyExists(String id) {
        //If event id is wrong return true
        if (id == null) {
            return true;
        }

        String sql = "SELECT * FROM " + FAV_EVENTS_TABLE + " WHERE " + FAV_EVENT_COLUMN_ID + " = '" + id + "'";

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

    /**
     * Take CommentsModel as input
     * Extract values from model, create content values and insert into table
     */
    public void insertNewContact(ContactsModel contactsModel) {
        //Get writable database
        SQLiteDatabase database = getWritableDatabase();

        //Prepare content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONTACTS_EVENT_ID, contactsModel.getEventsID());
        contentValues.put(CONTACTS_NAME, contactsModel.getContactName());
        contentValues.put(CONTACTS_EMAIL_ID, contactsModel.getEmailID());
        contentValues.put(CONTACTS_PHONE_NUMBER, contactsModel.getPhoneNumber());

        //Insert into database
        database.insert(CONTACTS_TABLE, null, contentValues);
    }

    /**
     * Get Contacts List
     * Returns a list of ContactsModel for a particular event ID
     */
    public List<ContactsModel> getContactsList(String eventID) {
        //Create a list
        ArrayList<ContactsModel> mList = new ArrayList<>();

        //Get database
        SQLiteDatabase database = getReadableDatabase();

        //Prepare query
        String sql = "SELECT * FROM " + CONTACTS_TABLE + " WHERE " + CONTACTS_EVENT_ID + "= " + "'" + eventID + "'";

        //Run query and get a cursor
        Cursor cursor = database.rawQuery(sql, null);

        //Loop over the cursor and inflate the list
        while (cursor.moveToNext()) {
            //Create new contact
            ContactsModel contact = new ContactsModel();
            contact.setEventsID(cursor.getString(cursor.getColumnIndex(CONTACTS_EVENT_ID)));
            contact.setEmailID(cursor.getString(cursor.getColumnIndex(CONTACTS_EMAIL_ID)));
            contact.setPhoneNumber(cursor.getString(cursor.getColumnIndex(CONTACTS_PHONE_NUMBER)));
            contact.setContactName(cursor.getString(cursor.getColumnIndex(CONTACTS_NAME)));

            mList.add(contact);
        }

        //Close cursor
        cursor.close();

        return mList;
    }

    /**
     * Insert a new link into links table
     * Get a LinksModel, extract data and put in content values
     */
    public void insertNewLink(LinksModel model) {
        //Get database
        SQLiteDatabase database = getWritableDatabase();

        //Create content values
        ContentValues contentValues = new ContentValues();
        contentValues.put(LINKS_EVENT_ID, model.getEventID());
        contentValues.put(LINKS_NAME, model.getLinkName());
        contentValues.put(LINKS_ADDRESS, model.getLinkAddress());

        //Insert into database
        database.insert(LINKS_TABLE, null, contentValues);
    }

    /**
     * Get links list
     * Return a list of LinksModel corresponding to a particular event
     */
    public List<LinksModel> getLinksList(String eventID) {
        //Create a list
        ArrayList<LinksModel> mList = new ArrayList<>();

        //Get the data base
        SQLiteDatabase database = getReadableDatabase();

        //Prepare sql statement
        String sql = "SELECT * FROM " + LINKS_TABLE + " WHERE " + LINKS_EVENT_ID + "= " + "'" + eventID + "'";

        //Run query and get a cursor
        Cursor cursor = database.rawQuery(sql, null);

        //Loop over the cursor and extract data
        while (cursor.moveToNext()) {
            LinksModel model = new LinksModel();
            model.setEventID(eventID);
            model.setLinkAddress(cursor.getString(cursor.getColumnIndex(LINKS_ADDRESS)));
            model.setLinkName(cursor.getString(cursor.getColumnIndex(LINKS_NAME)));

            mList.add(model);
        }

        //Close cursor
        cursor.close();

        return mList;
    }

    public void deleteFavEvent(EventsModel event) {
        SQLiteDatabase database = getWritableDatabase();
        String sql = "Delete FROM " + FAV_EVENTS_TABLE + " where " + EVENT_COLUMN_ID + " = \"" + event.getId() + "\";";

        database.execSQL(sql);
    }

    /**
     * Delete a particular event from database
     */
    public void deleteEvent(String id) {
        //Get the data base
        SQLiteDatabase database = getWritableDatabase();

        //SQL Query
        String eventDeleteQuery = "DELETE FROM " + EVENTS_TABLE +
                " WHERE " + EVENT_COLUMN_ID + " = " + "'" + id + "'";

        //SQL Query
        String contactDeleteQuery = "DELETE FROM " + CONTACTS_TABLE +
                " WHERE " + CONTACTS_EVENT_ID + " = " + "'" + id + "'";

        //SQL Query
        String linksDeleteQuery = "DELETE FROM " + LINKS_TABLE +
                " WHERE " + LINKS_EVENT_ID + " = " + "'" + id + "'";

        String favEventDeleteQuery = "DELETE FROM " + FAV_EVENTS_TABLE +
                " WHERE " + FAV_EVENT_COLUMN_ID + " = " + "'" + id + "'";

        String OutsideEventDeleteQuery = "DELETE FROM " + OUTSIDE_EVENTS_TABLE +
                " WHERE " + OUTSIDE_EVENT_COLUMN_ID + " = " + "'" + id + "'";

        String SubmittedEventDeleteQuery = "DELETE FROM " + SUBMITTED_EVENTS_TABLE +
                " WHERE " + SUBMITTED_EVENT_COLUMN_ID + " = " + "'" + id + "'";

        //Execute queries
        database.execSQL(eventDeleteQuery);
        database.execSQL(contactDeleteQuery);
        database.execSQL(linksDeleteQuery);
        database.execSQL(favEventDeleteQuery);
        database.execSQL(OutsideEventDeleteQuery);
        database.execSQL(SubmittedEventDeleteQuery);

        //Close database
        database.close();
    }

    /**
     * Remove all the events in the database
     */
    public void removeAllEventsFromDB() {
        //Get the data base
        SQLiteDatabase database = getWritableDatabase();

        //SQL Query
        String deleteEventsSQL = "DELETE FROM " + EVENTS_TABLE;
        String deleteContactsSQL = "DELETE FROM " + CONTACTS_TABLE;
        String deleteLinksSQL = "DELETE FROM " + LINKS_TABLE;
        String deleteFavEventsSQL = "DELETE FROM " + FAV_EVENTS_TABLE;
        String deleteSubmittedEventsSQL = "DELETE FROM " + SUBMITTED_EVENTS_TABLE;
        String deleteOutsideEventsSQL = "DELETE FROM " + OUTSIDE_EVENTS_TABLE;

        //Execute queries
        database.execSQL(deleteEventsSQL);
        database.execSQL(deleteContactsSQL);
        database.execSQL(deleteLinksSQL);
        database.execSQL(deleteFavEventsSQL);
        database.execSQL(deleteSubmittedEventsSQL);
        database.execSQL(deleteOutsideEventsSQL);

        //Close database
        database.close();
    }

    /**
     * Remove the events from database that are not in the stream
     */
    public void removeSpecificEventsFromDB(List<String> list) {
        //Get the data base
        SQLiteDatabase database = getReadableDatabase();

        //SQL query to get all the event id's in database
        String eventIdSQL = "SELECT " + EVENT_COLUMN_ID + " FROM " + EVENTS_TABLE;

        //Empty id list
        List<String> mDatabaseIDList = new ArrayList<>();

        //Execute query
        Cursor cursor = database.rawQuery(eventIdSQL, null);

        //Iterate and add to list
        while (cursor.moveToNext()) {
            mDatabaseIDList.add(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_ID)));
        }

        //Close database and cursor
        cursor.close();
        database.close();

        //Delete all the events that are not in the network list
        for (String eventID : mDatabaseIDList) {
            if (!list.contains(eventID)) {
                deleteEvent(eventID);
            }
        }
    }


    /**
     * Remove the outside events from database that are not in the stream
     */
    public void removeSpecificOutsideEventsFromDB(List<String> list) {
        //Get the data base
        SQLiteDatabase database = getReadableDatabase();

        //SQL query to get all the event id's in database
        String eventIdSQL = "SELECT " + OUTSIDE_EVENT_COLUMN_ID + " FROM " + OUTSIDE_EVENTS_TABLE;

        //Empty id list
        List<String> mDatabaseIDList = new ArrayList<>();

        //Execute query
        Cursor cursor = database.rawQuery(eventIdSQL, null);

        //Iterate and add to list
        while (cursor.moveToNext()) {
            mDatabaseIDList.add(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_ID)));
        }

        //Close database and cursor
        cursor.close();
        database.close();

        //Delete all the events that are not in the network list
        for (String eventID : mDatabaseIDList) {
            if (!list.contains(eventID)) {
                deleteEvent(eventID);
            }
        }
    }

    /**
     * Get list of events on a particular day
     */
    public List<EventsModel> getEventsOnADay(long dayStart, long dayEnd) {
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
            event.setDate(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_DATE)))));
            event.setAvatarId(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_AVATAR_ID)));
            event.setImage(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_IMAGE)));
            event.setTitle(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_TITLE)));
            event.setDescription(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_DESCRIPTION)));
            event.setSubmittedBy(cursor.getString(cursor.getColumnIndex(EVENT_COLOUMN_SUBMITTED_BY)));
            event.setNumOfPeopleInterested(cursor.getInt(cursor.getColumnIndex(EVENT_COLOUMN_PEOPLE_INTERESTED)));

            if (cursor.getInt(cursor.getColumnIndex(EVENT_COLOUMN_OUTSIDE_EVENT)) == 0) {
                event.setOutsideEvent(false);
            } else {
                event.setOutsideEvent(true);
            }

            if (cursor.getInt(cursor.getColumnIndex(EVENT_COLOUMN_VERIFIED)) == 0) {
                event.setVerified(false);
            } else {
                event.setVerified(true);
            }


            if (event.getDate().getTime() > dayStart && event.getDate().getTime() < dayEnd) {
                eventsList.add(event);
            }
        }
        //Close cursor after use
        cursor.close();

        return eventsList;
    }

    /**
     * Get list of Outside events on a particular day
     */
    public List<EventsModel> getOutsideEventsOnADay(long dayStart, long dayEnd) {
        //Get Database
        SQLiteDatabase database = getReadableDatabase();

        //Create new EventsModel list
        List<EventsModel> eventsList = new ArrayList<>();

        //Set up the query
        String sql = "SELECT * FROM " + OUTSIDE_EVENTS_TABLE;

        //Run the query and obtain cursor
        Cursor cursor = database.rawQuery(sql, null);

        //Extract the values while looping over cursor
        while (cursor.moveToNext()) {
            EventsModel event = new EventsModel();

            event.setId(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_ID)));
            event.setVenue(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_VENUE)));
            event.setDate(new Date(Long.parseLong(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_DATE)))));
            event.setAvatarId(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_AVATAR_ID)));
            event.setImage(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_IMAGE)));
            event.setTitle(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_TITLE)));
            event.setDescription(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLUMN_DESCRIPTION)));
            event.setSubmittedBy(cursor.getString(cursor.getColumnIndex(OUTSIDE_EVENT_COLOUMN_SUBMITTED_BY)));
            event.setNumOfPeopleInterested(cursor.getInt(cursor.getColumnIndex(OUTSIDE_EVENT_COLOUMN_PEOPLE_INTERESTED)));

            if (cursor.getInt(cursor.getColumnIndex(OUTSIDE_EVENT_COLOUMN_OUTSIDE_EVENT)) == 0) {
                event.setOutsideEvent(false);
            } else {
                event.setOutsideEvent(true);
            }

            if (cursor.getInt(cursor.getColumnIndex(OUTSIDE_EVENT_COLOUMN_VERIFIED)) == 0) {
                event.setVerified(false);
            } else {
                event.setVerified(true);
            }


            if (event.getDate().getTime() > dayStart && event.getDate().getTime() < dayEnd) {
                eventsList.add(event);
            }
        }
        //Close cursor after use
        cursor.close();

        return eventsList;
    }

    /**
     * Insert a new Comment
     */
    public void insertNewComment(CommentsModel commentsModel) {
        //Get Database
        SQLiteDatabase database = getReadableDatabase();

        //Create content values
        ContentValues contentValues = new ContentValues();

        //Add values
        contentValues.put(COMMENTS_COLUMN_COMMENT, commentsModel.getComment());
        contentValues.put(COMMENTS_COLUMN_EVENT_ID, commentsModel.getEventID());
        contentValues.put(COMMENTS_COLUMN_TIME, String.valueOf(commentsModel.getTime()));
        contentValues.put(COMMENTS_COLUMN_FROM, commentsModel.getFrom());

        //Insert into db
        database.insert(COMMENTS_TABLE, null, contentValues);

        //Close database
        database.close();
    }

    /**
     * Get a list of all the comments related to a particular event
     */
    public List<CommentsModel> getCommentsList(String eventId) {
        //Get Database
        SQLiteDatabase database = getReadableDatabase();

        //Create a new list
        List<CommentsModel> list = new ArrayList<>();

        //Select query
        String commentsSQL = "SELECT * FROM " + COMMENTS_TABLE + " WHERE " + COMMENTS_COLUMN_EVENT_ID + " = " + "'" + eventId + "'";

        //Run the query and get cursor
        Cursor cursor = database.rawQuery(commentsSQL, null);

        //Iterate cursor and return the list
        while (cursor.moveToNext()) {
            CommentsModel model = new CommentsModel();

            model.setComment(cursor.getString(cursor.getColumnIndex(COMMENTS_COLUMN_COMMENT)));
            model.setTime(Long.parseLong(cursor.getString(cursor.getColumnIndex(COMMENTS_COLUMN_TIME))));
            model.setFrom(cursor.getString(cursor.getColumnIndex(COMMENTS_COLUMN_FROM)));
            model.setEventID(eventId);

            list.add(model);
        }

        //Close cursor
        cursor.close();

        //Close database
        database.close();

        return list;
    }

    /**
     * Update the comments for a specified event
     */
    public void updateComments(List<CommentsModel> list, String eventId) {
        //Get Database
        SQLiteDatabase database = getReadableDatabase();

        //Delete SQL for existing comments
        String deleteSQL = "DELETE FROM " + COMMENTS_TABLE + " WHERE " + COMMENTS_COLUMN_EVENT_ID + " = " + "'" + eventId + "'";

        database.execSQL(deleteSQL);

        //Close database
        database.close();

        for (CommentsModel model : list) {
            insertNewComment(model);
        }
    }

    /**
     * Check if Submitted Event Already Exists in the database
     *
     * @return
     */

    public boolean doesSubmittedEventAlreadyExists(String id) {
        //If event id is wrong return true
        if (id == null) {
            return true;
        }

        String sql = "SELECT * FROM " + SUBMITTED_EVENTS_TABLE + " WHERE " + SUBMITTED_EVENT_COLUMN_ID + " = '" + id + "'";

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

    /**
     * Remove the events from database that are not in the stream
     */
    public void removeSpecificSubmittedEventsFromDB(List<String> list) {
        //Get the data base
        SQLiteDatabase database = getReadableDatabase();

        //SQL query to get all the event id's in database
        String eventIdSQL = "SELECT " + SUBMITTED_EVENT_COLUMN_ID + " FROM " + SUBMITTED_EVENTS_TABLE;

        //Empty id list
        List<String> mDatabaseIDList = new ArrayList<>();

        //Execute query
        Cursor cursor = database.rawQuery(eventIdSQL, null);

        //Iterate and add to list
        while (cursor.moveToNext()) {
            mDatabaseIDList.add(cursor.getString(cursor.getColumnIndex(SUBMITTED_EVENT_COLUMN_ID)));
        }

        //Close database and cursor
        cursor.close();
        database.close();

        //Delete all the events that are not in the network list
        for (String eventID : mDatabaseIDList) {
            if (!list.contains(eventID)) {
                deleteEvent(eventID);
            }
        }
    }

    /**
     * Delete from Submitted Events
     */
    public void deleteFromSubmittedEvents(String id) {
        //Get the data base
        SQLiteDatabase database = getWritableDatabase();

        //SQL Query
        String eventDeleteQuery = "DELETE FROM " + SUBMITTED_EVENTS_TABLE +
                " WHERE " + SUBMITTED_EVENT_COLUMN_ID + " = " + "'" + id + "'";

        database.execSQL(eventDeleteQuery);
    }
}
