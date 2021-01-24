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
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.constants.MyConstants;

import java.time.LocalTime;

public class LocationTracking extends Service implements LocationListener {

    private static final String TAG = "Location";

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

        startForeground(111, createNotification().build());
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

        // TODO : if device reboot, need to return 0 not previousVal (may be use previousVal)

        // save every trigger value to fix when initialize sensor
        editor.putLong(MyConstants.LATITUDE_VALUE, Double.doubleToRawLongBits(lat));
        editor.putLong(MyConstants.LONGITUDE_VALUE, Double.doubleToRawLongBits(lng));
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

        // TODO : return location to calculate distance

        Log.d(TAG, "onLocationResult: " + lat + ", " + lng);
    }
}
