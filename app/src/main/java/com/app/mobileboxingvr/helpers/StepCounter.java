package com.app.mobileboxingvr.helpers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class StepCounter implements SensorEventListener {

    private static final String TAG = "StepCounter";

    private static StepCounter instance;

    private Context context;

    private SensorManager sensorManager;
    private Sensor stepSensor;

    private int stepCounterValue;

    private StepCounter(Context context) {
        this.context = context;

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (stepSensor != null){
            Log.d(TAG, "initialSensor: Step Counter Sensor Available");
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Log.d(TAG, "initialSensor: Step Counter Sensor Not Available");
        }
    }

    public static StepCounter getInstance(Context context) {
        if (instance == null) {
            instance = new StepCounter(context);
        }
        return instance;
    }

    public Sensor getStepSensor() {
        return stepSensor;
    }

    public int getStepCounterValue() {
        return stepCounterValue;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        stepCounterValue = (int) sensorEvent.values[0];
        Log.d(TAG, "onSensorChanged: " + stepCounterValue);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
