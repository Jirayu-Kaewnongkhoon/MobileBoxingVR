package com.app.mobileboxingvr.ui.useractivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.constants.MyConstants;

public class UserActivityFragment extends Fragment {

    private TextView tvLocation, tvStepCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_useractivity, container, false);

        initializeView(v);

        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mBroadcastReceiver, new IntentFilter(MyConstants.ACTION_GET_VALUE_FROM_SERVICE));

        return v;
    }

    private void initializeView(View v) {
        tvLocation = v.findViewById(R.id.tvLocation);
        tvStepCount = v.findViewById(R.id.tvStepCount);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            int stepCount = intent.getIntExtra(MyConstants.STEP_COUNTER_VALUE, 0);
            double lat = intent.getDoubleExtra(MyConstants.LATITUDE_VALUE, 0);
            double lng = intent.getDoubleExtra(MyConstants.LONGITUDE_VALUE, 0);

            tvStepCount.setText("Step: " + stepCount);
            tvLocation.setText("Lat: " + lat + ", Lng: " + lng);
        }
    };
}
