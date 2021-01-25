package com.app.mobileboxingvr.background;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.app.mobileboxingvr.constants.MyConstants;
import com.app.mobileboxingvr.helpers.LocationTracking;
import com.app.mobileboxingvr.helpers.StepCounter;
import com.app.mobileboxingvr.works.ActivityWork;

import java.util.concurrent.TimeUnit;

public class BackgroundTask {

    private static BackgroundTask instance;

    private Context context;

    private BackgroundTask(Context context) {
        this.context = context;
    }

    public static BackgroundTask getInstance(Context context) {
        if (instance == null) {
            instance = new BackgroundTask(context);
        }
        return instance;
    }

    /**
     *  --startBackgroundTask--
     *  Start service for tracking user activity
     *  and then start background task for convert user data to game data
     */

    public void startBackgroundTask() {
        startLocationService();
        startStepCounterService();

        WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                        MyConstants.WORK_NAME,
                        ExistingPeriodicWorkPolicy.KEEP,
                        getPeriodicWorkRequest()
                );

        Toast.makeText(context, "Job started..", Toast.LENGTH_LONG).show();
    }

    /**
     *  --stopBackgroundTask--
     *  Stop service and stop background task
     */

    public void stopBackgroundTask() {
        stopLocationService();
        stopStepCounterService();

        WorkManager.getInstance(context).cancelAllWork();

        Toast.makeText(context, "Job stopped..", Toast.LENGTH_LONG).show();
    }

    /**
     *  --startLocationService--
     *  Create intent for start Location service
     */

    private void startLocationService() {
        Intent intent = new Intent(context, LocationTracking.class);
        intent.setAction(MyConstants.ACTION_START_LOCATION_SERVICE);
        context.startService(intent);
        Toast.makeText(context, "Location service started", Toast.LENGTH_SHORT).show();
    }

    /**
     *  --stopLocationService--
     *  Create intent for stop Location service
     */

    private void stopLocationService() {
        Intent intent = new Intent(context, LocationTracking.class);
        intent.setAction(MyConstants.ACTION_STOP_LOCATION_SERVICE);
        context.startService(intent);
        Toast.makeText(context, "Location service stopped", Toast.LENGTH_SHORT).show();
    }

    /**
     *  --startStepCounterService--
     *  Create intent for start Step Counter service
     */

    private void startStepCounterService() {
        Intent intent = new Intent(context, StepCounter.class);
        intent.setAction(MyConstants.ACTION_START_STEP_COUNTER_SERVICE);
        context.startService(intent);
        Toast.makeText(context, "StepCounter service started", Toast.LENGTH_SHORT).show();
    }

    /**
     *  --stopStepCounterService--
     *  Create intent for stop Step Counter service
     */

    private void stopStepCounterService() {
        Intent intent = new Intent(context, StepCounter.class);
        intent.setAction(MyConstants.ACTION_STOP_STEP_COUNTER_SERVICE);
        context.startService(intent);
        Toast.makeText(context, "StepCounter service stopped", Toast.LENGTH_SHORT).show();
    }

    /**
     *  --getPeriodicWorkRequest--
     *  Create PeriodicWorkRequest property for WorkManger
     */

    private PeriodicWorkRequest getPeriodicWorkRequest() {
        return new PeriodicWorkRequest.Builder(ActivityWork.class, 15, TimeUnit.MINUTES)
                .addTag(MyConstants.WORK_NAME)
//                .setConstraints(getConstraints())
                .build();
    }

    /**
     *  --getConstraints--
     *  Create Constraints property for PeriodicWorkRequest
     */

    private Constraints getConstraints() {
        // isNecessary?
        return new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
    }
}
