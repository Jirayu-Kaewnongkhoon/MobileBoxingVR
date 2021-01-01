package com.app.mobileboxingvr.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.mobileboxingvr.MainActivity;
import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.services.UserService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {

    private UserService user;

    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeView();
    }

    private void initializeView() {
        email = findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);

        user = UserService.getInstance();
    }

    public void login(View view) {
        user.login(email.getText().toString().trim(), password.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();

                        } else {

                            Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_LONG).show();

                        }

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (user.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }
}