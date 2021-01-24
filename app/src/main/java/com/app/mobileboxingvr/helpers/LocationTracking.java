package com.app.mobileboxingvr.helpers;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.constants.MyConstants;

import java.time.LocalTime;

public class LocationTracking extends Service implements LocationListener {

    private static final String TAG = "LocationTracking";

    private LocationManager locationManager;

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

    private void startLocationService() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);

        startForeground(222, createNotification().build());
    }

    private void stopLocationService() {
        locationManager.removeUpdates(this);
        stopForeground(true);
        stopSelf();
    }

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

    private void saveEveryLocation(double lat, double lng) {
        SharedPreferences pref = getSharedPreferences(MyConstants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        // save every trigger value to fix when initialize sensor
        editor.putLong(MyConstants.LATITUDE_VALUE, Double.doubleToRawLongBits(lat));
        editor.putLong(MyConstants.LONGITUDE_VALUE, Double.doubleToRawLongBits(lng));
        editor.apply();
    }

    private void saveDistance(double currentLatitude, double currentLongitude) {
        SharedPreferences pref = getSharedPreferences(MyConstants.SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        // set current location
        Location currentLocation = new Location("");
        currentLocation.setLatitude(currentLatitude);
        currentLocation.setLongitude(currentLongitude);

        long previousLatitude = pref.getLong(MyConstants.LATITUDE_VALUE, MyConstants.EXCLUDE_VALUE);
        long previousLongitude = pref.getLong(MyConstants.LONGITUDE_VALUE, MyConstants.EXCLUDE_VALUE);
        Log.d(TAG, "saveDistance: previous lat " + Double.longBitsToDouble(previousLatitude));
        Log.d(TAG, "saveDistance: previous lng " + Double.longBitsToDouble(previousLongitude));

        if (previousLatitude == MyConstants.EXCLUDE_VALUE
                && previousLongitude == MyConstants.EXCLUDE_VALUE) {
            Log.d(TAG, "saveDistance: EX");
            return;
        }

        // set previous location
        Location previousLocation = new Location("");
        previousLocation.setLatitude(Double.longBitsToDouble(previousLatitude));
        previousLocation.setLongitude(Double.longBitsToDouble(previousLongitude));

        // calculate distance & save to SharedPreference
        double distance = previousLocation.distanceTo(currentLocation);
        long previousDistance = pref.getLong(MyConstants.DISTANCE_VALUE, 0);
        double totalDistance = Double.longBitsToDouble(previousDistance) + distance;
        Log.d(TAG, "saveDistance: distance => " + distance);
        Log.d(TAG, "saveDistance: previous => " + Double.longBitsToDouble(previousDistance));
        Log.d(TAG, "saveDistance: total => " + totalDistance);
        editor.putLong(MyConstants.DISTANCE_VALUE, Double.doubleToRawLongBits(totalDistance));
        editor.apply();
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

        // TODO : check if location is the same location then not execute OR set min meter
        // save every trigger value
        saveDistance(lat, lng);
        saveEveryLocation(lat, lng);

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
        if (provider.equals("gps")) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }
}
