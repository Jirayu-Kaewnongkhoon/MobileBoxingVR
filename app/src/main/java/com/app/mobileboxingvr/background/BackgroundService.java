package com.app.mobileboxingvr.background;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.widget.Toast;

import com.app.mobileboxingvr.jobs.ActivityJob;

public class BackgroundService {

    private static BackgroundService instance;

    private JobScheduler scheduler;

    private final int JOB_UNIQUE_ID = 123;

    private final Context context;

    private BackgroundService(Context context) {
        this.context = context;

        scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    public static BackgroundService getInstance(Context context) {
        if (instance == null) {
            instance = new BackgroundService(context);
        }
        return instance;
    }

    public void startService() {
        ComponentName componentName = new ComponentName(context, ActivityJob.class);
        JobInfo info = new JobInfo.Builder(JOB_UNIQUE_ID, componentName)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        scheduler.schedule(info);

        Toast.makeText(context, "Job started..", Toast.LENGTH_LONG).show();
    }

    public void stopService() {
        scheduler.cancel(JOB_UNIQUE_ID);
        Toast.makeText(context, "Job stopped..", Toast.LENGTH_LONG).show();
    }
}
