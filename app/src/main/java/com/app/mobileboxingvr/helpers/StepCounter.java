package com.app.mobileboxingvr.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalTime;

public class StepCounter implements SensorEventListener {

    private static final String TAG = "StepCounter";

    private final String SHARED_PREFS = "UserActivity";
    private final String STEP_COUNTER_VALUE = "StepCounterValue";

    private static StepCounter instance;

    private Context context;

    private SensorManager sensorManager;
    private Sensor stepSensor;

    private StepCounter(Context context) {
        this.context = context;

        initializeSensor();
        test();
    }

    private void test () {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("step_counter").push().setValue(LocalTime.now());
    }

    public static StepCounter getInstance(Context context) {
        if (instance == null) {
            instance = new StepCounter(context);
        }
        return instance;
    }

    private void initializeSensor() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor != null){
            Log.d(TAG, "initialSensor: Step Counter Sensor Available");
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Log.d(TAG, "initialSensor: Step Counter Sensor Not Available");
        }
    }

    private void saveEveryStepCounterValue(int stepCounterValue) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        // TODO : if device reboot, need to return 0 not previousVal (may be use previousVal)

        // save every trigger value to fix when initialize sensor
        editor.putInt(STEP_COUNTER_VALUE, stepCounterValue);
        editor.apply();
    }

    public Sensor getStepSensor() {
        return stepSensor;
    }

    public int getStepCounterValue() {
        // use SharedPreference to get current trigger value
        // TODO : Using SharedPreference may not be as good as you think.
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        return pref.getInt(STEP_COUNTER_VALUE, 0);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int stepCounterValue = (int) sensorEvent.values[0];

        // use SharedPreference to save current value
        saveEveryStepCounterValue(stepCounterValue);

        Log.d(TAG, "onSensorChanged: step => " + stepCounterValue);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
