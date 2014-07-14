package com.maxpowa.chime.util;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.maxpowa.chime.Chime;

public class Authenticator implements Firebase.AuthListener {
	
	private Chime chime = null;
	
    public Authenticator(Chime chime) {
		this.chime = chime;
	}

	@Override
    public void onAuthError(FirebaseError error) {
        Utils.log.error("Authentication Failed! " + error.getMessage());
        chime.deauth();
    }

    @Override
    public void onAuthSuccess(Object authData) {
        Utils.log.info("Authentication Succeeded!");
    }

    @Override
    public void onAuthRevoked(FirebaseError error) {
        Utils.log.error("Authentication status was cancelled! " + error.getMessage());
        chime.deauth();
    }
}
