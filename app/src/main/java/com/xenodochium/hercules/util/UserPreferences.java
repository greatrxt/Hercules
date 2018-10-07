package com.xenodochium.hercules.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.xenodochium.hercules.application.Hercules;

public class UserPreferences {
    //Shared Preferences Keys
    private static final String HERCULES = "herculesUserPreferences";

    /**
     * Put boolean
     *
     * @param key
     * @param value
     */
    private static void putBoolean(String key, boolean value) {
        SharedPreferences pref = Hercules.getInstance().getApplicationContext().getSharedPreferences(HERCULES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    /**
     * Fetch boolean
     *
     * @param key
     * @return
     */
    private static boolean getBoolean(String key) {
        SharedPreferences pref = Hercules.getInstance().getApplicationContext().getSharedPreferences(HERCULES, Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    /**
     * Fetch String from SharedPreferences
     *
     * @param key
     * @return
     */
    private static String getString(String key) {
        SharedPreferences pref = Hercules.getInstance().getApplicationContext().getSharedPreferences(HERCULES, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    /**
     * Fetch integer from SharedPreferences
     *
     * @param key
     * @return
     */
    private static Integer getInteger(String key) {
        SharedPreferences pref = Hercules.getInstance().getApplicationContext().getSharedPreferences(HERCULES, Context.MODE_PRIVATE);
        return pref.getInt(key, -999);
    }

    /**
     * Put string in SharedPreferences
     *
     * @param key
     * @param value
     */
    private static void putString(String key, String value) {
        SharedPreferences pref = Hercules.getInstance().getApplicationContext().getSharedPreferences(HERCULES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Put integer in SharedPreferences
     *
     * @param key
     * @param value
     */
    private static void putInteger(String key, Integer value) {
        SharedPreferences pref = Hercules.getInstance().getApplicationContext().getSharedPreferences(HERCULES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    public static boolean isFirstTime() {
        SharedPreferences pref = Hercules.getInstance().getApplicationContext().getSharedPreferences(HERCULES, Context.MODE_PRIVATE);
        boolean isFirstTime = pref.getBoolean("isFirstTime", true);
        if (isFirstTime) {
            putBoolean("isFirstTime", false);
        }
        return isFirstTime;
    }
}
