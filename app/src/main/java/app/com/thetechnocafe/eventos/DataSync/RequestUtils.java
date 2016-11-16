package app.com.thetechnocafe.eventos.DataSync;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class RequestUtils {
    private static final String SERVER_ADDRESS = "http://192.168.43.55:8080";
    private static final String LINK_EVENT_REQUEST = SERVER_ADDRESS + "/api/events";
    private static final String SIGN_UP_REQUEST_ADDRESS = SERVER_ADDRESS + "/api/signup";

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
}
