package com.app.mobileboxingvr.works;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.services.ActivityService;

import java.time.LocalTime;

public class ActivityWork extends Worker {

    public ActivityWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        ActivityService service = ActivityService.getInstance(getApplicationContext());

        service.loadGameProfile();

        service.updateGameProfile();

        displayNotification();

        return Result.success();
    }

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
