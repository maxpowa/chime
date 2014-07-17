package com.maxpowa.chime.listeners;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.maxpowa.chime.util.UserList;
import com.maxpowa.chime.util.Utils;

public class FriendsListListener implements ChildEventListener {

	@Override
	public void onCancelled(FirebaseError arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChildAdded(DataSnapshot data, String arg1) {
		ListenerRegistry.addFriend(data.getName());
	}

	@Override
	public void onChildChanged(DataSnapshot data, String arg1) {
		ListenerRegistry.addFriend(data.getName());
	}

	@Override
	public void onChildMoved(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChildRemoved(DataSnapshot data) {
		ListenerRegistry.removeFriend(data.getName());
	}

}
