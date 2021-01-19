package com.app.mobileboxingvr.ui.gameprofile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.mobileboxingvr.MainActivity;
import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.background.BackgroundService;
import com.app.mobileboxingvr.models.GameProfile;
import com.app.mobileboxingvr.services.GameService;
import com.app.mobileboxingvr.services.UserService;
import com.app.mobileboxingvr.ui.login.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class GameProfileFragment extends Fragment {

    private static final String TAG = "GameProfileFragment";

    private UserService user;
    private GameService game;

    private TextView username, playerStatus;
    private Button btnLogout, btnStart, btnStop;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_gameprofile, container, false);

        initializeView(v);

        username.setText(user.getCurrentUser().getDisplayName());

        displayPlayerStatus();
        onLogoutClick();
        onStartJobClick();
        onStopJobClick();

        return v;
    }

    private void initializeView(View v) {
        username = v.findViewById(R.id.tvUsername);
        playerStatus = v.findViewById(R.id.tvPlayerStatus);
        btnLogout = v.findViewById(R.id.btnLogout);
        btnStart = v.findViewById(R.id.btnStart);
        btnStop = v.findViewById(R.id.btnStop);

        user = UserService.getInstance();
        game = GameService.getInstance();
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
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.logout();
                BackgroundService.getInstance(getActivity()).stopService();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
    }

    public void onStartJobClick() {
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundService.getInstance(getActivity()).startService();
            }
        });
    }

    public void onStopJobClick() {
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackgroundService.getInstance(getActivity()).stopService();
            }
        });
    }
}
