package com.app.mobileboxingvr.ui.gameprofile;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.background.BackgroundTask;
import com.app.mobileboxingvr.constants.MyConstants;
import com.app.mobileboxingvr.models.GameProfile;
import com.app.mobileboxingvr.helpers.GameManager;
import com.app.mobileboxingvr.helpers.UserManager;
import com.app.mobileboxingvr.ui.login.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class GameProfileFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "GameProfileFragment";

    private final int REQUEST_CODE = 111;

    private UserManager user;
    private GameManager game;

    private TextView username, playerStatus;
    private Button btnLogout, btnStart, btnStop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_gameprofile, container, false);

        initializeView(v);
        setupOnClick();

        username.setText(user.getCurrentUser().getDisplayName());

        displayPlayerStatus();

        checkSharedPreference();

        return v;
    }

    private void checkSharedPreference() {
        Map<String, ?> allEntries = getActivity().getSharedPreferences(MyConstants.SHARED_PREFS, Context.MODE_PRIVATE).getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d(TAG, entry.getKey() + ": " + entry.getValue().toString());
        }
    }

    private void initializeView(View v) {
        username = v.findViewById(R.id.tvUsername);
        playerStatus = v.findViewById(R.id.tvPlayerStatus);
        btnLogout = v.findViewById(R.id.btnLogout);
        btnStart = v.findViewById(R.id.btnStart);
        btnStop = v.findViewById(R.id.btnStop);

        user = UserManager.getInstance();
        game = GameManager.getInstance();
    }

    private void setupOnClick() {
        btnLogout.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    private void displayPlayerStatus() {
        game.getGameProfile().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GameProfile gameProfile = snapshot.getValue(GameProfile.class);

                if (gameProfile != null) {
                    playerStatus.setText(gameProfile.toString());
                    Log.d(TAG, "onDataChange: " + gameProfile.toString());
                } else {
                    playerStatus.setText("NEW PLAYER");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }

    public void onLogoutClick() {
        user.logout();
        BackgroundTask.getInstance(getActivity()).stopBackgroundTask();
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    public void onStartJobClick() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE);

        } else {

            BackgroundTask.getInstance(getActivity()).startBackgroundTask();
            Log.d(TAG, "onStartJobClick: ");

        }
    }

    public void onStopJobClick() {
        BackgroundTask.getInstance(getActivity()).stopBackgroundTask();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                BackgroundTask.getInstance(getActivity()).startBackgroundTask();
                Toast.makeText(getContext(), "Permission granted!", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(getContext(), "Permission denied!", Toast.LENGTH_LONG).show();

            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogout:
                onLogoutClick();
                break;
            case R.id.btnStart:
                onStartJobClick();
                break;
            case R.id.btnStop:
                onStopJobClick();
                break;
        }
    }
}
