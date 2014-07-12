package com.maxpowa.chime.listeners;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.maxpowa.chime.Chime;
import com.maxpowa.chime.util.User;
import com.maxpowa.chime.util.Utils;

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
		
		Chime.public_requests.child(data.getRef().getPath()+"/requests").addChildEventListener(
				new FriendRequestListener(Chime.myProfile.getId().toString()));
	}

}
