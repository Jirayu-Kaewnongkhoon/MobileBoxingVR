package com.app.mobileboxingvr.services;

import android.content.Context;
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

    private static ActivityService instance;

    private Context context;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private StepCounter stepCounter;
    private GameService game;
    private UserService user;
    private String userID;

    private GameProfile gameProfile;

    private final int TIME_SPENT = 60;

    private ActivityService(Context context) {
        this.context = context;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user_activity");

        stepCounter = StepCounter.getInstance(context);
        game = GameService.getInstance();
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
        int stepCounterValue = stepCounter.getStepCounterValue();

        return stepCounterValue;
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
}
