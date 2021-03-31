package com.app.mobileboxingvr.ui.other;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.background.BackgroundTask;
import com.app.mobileboxingvr.helpers.GameManager;
import com.app.mobileboxingvr.helpers.UserManager;
import com.app.mobileboxingvr.models.GameProfile;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class OtherFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "OtherFragment";

    private ConstraintLayout menuSetting, menuAbout, menuLogout;
    private TextView tvPlayerName, tvPlayerLevel;
    private ProgressBar playerExpBar;

    private GameManager game;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_other, container, false);

        initializeView(v);
        setupOnClick();

        displayPlayerInfo();

        return v;
    }

    /**
     *  --initializeView--
     *  Setup view and instantiate object
     */

    private void initializeView(View view) {
        menuSetting = view.findViewById(R.id.menuSetting);
        menuAbout = view.findViewById(R.id.menuAbout);
        menuLogout = view.findViewById(R.id.menuLogout);

        tvPlayerName = view.findViewById(R.id.tvPlayerName);
        tvPlayerLevel = view.findViewById(R.id.tvPlayerLevel);

        playerExpBar = view.findViewById(R.id.playerExpBar);

        game = new GameManager();
    }

    /**
     *  --setupOnClick--
     *  Setup onClick listener
     */

    private void setupOnClick() {
        menuSetting.setOnClickListener(this);
        menuAbout.setOnClickListener(this);
        menuLogout.setOnClickListener(this);
    }

    /**
     *  --displayPlayerInfo--
     *  Get game profile from database
     *  and display to screen
     */

    private void displayPlayerInfo() {
        game.getGameProfile().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GameProfile gameProfile = snapshot.getValue(GameProfile.class);

                if (gameProfile != null) {
                    tvPlayerName.setText(gameProfile.getPlayerName());
                    tvPlayerLevel.setText("Level " + gameProfile.getPlayerLevel());
                    playerExpBar.setProgress(gameProfile.getPlayerExp());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }

    /**
     *  --onSettingClick--
     *  Go to SettingActivity
     */

    private void onSettingClick() {
        startActivity(new Intent(getActivity(), SettingActivity.class));
    }

    /**
     *  --onAboutClick--
     *  Go to AboutActivity
     */

    private void onAboutClick() {
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }

    /**
     *  --onLogoutClick--
     *  Logout user and stop background task
     *  then go to LoginActivity
     */

    private void onLogoutClick() {
        UserManager.getInstance().logout();
        BackgroundTask.getInstance(getActivity()).stopBackgroundTask();
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuSetting:
                onSettingClick();
                break;
            case R.id.menuAbout:
                onAboutClick();
                break;
            case R.id.menuLogout:
                onLogoutClick();
                break;
        }
    }
}
