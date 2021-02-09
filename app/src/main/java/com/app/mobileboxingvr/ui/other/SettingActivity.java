package com.app.mobileboxingvr.ui.other;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.background.BackgroundTask;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.setting_container, new MySettingsFragment()).commit();

        setupActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar() {
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public static class MySettingsFragment extends PreferenceFragmentCompat {

        private final int REQUEST_CODE = 111;

        SharedPreferences.OnSharedPreferenceChangeListener listener =
                new SharedPreferences.OnSharedPreferenceChangeListener() {
                    @Override
                    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

                        if (key.equals("isServiceEnabled")) {

                            boolean isServiceEnabled = sharedPreferences.getBoolean(key, false);
                            Log.d("SETTING", "onSharedPreferenceChanged : " + isServiceEnabled);

                            if (isServiceEnabled) {
                                permissionCheck();
                            } else {
                                BackgroundTask.getInstance(getContext()).stopBackgroundTask();
                            }

                        }

                    }
                };

        private void permissionCheck() {
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

            } else {

                BackgroundTask.getInstance(getActivity()).startBackgroundTask();

            }
        }

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

                    Button btnClose = dialog.findViewById(R.id.btnClose);
                    btnClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });

                    Toast.makeText(getContext(), "Permission granted!", Toast.LENGTH_LONG).show();

                } else {

                    SwitchPreferenceCompat enableService = (SwitchPreferenceCompat) findPreference("isServiceEnabled");
                    if (enableService != null) {
                        enableService.setChecked(false);
                    }

                    boolean isServiceEnabled = getPreferenceManager().getSharedPreferences().getBoolean("isServiceEnabled", false);
                    Log.d("SETTING", "onRequestPermissionsResult: " + isServiceEnabled);

                    Toast.makeText(getContext(), "Permission denied!", Toast.LENGTH_LONG).show();

                }

            }
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.setting, rootKey);
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(listener);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(listener);
        }
    }
}