package com.maxpowa.chime;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class FriendListener implements ValueEventListener {
	
	private User user;
	private String refid = "";

	public FriendListener(String key) {
		this.refid = key;
	}

	@Override
	public void onCancelled(FirebaseError error) {
		Utils.log.error("FriendListener targeting "+ (user != null ? user.getUsername() : refid) +" has errored! ("+ error.getMessage() +")");
	}

	@Override
	public void onDataChange(DataSnapshot data) {
		user = data.getValue(User.class);
		Utils.log.info("Updating "+data.getRef().getPath()+" => "+data.getValue().toString());
	}

}
