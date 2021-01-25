package com.app.mobileboxingvr.works;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.constants.MyConstants;
import com.app.mobileboxingvr.models.GameProfile;
import com.app.mobileboxingvr.models.UserActivity;
import com.app.mobileboxingvr.helpers.ActivityService;
import com.app.mobileboxingvr.helpers.GameService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalTime;

public class ActivityWork extends Worker {

    private static final String TAG = "ActivityWork";

    private GameService game;

    private GameProfile gameProfile;
    private UserActivity newActivityValue;

    public ActivityWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        game = GameService.getInstance();

        loadUserActivity();

        displayNotification();

        return Result.success();
    }

    /**
     *  --loadGameProfile--
     *  Get current game profile from database
     */

    private void loadGameProfile() {
        game.getGameProfile().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gameProfile = snapshot.getValue(GameProfile.class);
                Log.d(TAG, "loadGameProfile: " + (gameProfile != null ? gameProfile.toString() : "NULL"));

                if (gameProfile == null) {
                    gameProfile = new GameProfile();
                }

                calculateUserActivityToGameProfile();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }

    /**
     *  --loadUserActivity--
     *  Get user activity that come from tracking service
     *  and save to database
     */

    private void loadUserActivity() {
        ActivityService activity = ActivityService.getInstance(getApplicationContext());

        String timestamp = activity.getTimestamp();
        int timeSpent = activity.getTimeSpent(MyConstants.WORK_ACCESS);
        int stepCounter = activity.getStepCounterValue();

        // need to call getSpeed before getDistance => Distance will reset after call
        double speed = activity.getSpeed();
        double distance = activity.getDistance(MyConstants.WORK_ACCESS);

        // first time will not do anything
        if (stepCounter == MyConstants.DEFAULT_VALUE && timeSpent == MyConstants.DEFAULT_VALUE) {
            return;
        }

        newActivityValue = new UserActivity(timestamp, timeSpent, stepCounter, distance, speed);

        Log.d(TAG, "loadUserActivity: " + newActivityValue.toString());

        activity.saveUserActivity(newActivityValue);
        loadGameProfile();
    }

    /**
     *  --calculateUserActivityToGameProfile--
     *  Convert user activity that come from tracking service to game data
     */

    private void calculateUserActivityToGameProfile() {
        // calculate Strength
        int newStrengthExp = (int) Math.round(newActivityValue.getStepCounter() * 0.5);
        gameProfile.setStrengthExp(gameProfile.getStrengthExp() + newStrengthExp);

        // calculate Stamina
        int newStaminaExp = newActivityValue.getStepCounter() / newActivityValue.getTimeSpent();
        gameProfile.setStaminaExp(gameProfile.getStaminaExp() + newStaminaExp);

        // calculate Agility
        int newAgilityExp = (int) Math.round(newActivityValue.getSpeed() * 10);
        gameProfile.setAgilityExp(gameProfile.getAgilityExp() + newAgilityExp);

        // set new Timestamp
        gameProfile.setTimestamp(newActivityValue.getTimestamp());

        Log.d(TAG, "calculateUserActivityToGameProfile: " + gameProfile.toString());
        game.updateGameProfile(gameProfile);
    }

    /**
     *  --displayNotification--
     *  Create Notification for checking time when task is done
     */

    private void displayNotification() {
        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("test", "Test", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "test")
                .setContentTitle("BACKGROUND TASK COMPLETED")
                .setContentText(LocalTime.now().toString())
                .setSmallIcon(R.mipmap.ic_launcher);

        manager.notify(1, builder.build());
    }
}
