package com.app.mobileboxingvr.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.mobileboxingvr.ui.MainActivity;
import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.ui.register.RegisterActivity;
import com.app.mobileboxingvr.helpers.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class LoginActivity extends AppCompatActivity {

    private UserManager user;

    private EditText etEmail, etPassword;
    private ProgressBar loginLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeView();
    }

    /**
     *  --initializeView--
     *  Setup view and instantiate object
     */

    private void initializeView() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        loginLoading = findViewById(R.id.loginLoading);

        user = UserManager.getInstance();
    }

    /**
     *  --onLoginClick--
     *  Validate user input
     *  and login user with email and password
     *  then go to MainActivity
     */

    public void onLoginClick(View view) {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        boolean isValid = loginValidator(email, password);

        if (isValid) {
            loginLoading.setVisibility(View.VISIBLE);

            user.login(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                    } else {

                        Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_LONG).show();

                    }

                    loginLoading.setVisibility(View.INVISIBLE);

                }
            });
        }
    }

    /**
     *  --onRegisterClick--
     *  Go to RegisterActivity
     */

    public void onRegisterClick(View view) {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }

    /**
     *  --loginValidator--
     *  Validate user input for login form
     */

    private boolean loginValidator(String email, String password) {

        if (email.isEmpty()) {
            etEmail.setError("Field can not be empty");
            return false;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid Email!");
            return false;
        }

        if (password.isEmpty()) {
            etPassword.setError("Field can not be empty");
            return false;
        }

        return true;
    }
}