package com.orion.prointeractofficer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import java.util.List;

public class LocationUpdatesBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = BroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: calling on receive");
        LocationUpdatesIntentService.enqueueWork(context, intent);
    }
}
