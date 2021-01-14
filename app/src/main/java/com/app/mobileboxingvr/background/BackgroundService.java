package com.app.mobileboxingvr.background;

import android.content.Context;
import android.widget.Toast;

import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.app.mobileboxingvr.works.ActivityWork;

import java.util.concurrent.TimeUnit;

public class BackgroundService {

    private static BackgroundService instance;

    private Context context;

    private final String WORK_NAME = "GAME_PROFILE";

    private BackgroundService(Context context) {
        this.context = context;
    }

    public static BackgroundService getInstance(Context context) {
        if (instance == null) {
            instance = new BackgroundService(context);
        }
        return instance;
    }

    public void startService() {
        WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(
                        WORK_NAME,
                        ExistingPeriodicWorkPolicy.KEEP,
                        getPeriodicWorkRequest()
                );

        Toast.makeText(context, "Job started..", Toast.LENGTH_LONG).show();
    }

    public void stopService() {
        WorkManager.getInstance(context).cancelAllWork();

        Toast.makeText(context, "Job stopped..", Toast.LENGTH_LONG).show();
    }

    private PeriodicWorkRequest getPeriodicWorkRequest() {
        return new PeriodicWorkRequest.Builder(ActivityWork.class, 15, TimeUnit.MINUTES)
                .addTag(WORK_NAME)
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
