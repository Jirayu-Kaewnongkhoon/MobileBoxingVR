package com.app.mobileboxingvr.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.util.Log;

import com.app.mobileboxingvr.constants.MyConstants;
import com.app.mobileboxingvr.models.UserActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ActivityService {

    private static final String TAG = "ActivityService";

    private static ActivityService instance;

    private Context context;

    private SharedPreferences pref;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private UserService user;
    private String userID;

    private ActivityService(Context context) {
        this.context = context;

        pref = context.getSharedPreferences(MyConstants.SHARED_PREFS, Context.MODE_PRIVATE);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user_activity");

        user = UserService.getInstance();
        userID = user.getCurrentUser().getUid();
    }

    public static ActivityService getInstance(Context context) {
        if (instance == null) {
            instance = new ActivityService(context);
        }
        return instance;
    }

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

    public String getTimestamp() {
        Date dNow = new Date();

        SimpleDateFormat ft = new SimpleDateFormat("dd.MM 'at' HH:mm:ss");
        ft.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));

        String timestamp = ft.format(dNow);

        return timestamp;
    }

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

    public double getSpeed() {
        double distance = getDistance(MyConstants.SELF_ACCESS);
        double timeSpent = getTimeSpent(MyConstants.SELF_ACCESS) * MyConstants.SECOND;
        double speed = distance / timeSpent;

        Log.d(TAG, "getSpeed: distance => " + distance);
        Log.d(TAG, "getSpeed: time => " + timeSpent);
        Log.d(TAG, "getSpeed: speed => " + speed);

        return speed;
    }

    public void saveUserActivity(UserActivity userActivity) {
        getUserActivity().push().setValue(userActivity);
    }

    public DatabaseReference getUserActivity() {
        return myRef.child(userID);
    }

    private void saveCurrentStepCounterValue(int stepCounterValue) {
        SharedPreferences.Editor editor = pref.edit();

        // keep current value
        editor.putInt(MyConstants.CURRENT_STEP_COUNTER_VALUE, stepCounterValue);
        editor.apply();
    }

    public void saveCurrentTimestampValue(long timestamp) {
        SharedPreferences.Editor editor = pref.edit();

        // keep current value
        editor.putLong(MyConstants.CURRENT_TIMESTAMP_VALUE, timestamp);
        editor.apply();
    }
}
