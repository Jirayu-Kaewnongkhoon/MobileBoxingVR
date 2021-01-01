package com.app.mobileboxingvr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.mobileboxingvr.login.LoginActivity;
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

    private void initializeView() {
        text = findViewById(R.id.text);

        user = UserService.getInstance();
    }

    public void logout(View view) {
        user.logout();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (user.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }
}