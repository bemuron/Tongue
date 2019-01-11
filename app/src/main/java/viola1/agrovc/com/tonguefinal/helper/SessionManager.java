package viola1.agrovc.com.tonguefinal.helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import java.util.HashMap;

import viola1.agrovc.com.tonguefinal.presentation.ui.activities.LoginActivity;

/*
*This class maintains session data across the app using shared prefs.
* We store a boolean flag isLoggedIn in shared prefs to check the login status
*
 */
public class SessionManager {
	// LogCat tag
	private static String TAG = SessionManager.class.getSimpleName();

	// Shared Preferences
	SharedPreferences pref;

	Editor editor;
	Context _context;

	// Shared pref mode
	int PRIVATE_MODE = 0;

	// Shared preferences file name
	private static final String PREF_NAME = "TongueUserPref";

	private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    //user id
    public static final String KEY_USER_ID = "userId";

    //username
    public static final String KEY_NAME = "name";

    //email
    public static final String KEY_EMAIL = "email";

    //user role (can be user or tutor)
    public static final String KEY_ROLE = "role";

    //user profile pic
    public static final String KEY_PROFILE_PIC = "profile_pic";

    //session in progress
    private static final String KEY_SESSION_IN_PROGRESS = "isSessionInProgress";

    //session: payment received
    private static final String KEY_SESSION_PAYMENT_RECEIVED = "isPaymentReceived";

    //session meeting id
    private static final String KEY_SESSION_MEETING_ID = "meeting_id";

    //session user id
    //id of the user who requested for a tutor
    //the student
    public static final String KEY_SESSION_USER_ID = "sessionUserId";

	public SessionManager(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	//create login session
    public void createLoginSession(int user_id, String name, String email, String role, String profile_pic){
        // Storing login value as TRUE
        editor.putBoolean(KEY_IS_LOGGED_IN, true);

        // Storing user id in pref
        editor.putInt(KEY_USER_ID, user_id);

        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);

        // Storing role in pref
        editor.putString(KEY_ROLE, role);

        // Storing role in pref
        editor.putString(KEY_PROFILE_PIC, profile_pic);

        // commit changes
        editor.commit();
    }

    /**create meeting in session session
     * The session pipe line
     * describe the procedural steps the session is at
    save the meeting details if the current user is in a meeting*/
    public void spMeetingInProgress(int meeting_id, int session_user_id){
        // Storing the meeting session as TRUE
        editor.putBoolean(KEY_SESSION_IN_PROGRESS, true);

        //the meeting id
        editor.putInt(KEY_SESSION_MEETING_ID, meeting_id);

        //the session user id
        editor.putInt(KEY_SESSION_USER_ID, session_user_id);

        //commit changes
        editor.commit();
    }

    /**
     *
     save the meeting details if the current user is in a meeting
     */
    public void spMeetingFinished(int meeting_id, int session_user_id){
        // Storing the meeting session as False
        editor.putBoolean(KEY_SESSION_IN_PROGRESS, false);

        //set that payment hasn't been received(false)
        editor.putBoolean(KEY_SESSION_PAYMENT_RECEIVED, false);

        //the meeting id
        editor.putInt(KEY_SESSION_MEETING_ID, meeting_id);

        //the session user id
        editor.putInt(KEY_SESSION_USER_ID, session_user_id);

        //commit changes
        editor.commit();
    }

    /**
     *
     save the meeting details if the current user is in a meeting
     */
    public void spPaymentReceived(int meeting_id){
        // Storing the meeting session as False
        editor.putBoolean(KEY_SESSION_PAYMENT_RECEIVED, true);

        //the meeting id
        editor.putInt(KEY_SESSION_MEETING_ID, meeting_id);

        //commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    //get user id
    public int  getUserId(){
        return pref.getInt(KEY_USER_ID, 0);
    }

    //get user role
    public String getUserRole(){
        return pref.getString(KEY_ROLE, null);
    }


    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user id
        user.put(KEY_USER_ID, pref.getString(KEY_USER_ID, null));

        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(_context, LoginActivity.class);

        // Closing all the Activities
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Starting Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

	public void setLogin(boolean isLoggedIn) {

		editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);

		// commit changes
		editor.commit();

		Log.d(TAG, "User login session modified!");
	}
}
