package com.app.mobileboxingvr.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.app.mobileboxingvr.constants.MyConstants;

public class SharedPreferenceManager {

    private static final String TAG = "SharedPreferenceManager";

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    public SharedPreferenceManager(Context context) {
        pref = context.getSharedPreferences(MyConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    /**
     *  --saveEveryLocation--
     *  Save trigger value from LocationTracking to SharedPreference
     */

    public void saveEveryLocation(double lat, double lng) {
        // save every trigger value to fix when initialize sensor
        editor.putLong(MyConstants.LATITUDE_VALUE, Double.doubleToRawLongBits(lat));
        editor.putLong(MyConstants.LONGITUDE_VALUE, Double.doubleToRawLongBits(lng));
        editor.apply();
    }

    /**
     *  --saveDistance--
     *  Calculate distance from previous location to current location
     *  and then save to SharedPreference
     */

    public void saveDistance(double currentLatitude, double currentLongitude) {
        // set current location
        Location currentLocation = new Location("");
        currentLocation.setLatitude(currentLatitude);
        currentLocation.setLongitude(currentLongitude);

        long previousLatitude = pref.getLong(MyConstants.LATITUDE_VALUE, MyConstants.EXCLUDE_VALUE);
        long previousLongitude = pref.getLong(MyConstants.LONGITUDE_VALUE, MyConstants.EXCLUDE_VALUE);
        Log.d(TAG, "saveDistance: previous lat " + Double.longBitsToDouble(previousLatitude));
        Log.d(TAG, "saveDistance: previous lng " + Double.longBitsToDouble(previousLongitude));

        if (previousLatitude == MyConstants.EXCLUDE_VALUE
                && previousLongitude == MyConstants.EXCLUDE_VALUE) {
            Log.d(TAG, "saveDistance: EX");
            return;
        }

        // set previous location
        Location previousLocation = new Location("");
        previousLocation.setLatitude(Double.longBitsToDouble(previousLatitude));
        previousLocation.setLongitude(Double.longBitsToDouble(previousLongitude));

        // calculate distance & save to SharedPreference
        double distance = previousLocation.distanceTo(currentLocation);
        double previousDistance = getTotalDistance();
        double totalDistance = previousDistance + distance;

        Log.d(TAG, "saveDistance: distance => " + distance);
        Log.d(TAG, "saveDistance: previous => " + previousDistance);
        Log.d(TAG, "saveDistance: total => " + totalDistance);

        editor.putLong(MyConstants.DISTANCE_VALUE, Double.doubleToRawLongBits(totalDistance));
        editor.apply();
    }

    /**
     *  --saveEveryStepCounterValue--
     *  Save trigger value from StepCounter to SharedPreference
     */

    public void saveEveryStepCounterValue(int stepCounterValue) {
        // save every trigger value
        editor.putInt(MyConstants.STEP_COUNTER_VALUE, stepCounterValue);
        editor.apply();
    }

    /**
     *  --saveCurrentStepCounterValue--
     *  Save current step counter to use for previous value in next round
     */

    public void saveCurrentStepCounterValue(int stepCounterValue) {
        // keep current value
        editor.putInt(MyConstants.PREVIOUS_STEP_COUNTER_VALUE, stepCounterValue);
        editor.apply();
    }

    /**
     *  --saveCurrentTimestampValue--
     *  Save current timestamp to use for previous value in next round
     */

    public void saveCurrentTimestampValue(long timestamp) {
        // keep current value
        editor.putLong(MyConstants.PREVIOUS_TIMESTAMP_VALUE, timestamp);
        editor.apply();
    }

    /**
     *  --getStepCounterValue--
     *  Get current step counter value from SharedPreference
     */

    public int getStepCounterValue() {
        return pref.getInt(MyConstants.STEP_COUNTER_VALUE, 0);
    }

    /**
     *  --getPreviousStepCounterValue--
     *  Get previous step counter value from SharedPreference
     */

    public int getPreviousStepCounterValue() {
        return pref.getInt(MyConstants.PREVIOUS_STEP_COUNTER_VALUE, MyConstants.DEFAULT_VALUE);
    }

    /**
     *  --getPreviousTimestampValue--
     *  Get previous timestamp value from SharedPreference
     */

    public long getPreviousTimestampValue() {
        return pref.getLong(MyConstants.PREVIOUS_TIMESTAMP_VALUE, MyConstants.DEFAULT_VALUE);
    }

    /**
     *  --getTotalDistance--
     *  Get total distance as Double from SharedPreference
     */

    public double getTotalDistance() {
        return Double.longBitsToDouble(pref.getLong(MyConstants.DISTANCE_VALUE, 0));
    }

    /**
     *  --resetTotalDistance--
     *  Reset total distance for next round
     */

    public void resetTotalDistance() {
        editor.putLong(MyConstants.DISTANCE_VALUE, 0);
        editor.apply();
    }
}
