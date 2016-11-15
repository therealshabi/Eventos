package app.com.thetechnocafe.eventos.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.com.thetechnocafe.eventos.Models.ContactsModel;
import app.com.thetechnocafe.eventos.Models.EventsModel;
import app.com.thetechnocafe.eventos.Models.LinksModel;

import static android.R.attr.id;

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
                EVENT_COLUMN_AVATAR_ID + " INTEGER, " +
                EVENT_COLUMN_IMAGE + " VARCHAR, " +
                EVENT_COLUMN_REQUIREMENTS + " VARCHAR " +
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

        //Run the queries to create tables
        db.execSQL(eventsTableSQL);
        db.execSQL(contactsTableSQL);
        db.execSQL(linksTableSQL);
        db.execSQL(favEventsTableSQL);
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
        contentValues.put(EVENT_COLUMN_REQUIREMENTS, event.getRequirements());


        database.insert(EVENTS_TABLE, null, contentValues);
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
            eventsModel.setAvatarId(cursor.getInt(cursor.getColumnIndex(EVENT_COLUMN_AVATAR_ID)));
            eventsModel.setImage(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_IMAGE)));
            eventsModel.setVenue(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_VENUE)));
            eventsModel.setRequirements(cursor.getString(cursor.getColumnIndex(EVENT_COLUMN_REQUIREMENTS)));
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
}
