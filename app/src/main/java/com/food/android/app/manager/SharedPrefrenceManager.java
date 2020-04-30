package com.food.android.app.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.food.android.app.App;

/**
 * Created by Aleesha Kanwal on 19/05/2017.
 */

public class SharedPrefrenceManager {

    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor prefsEditor;
    private String sharedPreferencesName = "AppPrefs";
    static private SharedPrefrenceManager _instance;

    public SharedPrefrenceManager(Context appCtx) {
        sharedPreferences =
                appCtx.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();
        prefsEditor.apply();
    }

    public static SharedPrefrenceManager getInstance() {
        if (_instance == null) {
            synchronized (SharedPrefrenceManager.class) {
                if (_instance == null)
                    _instance = new SharedPrefrenceManager(App.getAppContext());
            }
        }
        return _instance;
    }


    public static void putString(String key, String value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void putInt(String key, int value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    public static void putBoolean(String key, Boolean value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);

        editor.commit();
    }

    public static void putLong(String key, long value, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);

        editor.commit();
    }

    public static String getString(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }

    public static int getInt(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, 0);
    }

    public static Boolean getBoolean(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, false);
    }

    public static long getLong(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(key, 0L);
    }

    public static void removeKey(String key, Context context) {
        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove(key);
        editor.apply();
    }
}
