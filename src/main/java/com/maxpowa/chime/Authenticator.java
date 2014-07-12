package com.maxpowa.chime;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class Authenticator implements Firebase.AuthListener {
	
    @Override
    public void onAuthError(FirebaseError error) {
        Utils.log.error("Authentication Failed! " + error.getMessage());
    }

    @Override
    public void onAuthSuccess(Object authData) {
        Utils.log.info("Authentication Succeeded!");
    }

    @Override
    public void onAuthRevoked(FirebaseError error) {
        Utils.log.error("Authentication status was cancelled! " + error.getMessage());
    }
}
