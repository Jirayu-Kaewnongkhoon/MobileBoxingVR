package com.app.mobileboxingvr.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.constants.MyConstants;
import com.app.mobileboxingvr.helpers.SharedPreferenceManager;

import java.time.LocalTime;

public class StepCounter extends Service implements SensorEventListener {

    private static final String TAG = "StepCounter";

    private SensorManager sensorManager;
    private Sensor stepSensor;

    private SharedPreferenceManager pref;

    /**
     *  --onStartCommand--
     *  When service was called, it will check what action to do (Start or Stop)
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: StepCounter " + LocalTime.now());

        if (intent != null) {

            String action = intent.getAction();

            if (action != null) {

                if (action.equals(MyConstants.ACTION_START_STEP_COUNTER_SERVICE)) {

                    startStepCounterService();

                } else if (action.equals(MyConstants.ACTION_STOP_STEP_COUNTER_SERVICE)) {

                    stopStepCounterService();

                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        pref = new SharedPreferenceManager(getApplicationContext());
    }

    /**
     *  --startStepCounterService--
     *  Initial Step Counter service and then start foreground for running service
     */

    private void startStepCounterService() {
        initializeSensorService();

        startForeground(111, createNotification().build());
    }

    /**
     *  --stopStepCounterService--
     *  Remove listener and then stop foreground
     */

    private void stopStepCounterService() {
        sensorManager.unregisterListener(this, stepSensor);
        stopForeground(true);
        stopSelf();
    }

    /**
     *  --initializeSensorService--
     *  Initial Step Counter service and register listener
     */

    private void initializeSensorService() {
        sensorManager = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor != null){
            Log.d(TAG, "initialSensor: Step Counter Sensor Available");
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Log.d(TAG, "initialSensor: Step Counter Sensor Not Available");
        }
    }

    private void sendValueToActivity(int stepCounterValue) {
        Intent intent = new Intent();
        intent.setAction(MyConstants.ACTION_GET_VALUE_FROM_SERVICE);
        intent.putExtra(MyConstants.STEP_COUNTER_VALUE, stepCounterValue);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    /**
     *  --createNotification--
     *  Create Notification for running persistent service
     */

    private NotificationCompat.Builder createNotification() {
        String channelId = "step_counter_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("StepCounter Service")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentText("Running")
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {

                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "StepCounter Service",
                        NotificationManager.IMPORTANCE_HIGH
                );

                notificationChannel.setDescription("This channel is used by step counter service");

                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        return builder;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int stepCounterValue = (int) sensorEvent.values[0];

        sendValueToActivity(stepCounterValue);

        // use SharedPreference to save current value
        pref.saveEveryStepCounterValue(stepCounterValue);

        Log.d(TAG, "onSensorChanged: step => " + stepCounterValue);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
