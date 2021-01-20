package com.app.mobileboxingvr.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.app.mobileboxingvr.constants.MyConstants;
import com.app.mobileboxingvr.helpers.StepCounter;
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

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private StepCounter stepCounter;
    private UserService user;
    private String userID;

    private ActivityService(Context context) {
        this.context = context;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user_activity");

        stepCounter = StepCounter.getInstance(context);
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
        int currentValue = stepCounter.getStepCounterValue();

        SharedPreferences pref = context.getSharedPreferences(MyConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        int previousValue = pref.getInt(MyConstants.CURRENT_STEP_COUNTER_VALUE, -1);

        saveCurrentStepCounterValue(currentValue);
        
        Log.d(TAG, "getStepCounterValue: prev => " + previousValue + ", current " + currentValue);

        if (previousValue == MyConstants.DEFAULT_VALUE) {
            return MyConstants.DEFAULT_VALUE;
        }

        int diff = currentValue - previousValue;

        return diff;
    }

    public int getTimeSpent() {
        long currentValue = System.currentTimeMillis();

        SharedPreferences pref = context.getSharedPreferences(MyConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        long previousValue = pref.getLong(MyConstants.CURRENT_TIMESTAMP_VALUE, -1);

        saveCurrentTimestampValue(currentValue);

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

    public void saveUserActivity(UserActivity userActivity) {
        myRef.child(userID).push().setValue(userActivity);
    }

    private void saveCurrentStepCounterValue(int stepCounterValue) {
        SharedPreferences pref = context.getSharedPreferences(MyConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        // keep current value
        editor.putInt(MyConstants.CURRENT_STEP_COUNTER_VALUE, stepCounterValue);
        editor.apply();
    }

    public void saveCurrentTimestampValue(long timestamp) {
        SharedPreferences pref = context.getSharedPreferences(MyConstants.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        // keep current value
        editor.putLong(MyConstants.CURRENT_TIMESTAMP_VALUE, timestamp);
        editor.apply();
    }
}
