package viola1.agrovc.com.tonguefinal.constants;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by VIOLA1 on 12-Oct-17.
 */

/*
* Has shared preferences constants
* */
public class AppPrefs {

    public static String APP_PREFS = "APP_PREFERENCES";
    public static String PREF_EMAIL = "memberEmail";
    public static String PREF_PASSWORD = "memberPassword";
    public static String PREF_USER_TYPE = "userType";

    public static String PREF_USER_ID = "id";


    /*
    * Clears stored user Password
    * */
    public static void clearLoginCredentials(Context context) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(APP_PREFS, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(PREF_PASSWORD).apply();

    }
}
