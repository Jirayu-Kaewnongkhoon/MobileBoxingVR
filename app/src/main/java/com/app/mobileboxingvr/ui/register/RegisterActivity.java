package com.app.mobileboxingvr.ui.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.mobileboxingvr.MainActivity;
import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.helpers.ActivityManager;
import com.app.mobileboxingvr.helpers.GameManager;
import com.app.mobileboxingvr.helpers.UserManager;
import com.app.mobileboxingvr.models.GameProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private UserManager user;

    private EditText etPlayerName, etEmail, etPassword;

    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeView();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseDatabase.getInstance().getReference("game_profile")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        for (DataSnapshot gameProfile : snapshot.getChildren()) {
                            GameProfile profile = gameProfile.getValue(GameProfile.class);
                            list.add(profile.getPlayerName());
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.d(TAG, "onCancelled: " + error.getMessage());
                    }
                });
    }

    private void initializeView() {
        etPlayerName = findViewById(R.id.etPlayerName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        user = UserManager.getInstance();

        list = new ArrayList<>();
    }

    public void onLoginClick(View view) {
        finish();
    }

    public void onRegisterClick(View view) {
        String playerName = etPlayerName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        boolean isValid = registerValidator(playerName, email, password);

        if (isValid) {
            user.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        updateGameProfile(playerName);

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                    } else {

                        Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_LONG).show();

                    }

                }
            });
        }
    }

    public void updateGameProfile(String playerName) {
        GameProfile gameProfile = new GameProfile();
        gameProfile.setPlayerName(playerName);
        gameProfile.setTimestamp(ActivityManager.getInstance(getApplicationContext()).getTimestamp());

        GameManager game = new GameManager();
        game.updateGameProfile(gameProfile);
    }

    private boolean registerValidator(String playerName, String email, String password) {

        if (playerName.isEmpty()) {
            etPlayerName.setError("Field can not be empty");
            return false;
        }

        if (isExist(playerName)) {
            etPlayerName.setError("This name is already taken");
            return false;
        }

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

        if (password.length() < 6) {
            etPassword.setError("Password should contain 6 characters!");
            return false;
        }

        return true;
    }

    private boolean isExist(String playerName) {
        return list.contains(playerName);
    }
}