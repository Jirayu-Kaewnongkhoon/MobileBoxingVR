package com.app.mobileboxingvr.ui.gameprofile;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.background.BackgroundTask;
import com.app.mobileboxingvr.models.GameProfile;
import com.app.mobileboxingvr.helpers.GameManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class GameProfileFragment extends Fragment {

    private static final String TAG = "GameProfileFragment";

    private final int REQUEST_CODE = 111;

    private GameManager game;

    private TextView tvStrengthLevel, tvStaminaLevel, tvAgilityLevel, tvTimestamp;
    private TextView tvHealth, tvDamage, tvDefense;
    private ProgressBar loading, strengthExpBar, staminaExpBar, agilityExpBar;
    private ConstraintLayout profile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_gameprofile, container, false);

        initializeView(v);

        permissionCheck();

        displayPlayerStatus();

        return v;
    }

    /**
     *  --initializeView--
     *  Setup view and instantiate object
     */

    private void initializeView(View v) {
        tvStrengthLevel = v.findViewById(R.id.tvStrengthLevel);
        tvStaminaLevel = v.findViewById(R.id.tvStaminaLevel);
        tvAgilityLevel = v.findViewById(R.id.tvAgilityLevel);

        tvHealth = v.findViewById(R.id.tvHealth);
        tvDamage = v.findViewById(R.id.tvDamage);
        tvDefense = v.findViewById(R.id.tvDefense);

        tvTimestamp = v.findViewById(R.id.tvTimestamp);

        loading = v.findViewById(R.id.loading);
        strengthExpBar = v.findViewById(R.id.strengthExpBar);
        staminaExpBar = v.findViewById(R.id.staminaExpBar);
        agilityExpBar = v.findViewById(R.id.agilityExpBar);

        profile = v.findViewById(R.id.layoutGameProfile);

        game = new GameManager();
    }

    /**
     *  --displayPlayerStatus--
     *  Get game profile from database
     *  and setup to screen
     */

    private void displayPlayerStatus() {
        profile.setVisibility(View.INVISIBLE);

        game.getGameProfile().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GameProfile gameProfile = snapshot.getValue(GameProfile.class);

                if (gameProfile != null) {

                    setupGameProfile(gameProfile);
                    
                    Log.d(TAG, "onDataChange: " + gameProfile.toString());
                }

                profile.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "onCancelled: " + error.getMessage());
            }
        });
    }

    /**
     *  --setupGameProfile--
     *  Setup game profile to screen
     */

    private void setupGameProfile(GameProfile gameProfile) {
        tvStrengthLevel.setText(String.valueOf(gameProfile.getStrengthLevel()));
        strengthExpBar.setProgress(gameProfile.getStrengthExp());

        tvStaminaLevel.setText(String.valueOf(gameProfile.getStaminaLevel()));
        staminaExpBar.setProgress(gameProfile.getStaminaExp());

        tvAgilityLevel.setText(String.valueOf(gameProfile.getAgilityLevel()));
        agilityExpBar.setProgress(gameProfile.getAgilityExp());

        tvHealth.setText(String.valueOf(gameProfile.getHealth()));
        tvDamage.setText(String.valueOf(gameProfile.getDamage()));
        tvDefense.setText(String.valueOf(gameProfile.getDefense()));

        tvTimestamp.setText("Last Update : " + getDateFormat(gameProfile.getTimestamp()));
    }

    private String getDateFormat(long timestamp) {
        Date date = new Date(timestamp);

        SimpleDateFormat format = new SimpleDateFormat("dd.MM 'at' HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Asia/Bangkok"));

        return format.format(date);
    }

    /**
     *  --permissionCheck--
     *  Check if is first time permission request
     *  then show request dialog
     */

    private void permissionCheck() {
        SharedPreferences pref = getActivity().getSharedPreferences("PermissionCheck", Context.MODE_PRIVATE);
        boolean isFirstTime = pref.getBoolean("isFirstTime", true);

        if (isFirstTime) {
            pref.edit().putBoolean("isFirstTime", false).apply();

            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_permission);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Button btnAccept = dialog.findViewById(R.id.btnAccept);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        requestPermissions(
                                new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE);
                    }
                });

            }
        }
    }

    /**
     *  --onRequestPermissionsResult--
     *  Check if permission granted then start background task
     *  Otherwise, show info dialog
     */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_tracking);
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        BackgroundTask.getInstance(getActivity()).startBackgroundTask();
                    }
                });
                dialog.show();

                Button btnTrackingClose = dialog.findViewById(R.id.btnTrackingClose);
                btnTrackingClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Toast.makeText(getContext(), "Permission granted!", Toast.LENGTH_LONG).show();

            } else {

                Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_service_info);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Button btnServiceInfoClose = dialog.findViewById(R.id.btnServiceInfoClose);
                btnServiceInfoClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                Toast.makeText(getContext(), "Permission denied!", Toast.LENGTH_LONG).show();

            }

        }
    }
}
