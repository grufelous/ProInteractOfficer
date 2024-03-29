package com.orion.prointeractofficer;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

public class LocationRequestHelper {
    final static String KEY_LOCATION_UPDATES_REQUESTED = "location-updates-requested";

    static void setRequesting(Context context, boolean value) {
        Log.i("GeofenceActivity", "setRequesting: setting request");
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_LOCATION_UPDATES_REQUESTED, value)
                .apply();
    }

    static boolean getRequesting(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_LOCATION_UPDATES_REQUESTED, false);
    }
}
