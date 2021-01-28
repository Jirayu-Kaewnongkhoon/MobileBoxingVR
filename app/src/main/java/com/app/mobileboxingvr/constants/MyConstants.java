package com.app.mobileboxingvr.constants;

import java.util.concurrent.TimeUnit;

public class MyConstants {

    /**
     *  Constants Value for WorkManger
     */

    public static final String WORK_NAME = "GAME_PROFILE";

    /**
     *  Constants Value for SharedPreference
     */

    public static final String SHARED_PREFS = "UserActivity";

    public static final String STEP_COUNTER_VALUE = "StepCounterValue";
    public static final String DISTANCE_VALUE = "DistanceValue";
    public static final String LATITUDE_VALUE = "LatitudeValue";
    public static final String LONGITUDE_VALUE = "LongitudeValue";

    public static final String PREVIOUS_STEP_COUNTER_VALUE = "PreviousStepCounterValue";
    public static final String PREVIOUS_TIMESTAMP_VALUE = "PreviousTimestampValue";

    /**
     *  Constants Value for Time
     */

    public static final long MILLI_SECOND = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
    public static final long SECOND = TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES);

    /**
     *  Constants Value for Condition check
     */

    public static final int DEFAULT_VALUE = -1;
    public static final long EXCLUDE_VALUE = -999;

    /**
     *  Constants Value for Intent
     */

    public static final String ACTION_START_LOCATION_SERVICE = "StartLocationService";
    public static final String ACTION_STOP_LOCATION_SERVICE = "StopLocationService";

    public static final String ACTION_START_STEP_COUNTER_SERVICE = "StartStepCounterService";
    public static final String ACTION_STOP_STEP_COUNTER_SERVICE = "StopStepCounterService";

    /**
     *  Constants Value for Calculate
     */

    public static final int MAX_EXP = 100;
    public static final double EXP_PER_STEP = 0.5;
    public static final int SPEED_EXP = 10;

}
