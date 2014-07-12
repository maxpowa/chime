package com.maxpowa.chime.listeners;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.maxpowa.chime.Chime;
import com.maxpowa.chime.util.Notification;
import com.maxpowa.chime.util.Utils;
import com.maxpowa.chime.util.Notification.NotificationType;

public class FriendEventListener implements ChildEventListener {
	
	private String refid = "";

	public FriendEventListener(String key) {
		this.refid = key;
	}

	@Override
	public void onCancelled(FirebaseError error) {
		Utils.log.error("FriendListener targeting "+ (Utils.users.get(refid) != null ? Utils.users.get(refid).getUsername() : refid) +" has errored! ("+ error.getMessage() +")");
	}

	@Override
	public void onChildAdded(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChildChanged(DataSnapshot data, String key) {
		if (data.getName().equalsIgnoreCase("seen")) {
			Utils.log.info("Invoking notification: "+data.getName()+":"+data.getValue());
			Notification notify = new Notification(Utils.users.get(refid).getUsername(), "Is now playing "+data.getValue(), 0, NotificationType.STATUS);
			Chime.notificationOverlay.queueTemporaryNotification(notify);
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
