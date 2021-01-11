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
    private UserService user;
    private String userID;

    private GameProfile gameProfile;

    private final int TIME_SPENT = 60;

    private ActivityService(Context context) {
        this.context = context;

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

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

    public void updateGameProfile() {

        calculateData();

        myRef.child("game_profile").child(userID).setValue(gameProfile);
        Log.d(TAG, "updateGameProfile: " + gameProfile.toString());
    }

    public void loadGameProfile() {
        myRef.child("game_profile").child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        gameProfile = dataSnapshot.getValue(GameProfile.class);
                        Log.d(TAG, "loadGameProfile: " + (gameProfile != null ? gameProfile.toString() : "NULL"));

                        if (gameProfile == null) {
                            gameProfile = new GameProfile();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                    }
                });
    }

    public DatabaseReference getGameProfile() {
        return myRef.child("game_profile").child(userID);
    }

    public int getStepCounterValue() {
        int stepCounterValue = stepCounter.getStepCounterValue();

        return stepCounterValue;
    }

    public String getTimestamp() {
        Date dNow = new Date();

        SimpleDateFormat ft = new SimpleDateFormat("dd.MM 'at' HH:mm:ss");
        ft.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));

        String timestamp = ft.format(dNow);

        return timestamp;
    }

    public void calculateData() {

        UserActivity newActivityValue = new UserActivity(getTimestamp(), TIME_SPENT, getStepCounterValue());

        Log.d(TAG, "calculateData: " + newActivityValue.toString());

        // save new activity log
        myRef.child("user_activity").child(userID).push().setValue(newActivityValue);


        gameProfile.setStrengthExp(gameProfile.getStrengthExp() + newActivityValue.getStepCounter());
        gameProfile.setStaminaExp(gameProfile.getStaminaExp() + (int)(newActivityValue.getStepCounter()/TIME_SPENT));

        gameProfile.setTimestamp(newActivityValue.getTimestamp());

    }
}
