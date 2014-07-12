package com.maxpowa.chime.listeners;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.maxpowa.chime.util.Utils;

public class FriendRequestListener implements ChildEventListener {
	
	private String refid = "";

	public FriendRequestListener(String key) {
		this.refid = key;
	}

	@Override
	public void onCancelled(FirebaseError error) {
		Utils.log.error("FriendRequestListener has errored! ("+ error.getMessage() +")");
	}

	@Override
	public void onChildAdded(DataSnapshot data, String key) {
		Utils.log.info("Got a new friend request from "+data.getName()+" \""+data.getValue()+"\"");
	}

	@Override
	public void onChildChanged(DataSnapshot data, String key) {
		Utils.log.info(data.getName()+" modified their friend request => \""+data.getValue()+"\"");
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
