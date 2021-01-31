package com.app.mobileboxingvr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.app.mobileboxingvr.ui.activityhistory.ActivityHistoryFragment;
import com.app.mobileboxingvr.ui.gameprofile.GameProfileFragment;
import com.app.mobileboxingvr.ui.other.OtherFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeView();
        setupNav();

    }

    @Override
    protected void onStart() {
        super.onStart();

//        if (user.getCurrentUser() == null) {
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//        }
//
//        if (StepCounter.getInstance(this).getStepSensor() == null) {
//
//            // app cannot do background task
//            btnStart.setClickable(false);
//            btnStop.setClickable(false);
//
//            Log.d("MAIN", "onStart: Not Clickable");
//
//        }

    }

    private void initializeView() {
        nav = findViewById(R.id.bottom_navigation);
    }

    private void setupNav() {
        nav.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new GameProfileFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selected = null;

                    switch (item.getItemId()) {
                        case R.id.nav_home:
                            selected = new GameProfileFragment();
                            break;
                        case R.id.nav_history:
                            selected = new ActivityHistoryFragment();
                            break;
                        case R.id.nav_other:
                            selected = new OtherFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selected).commit();

                    return true;
                }
            };

}