package com.app.mobileboxingvr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.mobileboxingvr.ui.login.LoginActivity;
import com.app.mobileboxingvr.services.BackgroundService;
import com.app.mobileboxingvr.services.UserService;

public class MainActivity extends AppCompatActivity {

    private UserService user;

    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeView();

        text.setText(user.getCurrentUser().getDisplayName());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (user.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void initializeView() {
        text = findViewById(R.id.tvUsername);

        user = UserService.getInstance();
    }

    public void onLogoutClick(View view) {
        user.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    public void onStartJobClick(View view) {
        BackgroundService.getInstance(this).startService();
    }

    public void onStopJobClick(View view) {
        BackgroundService.getInstance(this).stopService();
    }

}