package app.com.thetechnocafe.eventos.DataSync;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.com.thetechnocafe.eventos.Database.EventsDatabaseHelper;
import app.com.thetechnocafe.eventos.Models.CommentsModel;
import app.com.thetechnocafe.eventos.Models.ContactsModel;
import app.com.thetechnocafe.eventos.Models.EventsModel;
import app.com.thetechnocafe.eventos.Models.LinksModel;
import app.com.thetechnocafe.eventos.Utils.SharedPreferencesUtils;

public abstract class DataSynchronizer {
    private static final String TAG = "DataSynchronizer";
    private static final String LINK_EVENT_REQUEST = "http://192.168.0.7:55555/api/events";
    private static final String LINK_OUTSIDE_EVENT_REQUEST = "http://192.168.0.7:55555/api/outside_events";
    //String related to json data fetched
    private static final String JSON_STATUS = "status";
    private static final String JSON_DATA = "data";
    private static final String JSON_SUCCESS = "success";
    //JSON keys for events
    private static final String JSON_EVENT_TITLE = "title";
    private static final String JSON_EVENT_DESCRIPTION = "description";
    private static final String JSON_EVENT_DATE = "date";
    private static final String JSON_EVENT_VENUE = "venue";
    private static final String JSON_EVENT_IMAGE = "image";
    private static final String JSON_EVENT_AVATAR_ID = "avatar_id";
    private static final String JSON_EVENT_ID = "_id";
    private static final String JSON_EVENT_REQUIREMENTS = "requirements";
    private static final String JSON_EVENT_VERIFIED = "verified";
    private static final String JSON_PEOPLE_INTERESTED = "people_interested";
    private static final String JSON_OUTSIDE_EVENT = "outside_event";

    private static final String JSON_EVENT_SUBMIITED_BY = "submitted_by";

    private EventsDatabaseHelper mEventsDatabaseHelper;

    /**
     * Function that notifies if sync was successful or not
     * Calling class has to override this function
     */
    public abstract void onDataSynchronized(boolean isSyncSuccessful);

    /**
     * Fetch data from network using volley
     * Create JSON request
     * Fetch data and store to SQLite database
     */
    public void fetchEventsFromNetwork(final Context context) {
        //Create new Json request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, LINK_EVENT_REQUEST, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Check if any data received
                if (response != null) {
                    try {
                        //Check server response
                        if (response.getString(JSON_STATUS).equals(JSON_SUCCESS)) {
                            //Get the database object
                            mEventsDatabaseHelper = new EventsDatabaseHelper(context);

                            //Delete all the events that have been delete from server
                            List<String> idList = new ArrayList<>();

                            //Retrieve data from JSON object
                            JSONArray eventsJSONArray = response.getJSONArray(JSON_DATA);

                            //Loop over the array and store data in SQLite database
                            for (int i = 0; i < eventsJSONArray.length(); i++) {
                                //Get json object at position i
                                JSONObject eventJSONobject = eventsJSONArray.getJSONObject(i);

                                //Create new event object
                                EventsModel event = new EventsModel();

                                //Insert the details into event object
                                if (insertEventDetails(event, eventJSONobject)) {
                                    //Add id to list
                                    idList.add(event.getId());

                                    //Create a list of comments
                                    List<CommentsModel> list = new ArrayList<>();

                                    //Insert comments into comment database
                                    JSONArray commentsJSONArray = eventJSONobject.getJSONArray(StringUtils.JSON_COMMENTS);
                                    for (int count = 0; count < commentsJSONArray.length(); count++) {
                                        CommentsModel model = new CommentsModel();

                                        model.setEventID(event.getId());
                                        model.setTime(commentsJSONArray.getJSONObject(count).getLong(StringUtils.JSON_TIME));
                                        model.setFrom(commentsJSONArray.getJSONObject(count).getString(StringUtils.JSON_FROM));
                                        model.setComment(commentsJSONArray.getJSONObject(count).getString(StringUtils.JSON_COMMENT));

                                        list.add(model);
                                    }

                                    //Insert into database
                                    mEventsDatabaseHelper.updateComments(list, event.getId());

                                    //Check if events already is in database
                                    //Insert the event into database
                                    if (!mEventsDatabaseHelper.doesEventAlreadyExists(event.getId())) {
                                        mEventsDatabaseHelper.insertNewEvent(event);

                                        if (event.getSubmittedBy().equals(SharedPreferencesUtils.getUsername(context))) {
                                            if (!mEventsDatabaseHelper.doesSubmittedEventAlreadyExists(event.getId())) {
                                                mEventsDatabaseHelper.insertNewSubmittedEvent(event);
                                            }

                                        }

                                        //Get the contacts
                                        JSONArray contactJSONArray = eventJSONobject.getJSONArray(StringUtils.JSON_CONTACTS);

                                        //Loop over the array and retrieve contacts JSON objects
                                        for (int contactsCount = 0; contactsCount < contactJSONArray.length(); contactsCount++) {
                                            ContactsModel contactsModel = new ContactsModel();
                                            //Inflate the object with data
                                            if (insertContactDetails(event.getId(), contactsModel, contactJSONArray.getJSONObject(contactsCount))) {
                                                //Insert into database
                                                mEventsDatabaseHelper.insertNewContact(contactsModel);
                                            }
                                        }

                                        //Get the links
                                        JSONArray linksJSONArray = eventJSONobject.getJSONArray(StringUtils.JSON_LINKS);

                                        //Loop over the array and retrieve links JSON objects
                                        for (int linksCount = 0; linksCount < linksJSONArray.length(); linksCount++) {
                                            LinksModel linksModel = new LinksModel();
                                            //Inflate the object with data
                                            if (insertLinkDetails(event.getId(), linksModel, linksJSONArray.getJSONObject(linksCount))) {
                                                //Insert into database
                                                mEventsDatabaseHelper.insertNewLink(linksModel);
                                            }
                                        }
                                    }
                                }
                            }

                            //Check for redundant events
                            mEventsDatabaseHelper.removeSpecificEventsFromDB(idList);

                            //Notify sync successful
                            onDataSynchronized(true);

                            //Close database
                            mEventsDatabaseHelper.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onDataSynchronized(false);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Notify synced failed
                onDataSynchronized(false);
            }
        });

        //Add the request to request queue
        VolleyQueue.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Set the required data in the EventsModel object
     * Checks if the particular data exists and inserts accordingly
     */
    private boolean insertEventDetails(EventsModel event, JSONObject object) {
        try {
            event.setTitle(object.getString(JSON_EVENT_TITLE));
            event.setDescription(object.getString(JSON_EVENT_DESCRIPTION));
            event.setImage(object.getString(JSON_EVENT_IMAGE));
            event.setVenue(object.getString(JSON_EVENT_VENUE));
            event.setId(object.getString(JSON_EVENT_ID));
            event.setAvatarId(object.getInt(JSON_EVENT_AVATAR_ID));
            event.setDate(new Date(object.getLong(StringUtils.JSON_DATE)));
            event.setRequirements(object.getString(JSON_EVENT_REQUIREMENTS));
            event.setSubmittedBy(object.getString(JSON_EVENT_SUBMIITED_BY));
            event.setVerified(object.getBoolean(JSON_EVENT_VERIFIED));
            event.setNumOfPeopleInterested(object.getInt(JSON_PEOPLE_INTERESTED));
            event.setOutsideEvent(object.getBoolean(JSON_OUTSIDE_EVENT));
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Set the required data in ContactsModel object from the JSONObject
     */
    private boolean insertContactDetails(String eventID, ContactsModel model, JSONObject object) {
        try {
            model.setEventsID(eventID);
            model.setContactName(object.getString(StringUtils.JSON_CONTACTS_NAME));
            model.setPhoneNumber(object.getString(StringUtils.JSON_CONTACTS_PHONE));
            model.setEmailID(object.getString(StringUtils.JSON_CONTACTS_EMAIL));
        } catch (JSONException e) {
            //Bad data
            return false;
        }
        return true;
    }

    /**
     * Set the required data in LinksModel object from the JSONObject
     */
    private boolean insertLinkDetails(String eventID, LinksModel model, JSONObject object) {
        try {
            model.setEventID(eventID);
            model.setLinkName(object.getString(StringUtils.JSON_LINKS_NAME));
            model.setLinkAddress(object.getString(StringUtils.JSON_LINKS_ADDRESS));
        } catch (JSONException e) {
            //Bad data
            return false;
        }
        return true;
    }


    public void fetchOutsideEventsFromNetwork(final Context context) {
        //Create new Json request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, LINK_OUTSIDE_EVENT_REQUEST, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Check if any data received
                if (response != null) {
                    try {
                        //Check server response
                        if (response.getString(JSON_STATUS).equals(JSON_SUCCESS)) {
                            //Get the database object
                            mEventsDatabaseHelper = new EventsDatabaseHelper(context);

                            //Delete all the events that have been delete from server
                            List<String> idList = new ArrayList<>();

                            //Retrieve data from JSON object
                            JSONArray eventsJSONArray = response.getJSONArray(JSON_DATA);

                            //Loop over the array and store data in SQLite database
                            for (int i = 0; i < eventsJSONArray.length(); i++) {
                                //Get json object at position i
                                JSONObject eventJSONobject = eventsJSONArray.getJSONObject(i);

                                //Create new event object
                                EventsModel event = new EventsModel();

                                //Insert the details into event object
                                if (insertEventDetails(event, eventJSONobject)) {
                                    //Add id to list
                                    idList.add(event.getId());

                                    //Create a list of comments
                                    List<CommentsModel> list = new ArrayList<>();

                                    //Insert comments into comment database
                                    JSONArray commentsJSONArray = eventJSONobject.getJSONArray(StringUtils.JSON_COMMENTS);
                                    for (int count = 0; count < commentsJSONArray.length(); count++) {
                                        CommentsModel model = new CommentsModel();

                                        model.setEventID(event.getId());
                                        model.setTime(commentsJSONArray.getJSONObject(count).getLong(StringUtils.JSON_TIME));
                                        model.setFrom(commentsJSONArray.getJSONObject(count).getString(StringUtils.JSON_FROM));
                                        model.setComment(commentsJSONArray.getJSONObject(count).getString(StringUtils.JSON_COMMENT));

                                        list.add(model);
                                    }

                                    //Insert into database
                                    mEventsDatabaseHelper.updateComments(list, event.getId());

                                    //Check if events already is in database
                                    //Insert the event into database
                                    if (!mEventsDatabaseHelper.doesEventAlreadyExists(event.getId())) {
                                        mEventsDatabaseHelper.insertNewEvent(event);

                                        if (event.getSubmittedBy().equals(SharedPreferencesUtils.getUsername(context))) {
                                            if (!mEventsDatabaseHelper.doesSubmittedEventAlreadyExists(event.getId())) {
                                                mEventsDatabaseHelper.insertNewSubmittedEvent(event);
                                            }

                                        }

                                        //Get the contacts
                                        JSONArray contactJSONArray = eventJSONobject.getJSONArray(StringUtils.JSON_CONTACTS);

                                        //Loop over the array and retrieve contacts JSON objects
                                        for (int contactsCount = 0; contactsCount < contactJSONArray.length(); contactsCount++) {
                                            ContactsModel contactsModel = new ContactsModel();
                                            //Inflate the object with data
                                            if (insertContactDetails(event.getId(), contactsModel, contactJSONArray.getJSONObject(contactsCount))) {
                                                //Insert into database
                                                mEventsDatabaseHelper.insertNewContact(contactsModel);
                                            }
                                        }

                                        //Get the links
                                        JSONArray linksJSONArray = eventJSONobject.getJSONArray(StringUtils.JSON_LINKS);

                                        //Loop over the array and retrieve links JSON objects
                                        for (int linksCount = 0; linksCount < linksJSONArray.length(); linksCount++) {
                                            LinksModel linksModel = new LinksModel();
                                            //Inflate the object with data
                                            if (insertLinkDetails(event.getId(), linksModel, linksJSONArray.getJSONObject(linksCount))) {
                                                //Insert into database
                                                mEventsDatabaseHelper.insertNewLink(linksModel);
                                            }
                                        }
                                    }
                                }
                            }

                            //Check for redundant events
                            mEventsDatabaseHelper.removeSpecificEventsFromDB(idList);

                            //Notify sync successful
                            onDataSynchronized(true);

                            //Close database
                            mEventsDatabaseHelper.close();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        onDataSynchronized(false);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Notify synced failed
                onDataSynchronized(false);
            }
        });

        //Add the request to request queue
        VolleyQueue.getInstance(context).getRequestQueue().add(request);
    }

}
