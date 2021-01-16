package com.app.mobileboxingvr.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.app.mobileboxingvr.helpers.StepCounter;
import com.app.mobileboxingvr.models.GameProfile;
import com.app.mobileboxingvr.models.UserActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ActivityService {

    private static final String TAG = "ActivityService";

    private final String SHARED_PREFS = "UserActivity";
    private final String CURRENT_STEP_COUNTER_VALUE = "CurrentStepCounterValue";

    private static ActivityService instance;

    private Context context;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private StepCounter stepCounter;
    private UserService user;
    private String userID;

    private final int TIME_SPENT = 60;

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

        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        int previousValue = pref.getInt(CURRENT_STEP_COUNTER_VALUE, 0);
        int diff = currentValue - previousValue;

        Log.d(TAG, "getStepCounterValue: prev => " + previousValue + ", current " + currentValue);

        saveCurrentStepCounterValue(currentValue);

        return diff;
    }

    public int getTimeSpent() {
        // TODO : define time spent for 1 interval activity

        return TIME_SPENT;
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
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        // keep current value
        editor.putInt(CURRENT_STEP_COUNTER_VALUE, stepCounterValue);
        editor.apply();
    }
}
