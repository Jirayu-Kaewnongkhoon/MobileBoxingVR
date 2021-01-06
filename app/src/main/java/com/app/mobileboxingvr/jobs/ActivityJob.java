package com.app.mobileboxingvr.jobs;

import android.app.job.JobParameters;
import android.app.job.JobService;

public class ActivityJob extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return true;
    }
}
