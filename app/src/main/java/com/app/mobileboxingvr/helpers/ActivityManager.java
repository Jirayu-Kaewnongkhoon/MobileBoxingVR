package com.app.mobileboxingvr.helpers;

import android.content.Context;
import android.util.Log;

import com.app.mobileboxingvr.constants.MyConstants;
import com.app.mobileboxingvr.models.UserActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ActivityManager {

    private static final String TAG = "ActivityService";

    private static ActivityManager instance;

    private SharedPreferenceManager pref;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private UserManager user;
    private String userID;

    private ActivityManager(Context context) {
        pref = new SharedPreferenceManager(context);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user_activity");

        user = UserManager.getInstance();
        userID = user.getCurrentUser().getUid();
    }

    public static ActivityManager getInstance(Context context) {
        if (instance == null) {
            instance = new ActivityManager(context);
        }
        return instance;
    }

    /**
     *  --getStepCounterValue--
     *  Get current step counter value from SharedPreference
     *  and subtract from previous value
     */

    public int getStepCounterValue() {
        int currentValue = pref.getStepCounterValue();

        int previousValue = pref.getPreviousStepCounterValue();

        pref.saveCurrentStepCounterValue(currentValue);
        
        Log.d(TAG, "getStepCounterValue: prev => " + previousValue + ", current " + currentValue);

        if (previousValue == MyConstants.DEFAULT_VALUE) {
            return MyConstants.DEFAULT_VALUE;
        }

        int diff = currentValue - previousValue;

        return diff;
    }

    /**
     *  --getTimeSpent--
     *  Get current timestamp value from SharedPreference
     *  and subtract from previous value
     */

    public int getTimeSpent() {
        long currentValue = System.currentTimeMillis();

        long previousValue = pref.getPreviousTimestampValue();

        pref.saveCurrentTimestampValue(currentValue);

        Log.d(TAG, "getTimeSpent: prev => " + previousValue + ", current " + currentValue);

        if (previousValue == MyConstants.DEFAULT_VALUE) {
            return MyConstants.DEFAULT_VALUE;
        }

        int timeSpent = Math.round((currentValue - previousValue) / MyConstants.MILLI_SECOND);

        return timeSpent;
    }

    /**
     *  --getTimestamp--
     *  Get current timestamp value
     */

    public String getTimestamp() {
        Date dNow = new Date();

        SimpleDateFormat ft = new SimpleDateFormat("dd.MM 'at' HH:mm:ss");
        ft.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));

        String timestamp = ft.format(dNow);

        return timestamp;
    }

    /**
     *  --getDistance--
     *  Get total distance value from SharedPreference
     */

    public double getDistance() {
        double distance = pref.getTotalDistance();

        // reset distance value for next round
        pref.resetTotalDistance();

        return distance;
    }

    /**
     *  --getSpeed--
     *  Get total distance value with SELF_ACCESS request code and get time spent
     *  then calculate to velocity with v = s/t
     */

    public double getSpeed(double distance, int timeSpent) {
        double speed = distance / ( timeSpent * MyConstants.SECOND );

        Log.d(TAG, "getSpeed: distance => " + distance);
        Log.d(TAG, "getSpeed: time => " + timeSpent);
        Log.d(TAG, "getSpeed: speed => " + speed);

        return speed;
    }

    /**
     *  --saveUserActivity--
     *  Save activity logs to database
     */

    public void saveUserActivity(UserActivity userActivity) {
        getUserActivity().push().setValue(userActivity);
    }

    /**
     *  --getUserActivity--
     *  Get activity logs from database
     */

    public DatabaseReference getUserActivity() {
        return myRef.child(userID);
    }
}
