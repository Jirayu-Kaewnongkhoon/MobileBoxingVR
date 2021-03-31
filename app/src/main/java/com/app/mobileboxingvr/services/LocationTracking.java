package com.app.mobileboxingvr.services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.constants.MyConstants;
import com.app.mobileboxingvr.helpers.SharedPreferenceManager;

import java.time.LocalTime;

public class LocationTracking extends Service implements LocationListener {

    private static final String TAG = "LocationTracking";

    private LocationManager locationManager;

    private SharedPreferenceManager pref;

    /**
     *  --onStartCommand--
     *  When service was called, it will check what action to do (Start or Stop)
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: Location " + LocalTime.now());

        if (intent != null) {

            String action = intent.getAction();

            if (action != null) {

                if (action.equals(MyConstants.ACTION_START_LOCATION_SERVICE)) {

                    startLocationService();

                } else if (action.equals(MyConstants.ACTION_STOP_LOCATION_SERVICE)) {

                    stopLocationService();

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
     *  --startLocationService--
     *  Initial Location service and then start foreground for running service
     */

    private void startLocationService() {
        initializeLocationService();

        startForeground(222, createNotification().build());
    }

    /**
     *  --stopLocationService--
     *  Remove listener and then stop foreground
     */

    private void stopLocationService() {
        locationManager.removeUpdates(this);
        stopForeground(true);
        stopSelf();
    }

    /**
     *  --initializeLocationService--
     *  Initial Location service and register listener
     */

    private void initializeLocationService() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
    }

    private void sendValueToActivity(double lat, double lng) {
        Intent intent = new Intent();
        intent.setAction(MyConstants.ACTION_GET_LOCATION_VALUE_FROM_SERVICE);
        intent.putExtra(MyConstants.LATITUDE_VALUE, lat);
        intent.putExtra(MyConstants.LONGITUDE_VALUE, lng);

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    /**
     *  --createNotification--
     *  Create Notification for running persistent service
     */

    private NotificationCompat.Builder createNotification() {
        String channelId = "location_notification_channel";
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
                .setContentTitle("Location Service")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentText("Running")
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {

                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );

                notificationChannel.setDescription("This channel is used by location service");

                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        return builder;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        sendValueToActivity(lat, lng);

        // TODO : check if location is the same location then not execute OR set min meter
        // save every trigger value
        pref.saveDistance(lat, lng);
        pref.saveEveryLocation(lat, lng);

        Log.d(TAG, "onLocationResult: " + lat + ", " + lng);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d(TAG, "onStatusChanged: " + status);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Log.d(TAG, "onProviderEnabled: " + provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Log.d(TAG, "onProviderDisabled: " + provider);

        // ask for enable GPS
        // TODO : ask before task start (dialog?)
        if (provider.equals("gps")) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }
}
