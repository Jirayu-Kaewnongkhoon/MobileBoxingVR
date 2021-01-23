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
import com.app.mobileboxingvr.helpers.Location;
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

    public void stopBackgroundTask() {
        stopLocationService();
        stopStepCounterService();

        WorkManager.getInstance(context).cancelAllWork();

        Toast.makeText(context, "Job stopped..", Toast.LENGTH_LONG).show();
    }

    private void startLocationService() {
        Intent intent = new Intent(context, Location.class);
        intent.setAction(MyConstants.ACTION_START_LOCATION_SERVICE);
        context.startService(intent);
        Toast.makeText(context, "Location service started", Toast.LENGTH_SHORT).show();
    }

    private void stopLocationService() {
        Intent intent = new Intent(context, Location.class);
        intent.setAction(MyConstants.ACTION_STOP_LOCATION_SERVICE);
        context.startService(intent);
        Toast.makeText(context, "Location service stopped", Toast.LENGTH_SHORT).show();
    }

    private void startStepCounterService() {
        Intent intent = new Intent(context, StepCounter.class);
        intent.setAction(MyConstants.ACTION_START_STEP_COUNTER_SERVICE);
        context.startService(intent);
        Toast.makeText(context, "StepCounter service started", Toast.LENGTH_SHORT).show();
    }

    private void stopStepCounterService() {
        Intent intent = new Intent(context, StepCounter.class);
        intent.setAction(MyConstants.ACTION_STOP_STEP_COUNTER_SERVICE);
        context.startService(intent);
        Toast.makeText(context, "StepCounter service stopped", Toast.LENGTH_SHORT).show();
    }

    private PeriodicWorkRequest getPeriodicWorkRequest() {
        // TODO : set delay maybe not necessary
        // to fix new initial step counter when app close => setInitialDelay for running first work after close app
        return new PeriodicWorkRequest.Builder(ActivityWork.class, 15, TimeUnit.MINUTES)
//                .setInitialDelay(5, TimeUnit.MINUTES)
                .addTag(MyConstants.WORK_NAME)
//                .setConstraints(getConstraints())
                .build();
    }

    private Constraints getConstraints() {
        // isNecessary?
        return new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();
    }
}
