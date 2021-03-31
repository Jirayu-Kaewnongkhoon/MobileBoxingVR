package com.app.mobileboxingvr.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.app.mobileboxingvr.R;
import com.app.mobileboxingvr.ui.activityhistory.ActivityHistoryFragment;
import com.app.mobileboxingvr.ui.gameprofile.GameProfileFragment;
import com.app.mobileboxingvr.ui.login.LoginActivity;
import com.app.mobileboxingvr.ui.other.OtherFragment;
import com.app.mobileboxingvr.ui.useractivity.UserActivityFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {

    private BottomNavigationView nav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeView();
        setupNav();

    }

    /**
     *  --initializeView--
     *  Setup view and instantiate object
     */

    private void initializeView() {
        nav = findViewById(R.id.bottom_navigation);
    }

    /**
     *  --setupNav--
     *  Setup navigation bar
     */

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
                        case R.id.nav_activity:
                            selected = new UserActivityFragment();
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

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

        if (firebaseAuth.getCurrentUser() == null) {

            startActivity(new Intent(this, LoginActivity.class));
            finish();

        }
    }
}