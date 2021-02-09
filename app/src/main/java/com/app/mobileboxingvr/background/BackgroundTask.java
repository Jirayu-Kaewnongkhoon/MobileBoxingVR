package com.app.mobileboxingvr.background;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.app.mobileboxingvr.constants.MyConstants;
import com.app.mobileboxingvr.helpers.SharedPreferenceManager;
import com.app.mobileboxingvr.services.LocationTracking;
import com.app.mobileboxingvr.services.StepCounter;
import com.app.mobileboxingvr.works.ActivityWork;

import java.util.concurrent.TimeUnit;

public class BackgroundTask {

    private static final String TAG = "BackgroundTask";

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

        Log.d(TAG, "startBackgroundTask: ");
    }

    /**
     *  --stopBackgroundTask--
     *  Stop service and stop background task
     */

    public void stopBackgroundTask() {
        stopLocationService();
        stopStepCounterService();
        resetActivityValue();

        WorkManager.getInstance(context).cancelAllWork();

        Log.d(TAG, "stopBackgroundTask: ");
    }

    /**
     *  --startLocationService--
     *  Create intent for start Location service
     */

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(context, LocationTracking.class);
            intent.setAction(MyConstants.ACTION_START_LOCATION_SERVICE);
            context.startService(intent);
            Log.d(TAG, "startLocationService: ");
        }
    }

    /**
     *  --stopLocationService--
     *  Create intent for stop Location service
     */

    private void stopLocationService() {
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(context, LocationTracking.class);
            intent.setAction(MyConstants.ACTION_STOP_LOCATION_SERVICE);
            context.startService(intent);
            Log.d(TAG, "stopLocationService: ");
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationTracking.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
     *  --startStepCounterService--
     *  Create intent for start Step Counter service
     */

    private void startStepCounterService() {
        if (!isStepCounterServiceRunning()) {
            Intent intent = new Intent(context, StepCounter.class);
            intent.setAction(MyConstants.ACTION_START_STEP_COUNTER_SERVICE);
            context.startService(intent);
            Log.d(TAG, "startStepCounterService: ");
        }
    }

    /**
     *  --stopStepCounterService--
     *  Create intent for stop Step Counter service
     */

    private void stopStepCounterService() {
        if (isStepCounterServiceRunning()) {
            Intent intent = new Intent(context, StepCounter.class);
            intent.setAction(MyConstants.ACTION_STOP_STEP_COUNTER_SERVICE);
            context.startService(intent);
            Log.d(TAG, "stopStepCounterService: ");
        }
    }

    private boolean isStepCounterServiceRunning() {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (StepCounter.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void resetActivityValue() {
        SharedPreferenceManager pref = new SharedPreferenceManager(context);
        pref.resetTotalDistance();
        pref.resetStepCounterValue();
        pref.resetTimestampValue();
        pref.resetLocation();
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
