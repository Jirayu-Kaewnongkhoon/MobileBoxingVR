package com.app.mobileboxingvr.ui.other;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.background.BackgroundTask;
import com.app.mobileboxingvr.helpers.UserManager;
import com.app.mobileboxingvr.ui.login.LoginActivity;

public class OtherFragment extends Fragment implements View.OnClickListener {

    private ConstraintLayout menuSetting, menuAbout, menuLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_other, container, false);

        initializeView(v);
        setupOnClick();

        return v;
    }

    private void initializeView(View view) {
        menuSetting = view.findViewById(R.id.menuSetting);
        menuAbout = view.findViewById(R.id.menuAbout);
        menuLogout = view.findViewById(R.id.menuLogout);
    }

    private void setupOnClick() {
        menuSetting.setOnClickListener(this);
        menuAbout.setOnClickListener(this);
        menuLogout.setOnClickListener(this);
    }

    private void onSettingClick() {
        startActivity(new Intent(getActivity(), SettingActivity.class));
    }

    private void onAboutClick() {
        startActivity(new Intent(getActivity(), AboutActivity.class));
    }

    private void onLogoutClick() {
        UserManager.getInstance().logout();
        BackgroundTask.getInstance(getActivity()).stopBackgroundTask();
        startActivity(new Intent(getActivity(), LoginActivity.class));
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
