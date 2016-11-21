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
}
