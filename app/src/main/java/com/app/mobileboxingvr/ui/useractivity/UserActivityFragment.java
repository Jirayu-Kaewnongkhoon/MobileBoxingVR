package com.app.mobileboxingvr.ui.useractivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.constants.MyConstants;
import com.app.mobileboxingvr.helpers.SharedPreferenceManager;

import java.text.DecimalFormat;

public class UserActivityFragment extends Fragment {

    private TextView tvLocation, tvStepCount;
    private ProgressBar progressStep;

    private SharedPreferenceManager pref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_useractivity, container, false);

        initializeView(v);

        loadActivity();

        return v;
    }

    private void initializeView(View v) {
        tvLocation = v.findViewById(R.id.tvLocation);
        tvStepCount = v.findViewById(R.id.tvStepCount);

        progressStep = v.findViewById(R.id.progressStep);

        pref = new SharedPreferenceManager(getContext());
    }

    private void loadActivity() {
        int stepCount = pref.getStepCounterValue();
        double distance = pref.getTotalDistance();

        updateStepCount(stepCount);
        updateDistance(distance);
    }

    private void updateStepCount(int stepCount) {
        progressStep.setProgress((100 * stepCount) / 5000);
        tvStepCount.setText(stepCount + "/5000");
    }

    private void updateDistance(double distance) {
        tvLocation.setText("Distance: " + new DecimalFormat("#0.00").format(distance / 1000) + " km.");
    }

    private BroadcastReceiver mReceiverLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            double lat = intent.getDoubleExtra(MyConstants.LATITUDE_VALUE, 0);
            double lng = intent.getDoubleExtra(MyConstants.LONGITUDE_VALUE, 0);
            // TODO : display current distance
            updateDistance(pref.getTotalDistance());
        }
    };

    private BroadcastReceiver mReceiverStepCount = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            int stepCount = intent.getIntExtra(MyConstants.STEP_COUNTER_VALUE, 0);

            updateStepCount(stepCount);
        }
    };

    @Override
    public void onResume() {
        super.onResume();

        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mReceiverLocation, new IntentFilter(MyConstants.ACTION_GET_LOCATION_VALUE_FROM_SERVICE));

        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mReceiverStepCount, new IntentFilter(MyConstants.ACTION_GET_STEP_COUNTER_VALUE_FROM_SERVICE));
    }

    @Override
    public void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiverLocation);

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiverStepCount);
    }
}
