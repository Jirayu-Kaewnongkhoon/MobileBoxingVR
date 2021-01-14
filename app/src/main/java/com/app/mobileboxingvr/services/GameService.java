package com.app.mobileboxingvr.services;

import com.app.mobileboxingvr.models.GameProfile;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GameService {

    public static GameService instance;

    private FirebaseDatabase database;
    private DatabaseReference myRef;

    private UserService user;
    private String userID;

    private GameService() {
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("game_profile");

        user = UserService.getInstance();
        userID = user.getCurrentUser().getUid();
    }

    public static GameService getInstance() {
        if (instance == null) {
            instance = new GameService();
        }
        return instance;
    }

    public void updateGameProfile(GameProfile gameProfile) {
        getGameProfile().setValue(gameProfile);
    }

    public DatabaseReference getGameProfile() {
        return myRef.child(userID);
    }
}
