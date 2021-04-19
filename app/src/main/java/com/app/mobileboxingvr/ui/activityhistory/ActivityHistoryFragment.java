package com.app.mobileboxingvr.ui.activityhistory;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.models.UserActivity;
import com.app.mobileboxingvr.helpers.ActivityManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ActivityHistoryFragment extends Fragment {

    private static final String TAG = "ActivityHistoryFragment";

    private ActivityManager activity;

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_activityhistory, container, false);

        initializeView(v);
        recyclerViewSetup();
        loadActivityHistory();

        return v;
    }

    /**
     *  --loadActivityHistory--
     *  Get all user activity from database
     *  and set to recycler view
     */

    private void loadActivityHistory() {
        activity.getUserActivity()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<UserActivity> list = new ArrayList<>();

                        for(DataSnapshot activity : snapshot.getChildren()) {
                            UserActivity userActivity = activity.getValue(UserActivity.class);
                            list.add(userActivity);
                        }

                        recyclerView.setAdapter(new HistoryAdapter(list, getContext()));
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getMessage());
                    }
                });
    }

    /**
     *  --initializeView--
     *  Setup view and instantiate object
     */

    private void initializeView(View v) {
        recyclerView = v.findViewById(R.id.rvHistory);

        activity = ActivityManager.getInstance(getActivity());
    }

    /**
     *  --recyclerViewSetup--
     *  Setup recycler view property
     */

    private void recyclerViewSetup() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        // new activity will appear on top
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }
}
