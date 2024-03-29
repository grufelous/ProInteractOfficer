package com.orion.prointeractofficer;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class GeofenceTransitionsJobIntentService extends JobIntentService {
    FirebaseUser user;
    DatabaseReference rtDB;
    public GeofenceTransitionsJobIntentService() {
        super();
        user = FirebaseAuth.getInstance().getCurrentUser();
        rtDB = FirebaseDatabase.getInstance().getReference();
    }
    private static final int JOB_ID = 215;

    private static final String TAG = GeofenceTransitionsJobIntentService.class.getSimpleName();

    public static void enqueueWork(Context context, Intent intent) {
        Log.i(TAG, "enqueueWork: ");
        enqueueWork(context, GeofenceTransitionsJobIntentService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(Intent intent) {
        Log.i(TAG, "onHandleIntent: ");
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
        rtDB.child("officer").child(user.getUid()).child("available").setValue(false);
        //rtDB.child("officer").child(user.getUid()).child("last_time").setValue(System.currentTimeMillis());
        Log.i(TAG, "handleGeofenceExit: ");
    }

    private void handleGeofenceDwell() {
        Log.i(TAG, "handleGeofenceDwell: ");
    }

    private void handleGeofenceEntry() {
        rtDB.child("officer").child(user.getUid()).child("available").setValue(true);
        //rtDB.child("officer").child(user.getUid()).child("geo").setValue(true);
        Log.i(TAG, "handleGeofenceEntry: ");
    }
}
