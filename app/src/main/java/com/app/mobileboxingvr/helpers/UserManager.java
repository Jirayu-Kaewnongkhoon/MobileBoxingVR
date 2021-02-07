package com.app.mobileboxingvr.helpers;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserManager {

    private static UserManager instance;

    private FirebaseAuth auth;

    private UserManager() {
        initializeFirebase();
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    private void initializeFirebase() {
        auth = FirebaseAuth.getInstance();
    }

    /**
     *  --getCurrentUser--
     *  Get current login user from Firebase Authentication
     */

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    /**
     *  --register--
     *  Register user with Firebase Authentication
     */

    public Task<AuthResult> register(String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    /**
     *  --login--
     *  Login user with Firebase Authentication
     */

    public Task<AuthResult> login(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    /**
     *  --logout--
     *  Logout user with Firebase Authentication
     */

    public void logout() {
        auth.signOut();
    }

}
