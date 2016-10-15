package app.com.thetechnocafe.eventos.DataSync;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by gurleensethi on 15/10/16.
 * Singleton class to maintain a single request queue over the lifetime of the app
 */

public class VolleyQueue {
    private static VolleyQueue mInstance;
    private RequestQueue mRequestQueue;
    private static Context mContext;

    //Private constructor to make class singleton
    private VolleyQueue(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    //Return instance of this class
    public static VolleyQueue getInstance(Context context) {
        //Check if function is called first time
        if (mInstance == null) {
            mInstance = new VolleyQueue(context);
        }
        return mInstance;
    }

    //Return the request queue
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }
}
