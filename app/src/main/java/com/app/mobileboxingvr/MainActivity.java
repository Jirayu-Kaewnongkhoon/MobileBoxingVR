package com.app.mobileboxingvr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.mobileboxingvr.helpers.StepCounter;
import com.app.mobileboxingvr.models.GameProfile;
import com.app.mobileboxingvr.services.ActivityService;
import com.app.mobileboxingvr.ui.login.LoginActivity;
import com.app.mobileboxingvr.background.BackgroundService;
import com.app.mobileboxingvr.services.UserService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private UserService user;
    private ActivityService activity;

    private TextView username, playerStatus;
    private Button btnStart, btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeView();

        username.setText(user.getCurrentUser().getDisplayName());
    }

    @Override
    protected void onStart() {
        super.onStart();

//        if (user.getCurrentUser() == null) {
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//        }
//
//        if (StepCounter.getInstance(this).getStepSensor() == null) {
//
//            // app cannot do background task
//            btnStart.setClickable(false);
//            btnStop.setClickable(false);
//
//            Log.d("MAIN", "onStart: Not Clickable");
//
//        }

        displayPlayerStatus();
    }

    private void initializeView() {
        username = findViewById(R.id.tvUsername);
        playerStatus = findViewById(R.id.tvPlayerStatus);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);

        user = UserService.getInstance();
        activity = ActivityService.getInstance(getApplicationContext());
    }

    private void displayPlayerStatus() {
        activity.getGameProfile().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GameProfile gameProfile = snapshot.getValue(GameProfile.class);

                if (gameProfile != null) {
                    playerStatus.setText(gameProfile.toString());
                    Log.d("MAIN", "onDataChange: " + gameProfile.toString());
                } else {
                    playerStatus.setText("NEW PLAYER");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onLogoutClick(View view) {
        user.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void onStartJobClick(View view) {
        BackgroundService.getInstance(getApplicationContext()).startService();
    }

    public void onStopJobClick(View view) {
        BackgroundService.getInstance(getApplicationContext()).stopService();
    }

}