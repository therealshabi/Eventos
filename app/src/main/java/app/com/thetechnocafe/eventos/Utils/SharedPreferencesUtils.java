package app.com.thetechnocafe.eventos.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gurleensethi on 21/11/16.
 */

public class SharedPreferencesUtils {
    private static final String SHARED_PREFERENCES_FILE = "preferences";
    private static final String SHARED_PREFERENCES_USERNAME = "username";
    private static final String SHARED_PREFERENCES_PASSWORD = "password";
    private static final String SHARED_PREFERENCES_LOGIN_STATE = "login_state";
    private static final String SHARED_PREFERENCES_FULL_NAME = "full_name";
    private static final String SHARED_PREFERENCES_PHONE_NUMBER = "phone_number";
    private static final String SHARED_PREFERENCS_WALKTHROUGH_STATE = "walk_through";
    private static final String TOGGLE_STATE = "toggle_button_state";

    /**
     * Change the username in sharedpreferences
     */
    public static void setUsername(Context context, String username) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        //Get editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Add username
        editor.putString(SHARED_PREFERENCES_USERNAME, username);

        editor.commit();
    }

    /**
     * Get the username from shared preferences
     */
    public static String getUsername(Context context) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        return sharedPreferences.getString(SHARED_PREFERENCES_USERNAME, null);
    }

    /**
     * Change the password in sharedpreferences
     */
    public static void setPassword(Context context, String password) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        //Get editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Add username
        editor.putString(SHARED_PREFERENCES_PASSWORD, password);

        editor.commit();
    }

    /**
     * Get the password from shared preferences
     */
    public static String getPassword(Context context) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        return sharedPreferences.getString(SHARED_PREFERENCES_PASSWORD, null);
    }

    /**
     * Set login state in shared preferences
     */
    public static void setLoginState(Context context, boolean isLoggedIn) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        //Get editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Add value to editor
        editor.putBoolean(SHARED_PREFERENCES_LOGIN_STATE, isLoggedIn);

        editor.commit();
    }

    /**
     * Get login state from shared preferences
     */
    public static boolean getLoginState(Context context) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(SHARED_PREFERENCES_LOGIN_STATE, false);
    }

    /**
     * Set full name in shared preferences
     */
    public static void setFullName(Context context, String fullName) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        //Get editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Add value to editor
        editor.putString(SHARED_PREFERENCES_FULL_NAME, fullName);

        editor.commit();
    }

    /**
     * Get full name from shared preferences
     */
    public static String getFullName(Context context) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        return sharedPreferences.getString(SHARED_PREFERENCES_FULL_NAME, null);
    }

    /**
     * Set login state in shared preferences
     */
    public static void setPhoneNumber(Context context, String phoneNumber) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        //Get editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Add value to editor
        editor.putString(SHARED_PREFERENCES_PHONE_NUMBER, phoneNumber);

        editor.commit();
    }

    /**
     * Get login state from shared preferences
     */
    public static String getPhoneNumber(Context context) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        return sharedPreferences.getString(SHARED_PREFERENCES_PHONE_NUMBER, null);
    }

    public static void setRating(Context context, String eventId, int rating) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(eventId, rating);
        editor.commit();
    }

    public static int getRating(Context context, String eventId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(eventId, 0);
    }

    public static void setSharedPreferencsWalkthroughState(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREFERENCS_WALKTHROUGH_STATE, true);
        editor.commit();
    }

    public static boolean getSharedPreferencsWalkthroughState(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SHARED_PREFERENCS_WALKTHROUGH_STATE, false);
    }

    //Toggle State of Event Attending or Not Attending
    public static void setSharedPreferencesToggleState(Context context, String id, boolean state) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(id + TOGGLE_STATE, state);
        editor.commit();
    }

    public static boolean getSharedPreferencesToggleState(Context context, String id) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(id + TOGGLE_STATE, false);
    }
}
