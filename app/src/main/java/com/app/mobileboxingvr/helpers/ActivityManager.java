package com.app.mobileboxingvr.helpers;

import android.content.Context;
import android.content.SharedPreferences;
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

    private Context context;

    private SharedPreferences pref;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private UserManager user;
    private String userID;

    private ActivityManager(Context context) {
        this.context = context;

        pref = context.getSharedPreferences(MyConstants.SHARED_PREFS, Context.MODE_PRIVATE);

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
        int currentValue = pref.getInt(MyConstants.STEP_COUNTER_VALUE, 0);

        int previousValue = pref.getInt(MyConstants.CURRENT_STEP_COUNTER_VALUE, MyConstants.DEFAULT_VALUE);

        saveCurrentStepCounterValue(currentValue);
        
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

    public int getTimeSpent(int requestCode) {
        long currentValue = System.currentTimeMillis();

        long previousValue = pref.getLong(MyConstants.CURRENT_TIMESTAMP_VALUE, MyConstants.DEFAULT_VALUE);

        if (requestCode == MyConstants.WORK_ACCESS) {
            saveCurrentTimestampValue(currentValue);
        }

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
     *  and it will reset after method was called with WORK_ACCESS request code
     */

    public double getDistance(int requestCode) {
        double distance = Double.longBitsToDouble(pref.getLong(MyConstants.DISTANCE_VALUE, 0));

        if (requestCode == MyConstants.WORK_ACCESS) {
            // reset distance value for next round
            SharedPreferences.Editor editor = pref.edit();
            editor.putLong(MyConstants.DISTANCE_VALUE, 0);
            editor.apply();
        }

        return distance;
    }

    /**
     *  --getSpeed--
     *  Get total distance value with SELF_ACCESS request code and get time spent
     *  then calculate to velocity with v = s/t
     */

    public double getSpeed() {
        double distance = getDistance(MyConstants.SELF_ACCESS);
        double timeSpent = getTimeSpent(MyConstants.SELF_ACCESS) * MyConstants.SECOND;
        double speed = distance / timeSpent;

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

    /**
     *  --saveCurrentStepCounterValue--
     *  Save current step counter to use for previous value in next round
     */

    private void saveCurrentStepCounterValue(int stepCounterValue) {
        SharedPreferences.Editor editor = pref.edit();

        // keep current value
        editor.putInt(MyConstants.CURRENT_STEP_COUNTER_VALUE, stepCounterValue);
        editor.apply();
    }

    /**
     *  --saveCurrentTimestampValue--
     *  Save current timestamp to use for previous value in next round
     */

    public void saveCurrentTimestampValue(long timestamp) {
        SharedPreferences.Editor editor = pref.edit();

        // keep current value
        editor.putLong(MyConstants.CURRENT_TIMESTAMP_VALUE, timestamp);
        editor.apply();
    }
}
