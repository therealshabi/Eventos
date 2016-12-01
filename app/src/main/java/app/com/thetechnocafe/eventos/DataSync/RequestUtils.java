package app.com.thetechnocafe.eventos.DataSync;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.com.thetechnocafe.eventos.Database.EventsDatabaseHelper;
import app.com.thetechnocafe.eventos.Models.EventsModel;
import app.com.thetechnocafe.eventos.Utils.SharedPreferencesUtils;

import static app.com.thetechnocafe.eventos.DataSync.StringUtils.JSON_DATA;

public abstract class RequestUtils {
    private static final String SERVER_ADDRESS = "http://192.168.0.7:55555";
    private static final String LINK_EVENT_REQUEST = SERVER_ADDRESS + "/api/events";
    private static final String SIGN_UP_REQUEST_ADDRESS = SERVER_ADDRESS + "/api/signup";
    private static final String SIGN_IN_REQUEST_ADDRESS = SERVER_ADDRESS + "/api/login";
    private static final String UPDATE_ACCOUNT_REQUEST_ADDRESS = SERVER_ADDRESS + "/api/user/update";
    private static final String GET_SUBMITTED_EVENTS_REQUEST_ADDRESS = SERVER_ADDRESS + "/api/submitted-events";
    private static final String SUBMIT_COMMENT_REQUEST_ADDRESS = SERVER_ADDRESS + "/api/events/comment";
    private static final String RATE_EVENT_REQUEST_POST = SERVER_ADDRESS + "/api/events/rating/";
    private static final String INCREASE_DECREASE_PARTICIPATION_EVENT = SERVER_ADDRESS + "/api/events/interested/";
    //JSON keys for submitted events
    private static final String JSON_EVENT_TITLE = "title";
    private static final String JSON_EVENT_DESCRIPTION = "description";
    private static final String JSON_EVENT_DATE = "date";
    private static final String JSON_EVENT_VENUE = "venue";
    private static final String JSON_EVENT_IMAGE = "image";
    private static final String JSON_EVENT_AVATAR_ID = "avatar_id";
    private static final String JSON_EVENT_ID = "_id";
    private static final String JSON_EVENT_REQUIREMENTS = "requirements";
    private static final String JSON_EVENT_VERIFIED = "verified";
    private static final String JSON_EVENT_SUBMIITED_BY = "submitted_by";
    private static final String JSON_EVENT_PEOPLE_INTERESTED = "people_interested";
    private static final String JSON_OUTSIDE_EVENT = "outside_event";

    public EventsDatabaseHelper mEventsDatabaseHelper;

    public abstract void isRequestSuccessful(boolean isSuccessful, String message);

    /**
     * Submit forum to network
     * requires
     */
    public void submitForumToServer(Context context, final JSONObject jsonObject) {
        //Create new JSON Object Request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, LINK_EVENT_REQUEST, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Check if response if not null
                if (response != null) {
                    //Check for success
                    try {
                        if (response.getString(StringUtils.JSON_STATUS).equals(StringUtils.JSON_SUCCESS)) {
                            isRequestSuccessful(true, null);
                        } else {
                            isRequestSuccessful(false, null);
                        }
                    } catch (JSONException e) {
                        isRequestSuccessful(false, null);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Notify error
                isRequestSuccessful(false, null);
            }
        }) {
            @Override
            public byte[] getBody() {
                return jsonObject.toString().getBytes();
            }
        };

        //Add request to queue
        VolleyQueue.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Sign up Request
     */
    public void signUp(Context context, final JSONObject jsonObject) {
        //Create new json object request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SIGN_UP_REQUEST_ADDRESS, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString(StringUtils.JSON_STATUS).equals(StringUtils.JSON_SUCCESS)) {
                        isRequestSuccessful(true, response.getString(JSON_DATA));
                    } else {
                        isRequestSuccessful(false, response.getString(JSON_DATA));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isRequestSuccessful(false, error.toString());
            }
        });

        //Add request to queue
        VolleyQueue.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Sign in request
     */
    public void signIn(final Context context, JSONObject jsonObject) {
        //Create new json object request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SIGN_IN_REQUEST_ADDRESS, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString(StringUtils.JSON_STATUS).equals(StringUtils.JSON_SUCCESS)) {
                        isRequestSuccessful(true, response.getString(JSON_DATA));
                        JSONObject object = response.getJSONArray(JSON_DATA).getJSONObject(0);
                        SharedPreferencesUtils.setFullName(context, object.getString(StringUtils.JSON_FULL_NAME));
                        SharedPreferencesUtils.setPassword(context, object.getString(StringUtils.JSON_PASSWORD));
                        SharedPreferencesUtils.setUsername(context, object.getString(StringUtils.JSON_EMAIL));
                        SharedPreferencesUtils.setPhoneNumber(context, object.getString(StringUtils.JSON_PHONE));

                    } else {
                        isRequestSuccessful(false, response.getString(JSON_DATA));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isRequestSuccessful(false, error.toString());
            }
        });

        //Add request to queue
        VolleyQueue.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Update user account details
     */
    public void updateAccountDetails(Context context, JSONObject object) {
        //Create new json request
        JsonRequest request = new JsonObjectRequest(Request.Method.PUT, UPDATE_ACCOUNT_REQUEST_ADDRESS, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString(StringUtils.JSON_STATUS).equals(StringUtils.JSON_SUCCESS)) {
                        isRequestSuccessful(true, response.getString(JSON_DATA));
                    } else {
                        isRequestSuccessful(false, response.getString(JSON_DATA));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isRequestSuccessful(false, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        //Add to volley queue
        VolleyQueue.getInstance(context).getRequestQueue().add(request);
    }

    public void increaseDecreaseParticipationEvent(final Context context, String id, int num) {
        JSONObject object = new JSONObject();
        try {
            object.put(StringUtils.JSON_EVENT_INCREASE_DECREASE, num);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, INCREASE_DECREASE_PARTICIPATION_EVENT + id, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString(StringUtils.JSON_STATUS).equals(StringUtils.JSON_SUCCESS)) {
                        isRequestSuccessful(true, response.getString(JSON_DATA));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    isRequestSuccessful(false, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isRequestSuccessful(false, null);
            }
        });

        VolleyQueue.getInstance(context).getRequestQueue().add(request);
    }

    /**
     * Get all the events submitted by the specified user
     */
    public void getSubmittedEvents(final Context context, String email) {
        //Create a JSON Object
        JSONObject object = new JSONObject();
        try {
            object.put(StringUtils.SUBMITTED_BY, email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, GET_SUBMITTED_EVENTS_REQUEST_ADDRESS, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString(StringUtils.JSON_STATUS).equals(StringUtils.JSON_SUCCESS)) {
                        isRequestSuccessful(true, response.getString(JSON_DATA));
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
                            if (insertSubmittedEventDetails(event, eventJSONobject)) {
                                //Add id to list
                                idList.add(event.getId());
                            }


                            if (!mEventsDatabaseHelper.doesSubmittedEventAlreadyExists(event.getId())) {
                                mEventsDatabaseHelper.insertNewSubmittedEvent(event);
                            } else {
                                mEventsDatabaseHelper.deleteFromSubmittedEvents(event.getId());
                                mEventsDatabaseHelper.insertNewSubmittedEvent(event);
                            }

                            mEventsDatabaseHelper.removeSpecificSubmittedEventsFromDB(idList);
                            mEventsDatabaseHelper.close();

                        }
                    } else {
                        isRequestSuccessful(false, response.getString(JSON_DATA));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    isRequestSuccessful(false, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isRequestSuccessful(false, null);
            }
        });

        //Add to volley queue
        VolleyQueue.getInstance(context).getRequestQueue().add(request);
    }

    private boolean insertSubmittedEventDetails(EventsModel event, JSONObject object) {
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
            event.setNumOfPeopleInterested(object.getInt(JSON_EVENT_PEOPLE_INTERESTED));
            event.setOutsideEvent(object.getBoolean(JSON_OUTSIDE_EVENT));
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Submite a comment for a particular event
     */

    public void submitCommentForEvent(Context context, JSONObject object) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SUBMIT_COMMENT_REQUEST_ADDRESS, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString(StringUtils.JSON_STATUS).equals(StringUtils.JSON_SUCCESS)) {
                        isRequestSuccessful(true, response.getString(JSON_DATA));
                    } else {
                        isRequestSuccessful(false, response.getString(JSON_DATA));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isRequestSuccessful(false, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isRequestSuccessful(false, null);
            }
        });

        //Add to volley queue
        VolleyQueue.getInstance(context).getRequestQueue().add(request);
    }

    //Submit Rating for a particular Event
    public void setRateEventRequestPost(Context context, JSONObject object, String id) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, RATE_EVENT_REQUEST_POST + id, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString(StringUtils.JSON_STATUS).equals(StringUtils.JSON_SUCCESS)) {
                        isRequestSuccessful(true, response.getString(JSON_DATA));
                    } else {
                        isRequestSuccessful(false, response.getString(JSON_DATA));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    isRequestSuccessful(false, null);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                isRequestSuccessful(false, null);
            }
        });

        //Add to volley queue
        VolleyQueue.getInstance(context).getRequestQueue().add(request);
    }
}




