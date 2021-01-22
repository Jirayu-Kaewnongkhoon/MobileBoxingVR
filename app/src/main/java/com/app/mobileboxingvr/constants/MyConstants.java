package com.app.mobileboxingvr.constants;

import java.util.concurrent.TimeUnit;

public class MyConstants {

    public static final String WORK_NAME = "GAME_PROFILE";

    public static final String SHARED_PREFS = "UserActivity";

    public static final String STEP_COUNTER_VALUE = "StepCounterValue";
    public static final String LATITUDE_VALUE = "LatitudeValue";
    public static final String LONGITUDE_VALUE = "LongitudeValue";

    public static final String CURRENT_STEP_COUNTER_VALUE = "CurrentStepCounterValue";
    public static final String CURRENT_TIMESTAMP_VALUE = "CurrentTimestampValue";

    public static final long MILLI_SECOND = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);

    public static final int DEFAULT_VALUE = -1;

    public static final String ACTION_START_LOCATION_SERVICE = "StartLocationService";
    public static final String ACTION_STOP_LOCATION_SERVICE = "StopLocationService";

}
