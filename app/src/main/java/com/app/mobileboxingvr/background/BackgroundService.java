package com.app.mobileboxingvr.background;

import android.content.Context;
import android.widget.Toast;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.app.mobileboxingvr.works.ActivityWork;

import java.util.concurrent.TimeUnit;

public class BackgroundService {

    private static BackgroundService instance;

    private PeriodicWorkRequest request;

    private final Context context;

    private BackgroundService(Context context) {
        this.context = context;

        request = new PeriodicWorkRequest.Builder(ActivityWork.class, 15, TimeUnit.MINUTES).build();
    }

    public static BackgroundService getInstance(Context context) {
        if (instance == null) {
            instance = new BackgroundService(context);
        }
        return instance;
    }

    public void startService() {
        WorkManager.getInstance(context).enqueue(request);

        Toast.makeText(context, "Job started..", Toast.LENGTH_LONG).show();
    }

    public void stopService() {
        WorkManager.getInstance(context).cancelWorkById(request.getId());

        Toast.makeText(context, "Job stopped..", Toast.LENGTH_LONG).show();
    }
}
