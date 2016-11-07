package app.com.thetechnocafe.eventos.DataSync;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by gurleensethi on 16/10/16.
 */

public abstract class RequestUtils {
    private static final String LINK_EVENT_REQUEST = "http://192.168.43.55:8000/api/events";

    public abstract void isRequestSuccessful(boolean isSuccessful);

    /**
     * Submit forum to network
     * requires
     */
    public void submitForumToServer(Context context, final JSONObject jsonObject) {
        try {
            Toast.makeText(context, jsonObject.get(StringUtils.JSON_LINKS).toString(), Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Create new JSON Object Request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, LINK_EVENT_REQUEST, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Check if response if not null
                if (response != null) {
                    //Check for success
                    try {
                        if (response.getString(StringUtils.JSON_STATUS).equals(StringUtils.JSON_SUCCESS)) {
                            isRequestSuccessful(true);
                        } else {
                            isRequestSuccessful(false);
                        }
                    } catch (JSONException e) {
                        isRequestSuccessful(false);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Notify error
                isRequestSuccessful(false);
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
}
