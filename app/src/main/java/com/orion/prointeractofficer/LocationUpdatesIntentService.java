package com.orion.prointeractofficer;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LocationUpdatesIntentService extends IntentService {
    public static final String TAG = IntentService.class.getSimpleName();

    public LocationUpdatesIntentService() {
        super("LocationUpdatesIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null) {
            LocationResult locationResult = LocationResult.extractResult(intent);
            if(locationResult != null) {
                List<Location> locations = locationResult.getLocations();
                LocationResultHelper locationResultHelper = new LocationResultHelper(
                        this, locations);
                // Save the location data to SharedPreferences.
                locationResultHelper.saveResults();
                // Show notification with the location data.
                locationResultHelper.showNotification();
                Log.i(TAG, LocationResultHelper.getSavedLocationResult(this));
            }
        }
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
