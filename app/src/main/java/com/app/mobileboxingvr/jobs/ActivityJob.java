package com.app.mobileboxingvr.jobs;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.app.mobileboxingvr.services.ActivityService;

import java.time.LocalTime;

public class ActivityJob extends JobService {
    private static final String TAG = "ActivityJob";

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob: HELLO!");

        backgroundTask(jobParameters);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob: HELLO!");
        return true;
    }

    private void backgroundTask(JobParameters jobParameters) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                Log.d(TAG, "run: BACKGROUND TASK " + LocalTime.now());
                ActivityService service = ActivityService.getInstance(getApplicationContext());

                service.loadGameProfile();

                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                service.updateGameProfile();

                jobFinished(jobParameters, false);

            }
        }).start();
    }
}
