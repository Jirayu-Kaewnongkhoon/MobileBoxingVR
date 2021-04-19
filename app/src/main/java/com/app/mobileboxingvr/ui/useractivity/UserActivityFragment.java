package com.app.mobileboxingvr.ui.useractivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

    private TextView tvDistance, tvStepCount, tvSpeed, tvTimeSpent;
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
        tvDistance = v.findViewById(R.id.tvDistance);
        tvStepCount = v.findViewById(R.id.tvStepCount);
        tvSpeed = v.findViewById(R.id.tvSpeed);
        tvTimeSpent = v.findViewById(R.id.tvTimeSpent);

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
        tvDistance.setText(new DecimalFormat("#0.00").format(distance / 1000) + " KM.");
    }

    private void updateTimeSpent(long previousTimestamp) {
        long timeSpent = Math.round((System.currentTimeMillis() - previousTimestamp) / MyConstants.MILLI_SECOND);
        tvTimeSpent.setText(timeSpent + " MIN");
    }

    private void updateSpeed(long previousTimestamp, double distance) {
        double timeSpent = (double) (System.currentTimeMillis() - previousTimestamp) / MyConstants.MILLI_SECOND;
        tvSpeed.setText(new DecimalFormat("#0.00").format(distance / (timeSpent * MyConstants.SECOND)) + " M/S.");
    }

    private BroadcastReceiver mReceiverLocation = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateDistance(pref.getTotalDistance());
            updateTimeSpent(pref.getPreviousTimestampValue());
            updateSpeed(pref.getPreviousTimestampValue(), pref.getTotalDistance());
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
