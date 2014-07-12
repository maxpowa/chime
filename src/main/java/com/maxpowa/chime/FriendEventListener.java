package com.maxpowa.chime;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;

public class FriendEventListener implements ChildEventListener {
	
	private User user;
	private String refid = "";

	public FriendEventListener(String key) {
		this.refid = key;
	}

	@Override
	public void onCancelled(FirebaseError error) {
		Utils.log.error("FriendListener targeting "+ (user != null ? user.getUsername() : refid) +" has errored! ("+ error.getMessage() +")");
	}

	@Override
	public void onChildAdded(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChildChanged(DataSnapshot data, String key) {
		if (data.getName().equalsIgnoreCase("seen")) {
			Utils.log.info("Invoking notification: "+data.getName()+":"+data.getValue());
		} else if (data.getName().equalsIgnoreCase("username")) {
			Utils.log.info("Invoking notification: "+data.getName()+":"+data.getValue());
		} else if (data.getName().equalsIgnoreCase("lastSeen")) {
			// don't spam the log!
		} else {
			Utils.log.info("Caught change of \""+data.getName()+"\" but ignored.");
		}
	}

	@Override
	public void onChildMoved(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChildRemoved(DataSnapshot arg0) {
		// TODO Auto-generated method stub
		
	}

}
