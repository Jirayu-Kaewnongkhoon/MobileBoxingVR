package com.app.mobileboxingvr.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.mobileboxingvr.MainActivity;
import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.helpers.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class RegisterActivity extends AppCompatActivity {

    private UserService user;

    private EditText username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeView();
    }

    private void initializeView() {
        username = findViewById(R.id.etUsername);
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);

        user = UserService.getInstance();
    }

    public void onRegisterClick(View view) {
        user.register(email.getText().toString().trim(), password.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            updateProfile();

                        } else {

                            Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_LONG).show();

                        }

                    }
                });
    }

    public void updateProfile() {
        user.updateProfile(username.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Log.d("REGISTER", "updateProfile: " + user.getCurrentUser().getDisplayName());
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        }

                    }
                });
    }
}