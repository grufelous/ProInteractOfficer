package com.orion.prointeractofficer;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class GeofenceActivity extends AppCompatActivity {
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;
    private List<Geofence> geofencesList;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 42;
    private static final String TAG = GeofenceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence);

        // log information that intent is created
        Log.i(TAG, "onCreate: ");

        // set intent to null
        geofencePendingIntent = null;

        // initialize geofence list
        geofencesList = new ArrayList<>();

        // create geofence instance
        geofencingClient = getGeofencingClientInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!checkPermissions()) {
            requestPermissions();
        } else {
            addGeofence();
        }
    }

    // setup geofence instance
    private GeofencingClient getGeofencingClientInstance() {
        return LocationServices.getGeofencingClient(this);
    }

    // create geofence objects
    private Geofence getNewGeofence(String geofenceId, double geofencePointLongitude, double geofencePointLatitude, float geofencePointRadius) {
        return new Geofence.Builder()
                .setRequestId(geofenceId)
                .setCircularRegion(geofencePointLongitude, geofencePointLatitude, geofencePointRadius)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .setExpirationDuration(60*1000)
                .build();
    }

    // setup initial triggers
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofencesList);
        return builder.build();
    }

    // geofence pending intent
    private PendingIntent getGeofencePendingIntent() {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceTransitionsIntentService.class);
        geofencePendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    // add geofence
    private void addGeofence() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // yay! geofence added!
                        Log.d(TAG, "Geofence loaded");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // :(
                        Log.d(TAG, "Failed to load geofence with error\n" + e.getStackTrace());
                    }
                });
    }

    // check location permissions
    private boolean checkPermissions() {
        int permission = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        return permission == PackageManager.PERMISSION_GRANTED;
    }

    // request location permissions
    private void requestPermissions() {
        boolean shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if(shouldShowRationale) {
            // TODO
            // user denied
            // request permission again
        } else {
            Log.i(TAG, "requestPermissions: ");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
