package com.maxpowa.chime;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Initializer implements ValueEventListener {

	@Override
	public void onCancelled(FirebaseError err) {
		Utils.log.error("Initializer errored ("+err.getMessage()+")");
	}

	@Override
	public void onDataChange(DataSnapshot data) {
		Chime.myUser = (User) data.getValue(User.class);
		Utils.log.info("Initializer retrieved data for "+data.getRef().getPath());
		if (Chime.myUser == null) {
			Utils.log.info("Retrieved data was null, creating new user at "+Chime.me.getPath());
			Chime.myUser = new User();
			Chime.myUser.setUsername(Chime.getSession().getPlayerID());
			Chime.me.setValue(Chime.myUser);
		}
		Chime.me.push();
		
		if (Chime.myUser.getFriends() != null) {
			for (String key : Chime.myUser.getFriends().keySet()) {
				Chime.users.child(key).addValueEventListener(new FriendListener(key));
				Chime.users.child(key).addChildEventListener(new FriendEventListener(key));
			}
		}
	}

}
