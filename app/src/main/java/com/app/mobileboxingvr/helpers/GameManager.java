package com.app.mobileboxingvr.helpers;

import com.app.mobileboxingvr.models.GameProfile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameManager {

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private UserManager user;
    private String userID;

    public GameManager() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("game_profile");

        user = UserManager.getInstance();
        userID = user.getCurrentUser().getUid();
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
