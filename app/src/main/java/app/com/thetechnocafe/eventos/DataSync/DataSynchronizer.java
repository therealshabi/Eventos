package app.com.thetechnocafe.eventos.DataSync;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import app.com.thetechnocafe.eventos.Database.EventsDatabaseHelper;
import app.com.thetechnocafe.eventos.Models.EventsModel;

/**
 * Created by gurleensethi on 15/10/16.
 * Synchronize data from network
 */

public abstract class DataSynchronizer {
    private EventsDatabaseHelper mEventsDatabaseHelper;

    private static final String TAG = "DataSynchronizer";
    private static final String LINK_EVENT_REQUEST = "http://192.168.43.55:8000/api/events";
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
                                    //Check if events already is in database
                                    //Insert the event into database
                                    if (!mEventsDatabaseHelper.doesEventAlreadyExists(event.getId())) {
                                        mEventsDatabaseHelper.insertNewEvent(event);
                                    }
                                }
                            }

                            //Notify sync successful
                            onDataSynchronized(true);
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
            event.setDate(new Date());
            event.setRequirements(object.getString(JSON_EVENT_REQUIREMENTS));
        } catch (JSONException e) {
            //Bad event data
            return false;
        }
        return true;
    }
}
