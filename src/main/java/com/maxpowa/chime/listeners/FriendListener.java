package com.maxpowa.chime.listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.maxpowa.chime.util.User;
import com.maxpowa.chime.util.Utils;

public class FriendListener implements ValueEventListener {
	
	private String refid = "";

	public FriendListener(String key) {
		this.refid = key;
	}

	@Override
	public void onCancelled(FirebaseError error) {
		Utils.log.error("FriendListener targeting "+ (Utils.users.get(refid) != null ? Utils.users.get(refid).getUsername() : refid) +" has errored! ("+ error.getMessage() +")");
	}

	@Override
	public void onDataChange(DataSnapshot data) {
		Utils.users.put(refid, data.getValue(User.class));
		Utils.log.info("Updating "+data.getRef().getPath()+" => "+data.getValue().toString());
	}

}
