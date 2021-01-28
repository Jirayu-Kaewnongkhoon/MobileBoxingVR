package com.app.mobileboxingvr.helpers;

import com.app.mobileboxingvr.models.GameProfile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameManager {

    public static GameManager instance;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private UserManager user;
    private String userID;

    private GameManager() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("game_profile");

        user = UserManager.getInstance();
        userID = user.getCurrentUser().getUid();
    }

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    /**
     *  --updateGameProfile--
     *  Save game profile to database
     */

    public void updateGameProfile(GameProfile gameProfile) {
        getGameProfile().setValue(gameProfile);
    }

    /**
     *  --getGameProfile--
     *  Get game profile from database
     */

    public DatabaseReference getGameProfile() {
        return myRef.child(userID);
    }
}
