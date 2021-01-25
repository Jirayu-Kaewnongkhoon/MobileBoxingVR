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

    public static final String CURRENT_STEP_COUNTER_VALUE = "CurrentStepCounterValue";
    public static final String CURRENT_TIMESTAMP_VALUE = "CurrentTimestampValue";

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

    public static final int WORK_ACCESS = 149;
    public static final int SELF_ACCESS = 230;

    /**
     *  Constants Value for Intent
     */

    public static final String ACTION_START_LOCATION_SERVICE = "StartLocationService";
    public static final String ACTION_STOP_LOCATION_SERVICE = "StopLocationService";

    public static final String ACTION_START_STEP_COUNTER_SERVICE = "StartStepCounterService";
    public static final String ACTION_STOP_STEP_COUNTER_SERVICE = "StopStepCounterService";

}
