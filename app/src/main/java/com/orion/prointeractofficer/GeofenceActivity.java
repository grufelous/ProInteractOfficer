package com.orion.prointeractofficer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class GeofenceActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        SharedPreferences.OnSharedPreferenceChangeListener {
    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;
    private List<Geofence> geofencesList;
    TextView geoText;
    Button setupBtn;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 42;
    private static final int GEOFENCE_EXPIRATION_DURATION = 1000 * 60 * 60; // milliseconds * seconds * minutes
    private static final int GEOFENCE_LOITERTING_DELAY = 1000 * 30; // milliseconds * seconds
    private static final String TAG = GeofenceActivity.class.getSimpleName();

    public void launchOfficer() {
        Intent setupIntent = new Intent(this, OfficerAuthActivity.class);
        startActivity(setupIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geofence);
        setupBtn = findViewById(R.id.setupAct);

        // inits for location updates
        buildGoogleApiClient();

        // log information that intent is created
        Log.i(TAG, "onCreate: GeofenceActivity loaded");

        // set intent to null
        geofencePendingIntent = null;

        // initialize geofence list
        geofencesList = new ArrayList<>();

        // add geofence
        geofencesList.add(getNewGeofence("originalGeofence", 31.253072, 75.704507, 75));

        // create geofence instance
        geofencingClient = getGeofencingClientInstance();

        // add geofence to client
        addGeofence();

        geoText = findViewById(R.id.geo);
        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchOfficer();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(!checkPermissions()) {
            requestPermissions();
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
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_DWELL)
                .setExpirationDuration(GEOFENCE_EXPIRATION_DURATION)
                .setLoiteringDelay(GEOFENCE_LOITERTING_DELAY)
                .build();
    }

    // setup initial triggers
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_EXIT)
                .addGeofences(geofencesList);
        return builder.build();
    }

    // geofence pending intent
    private PendingIntent getGeofencePendingIntent() {
        Log.i(TAG, "getGeofencePendingIntent: created intent");
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    // add geofence
    @SuppressLint("MissingPermission")
    private void addGeofence() {
        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // yay! geofence added!
                        Log.d(TAG, "Successfully added Geofence");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // :(
                        Log.d(TAG, "Failed to add geofence with error" + e);
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
            showPermissionRequestAlertDialog();
        } else {
            Log.i(TAG, "requestPermissions: requesting permissions normally");
            callPermissionSystemDialog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionsResult: ");
        if(requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if(grantResults.length <= 0)  {
                Log.i(TAG, "onRequestPermissionsResult: something happened that should not");
            }  else if(grantResults[0] == PackageManager.PERMISSION_DENIED) {
                boolean shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                if(!shouldShowRationale) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this)
                            .setTitle(R.string.location_permission_title)
                            .setMessage(R.string.location_permission_message)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, REQUEST_PERMISSIONS_REQUEST_CODE);
                                }
                            });

                    AlertDialog permissionDialog = builder.create();

                    permissionDialog.show();
                } else {
                    Log.i(TAG, "onRequestPermissionsResult: showing permission dialog");
                    showPermissionRequestAlertDialog();
                }
            }  else {
                addGeofence();
            }
        }
    }

    private void showPermissionRequestAlertDialog() {
        Log.i(TAG, "showPermissionRequestAlertDialog: showing permission dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.location_permission_title)
                .setMessage(R.string.location_permission_message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callPermissionSystemDialog();
                    }
                });

        AlertDialog permissionDialog = builder.create();

        permissionDialog.show();
    }

    private void callPermissionSystemDialog() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    // code for location updates activity
    // TODO: create a new activity 'Location Updates' and run it whenever geofence activity is called
    private static final long UPDATE_INTERVAL = 10 * 1000;
    private static final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;
    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL * 3;

    // create a location request variable
    private LocationRequest mLocationRequest;
    // create google api client
    private GoogleApiClient mGoogleApiClient;

    private void createLocationRequest() {
        Log.i(TAG, "createLocationRequest: successfully created location request");
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildGoogleApiClient() {
        if(mGoogleApiClient != null) {
            Log.e(TAG, "buildGoogleApiClient: ApiClient already created");
            return;
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    private PendingIntent getLocationUpdatesPendingIntent() {
        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(LocationResultHelper.KEY_LOCATION_UPDATES_RESULT)) {
            Log.i(TAG, "onSharedPreferenceChanged: Logging from UI " + LocationResultHelper.getSavedLocationResult(this));
        } else if (key.equals(LocationRequestHelper.KEY_LOCATION_UPDATES_REQUESTED)) {
            Log.i(TAG, "onSharedPreferenceChanged: Logging from UI " + LocationRequestHelper.getRequesting(this));
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "onConnected: GoogleApiClient connected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        final String text = "Connection suspended";
        Log.w(TAG, text + ": Error code: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        final String text = "Exception while connecting to Google Play services";
        Log.w(TAG, text + ": " + connectionResult.getErrorMessage());
    }

    public void requestLocationUpdates() {
        try {
            Log.i(TAG, "Starting location updates");
            LocationRequestHelper.setRequesting(this, true);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, getLocationUpdatesPendingIntent());
        } catch (SecurityException e) {
            LocationRequestHelper.setRequesting(this, false);
            e.printStackTrace();
        }
    }

    public void removeLocationUpdates(View view) {
        Log.i(TAG, "Removing location updates");
        LocationRequestHelper.setRequesting(this, false);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                getLocationUpdatesPendingIntent());
    }

    public void clicked(View view) {
        requestLocationUpdates();
    }

    public void startService(View view) {
//        PackageManager pm  = this.getPackageManager();
//        ComponentName componentName = new ComponentName(this, LocationUpdatesBroadcastReceiver.class);
//        int status = getApplicationContext().getPackageManager().getComponentEnabledSetting(componentName);
//        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
//        Log.e(TAG,status + "");
        requestLocationUpdates();
    }

    public void stopService(View view) {
//        PackageManager pm  = this.getPackageManager();
//        ComponentName componentName = new ComponentName(this, LocationUpdatesBroadcastReceiver.class);
//        int status = getApplicationContext().getPackageManager().getComponentEnabledSetting(componentName);
//        pm.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                PackageManager.DONT_KILL_APP);
//        Log.e(TAG,status + "");
        removeLocationUpdates(view);
    }
}
