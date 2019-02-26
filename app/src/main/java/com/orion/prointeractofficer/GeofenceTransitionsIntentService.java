package com.orion.prointeractofficer;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class GeofenceTransitionsIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "com.orion.prointeractofficer.action.FOO";
    public static final String ACTION_BAZ = "com.orion.prointeractofficer.action.BAZ";
    private static final String TAG = GeofenceTransitionsIntentService.class.getSimpleName();

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "com.orion.prointeractofficer.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.orion.prointeractofficer.extra.PARAM2";

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
        Log.i(TAG, "GeofenceTransitionsIntentService: ");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "onHandleIntent: service loaded");
        if (intent != null) {
            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
            if(geofencingEvent.hasError()) {
                int errorCode = geofencingEvent.getErrorCode();
                String errorMessage = GeofenceStatusCodes.getStatusCodeString(errorCode);
                Log.e(TAG, "onHandleIntent: Error Code: " + errorCode + " Error Message: " + errorMessage);
                return;
            }

            int geofenceTransition = geofencingEvent.getGeofenceTransition();

            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                    geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL ||
                    geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                List<Geofence> triggeredGeofences = geofencingEvent.getTriggeringGeofences();

                // TODO: get data from triggeredGeofences and pass as result
                String geofenceTransitionDetails = "Something triggered";

                // TODO: show notification instead
                Log.d(TAG, "onHandleIntent: " + geofenceTransitionDetails);
            }  else {
                Log.e(TAG, "onHandleIntent: failed with " + geofenceTransition);
            }

            if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
                handleGeofenceEntry();
            } else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL) {
                handleGeofenceDwell();
            } else if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
                handleGeofenceExit();
            }
        }
    }

    // TODO: handle these methods
    private void handleGeofenceExit() {
    }

    private void handleGeofenceDwell() {
    }

    private void handleGeofenceEntry() {
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
