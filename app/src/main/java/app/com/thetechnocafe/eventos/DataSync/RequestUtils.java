package app.com.thetechnocafe.eventos.DataSync;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import app.com.thetechnocafe.eventos.Utils.SharedPreferencesUtils;

public abstract class RequestUtils {
    private static final String SERVER_ADDRESS = "http://192.168.0.7:55555";
    private static final String LINK_EVENT_REQUEST = SERVER_ADDRESS + "/api/events";
    private static final String SIGN_UP_REQUEST_ADDRESS = SERVER_ADDRESS + "/api/signup";
    private static final String SIGN_IN_REQUEST_ADDRESS = SERVER_ADDRESS + "/api/login";
    private static final String UPDATE_ACCOUNT_REQUEST_ADDRESS = SERVER_ADDRESS + "/api/user/update";
    private static final String GET_SUBMITTED_EVENTS_REQUEST_ADDRESS = SERVER_ADDRESS + "/api/submitted-events";
    private static final String SUBMIT_COMMENT_REQUEST_ADDRESS = SERVER_ADDRESS + "/api/events/comment";
    private static final String RATE_EVENT_REQUEST_POST = SERVER_ADDRESS + "/api/events/rating/";

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
                        isRequestSuccessful(true, response.getString(StringUtils.JSON_DATA));
                    } else {
                        isRequestSuccessful(false, response.getString(StringUtils.JSON_DATA));
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
                        isRequestSuccessful(true, response.getString(StringUtils.JSON_DATA));
                        JSONObject object = response.getJSONArray(StringUtils.JSON_DATA).getJSONObject(0);
                        SharedPreferencesUtils.setFullName(context, object.getString(StringUtils.JSON_FULL_NAME));
                        SharedPreferencesUtils.setPassword(context, object.getString(StringUtils.JSON_PASSWORD));
                        SharedPreferencesUtils.setUsername(context, object.getString(StringUtils.JSON_EMAIL));
                        SharedPreferencesUtils.setPhoneNumber(context, object.getString(StringUtils.JSON_PHONE));

                    } else {
                        isRequestSuccessful(false, response.getString(StringUtils.JSON_DATA));
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
                        isRequestSuccessful(true, response.getString(StringUtils.JSON_DATA));
                    } else {
                        isRequestSuccessful(false, response.getString(StringUtils.JSON_DATA));
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

    /**
     * Get all the events submitted by the specified user
     */
    public void getSubmittedEvents(Context context, String email) {
        //Create a JSON Object
        JSONObject object = new JSONObject();
        try {
            object.put(StringUtils.SUBMITTED_BY, email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, GET_SUBMITTED_EVENTS_REQUEST_ADDRESS, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString(StringUtils.JSON_STATUS).equals(StringUtils.JSON_SUCCESS)) {
                        isRequestSuccessful(true, response.getString(StringUtils.JSON_DATA));
                        //TODO: DO THE STORAGE AND ANYTHING ELSE HERE
                    } else {
                        isRequestSuccessful(false, response.getString(StringUtils.JSON_DATA));
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

    /**
     * Submite a comment for a particular event
     */
    public void submitCommentForEvent(Context context, JSONObject object) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, SUBMIT_COMMENT_REQUEST_ADDRESS, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString(StringUtils.JSON_STATUS).equals(StringUtils.JSON_SUCCESS)) {
                        isRequestSuccessful(true, response.getString(StringUtils.JSON_DATA));
                    } else {
                        isRequestSuccessful(false, response.getString(StringUtils.JSON_DATA));
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
                        isRequestSuccessful(true, response.getString(StringUtils.JSON_DATA));
                    } else {
                        isRequestSuccessful(false, response.getString(StringUtils.JSON_DATA));
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
