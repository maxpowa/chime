package com.maxpowa.chime.listeners;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.maxpowa.chime.Chime;
import com.maxpowa.chime.util.Notification;
import com.maxpowa.chime.util.RequestList;
import com.maxpowa.chime.util.Notification.NotificationType;
import com.maxpowa.chime.util.User;
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
		Utils.log.info("Got a new friend request from "+data.getValue()+" ("+data.getName()+")");
		Notification notify = new Notification("Friend Request",data.getValue()+" wants to be friends with you.", 0, NotificationType.FRIENDREQUEST);
		Chime.notificationOverlay.queueTemporaryNotification(notify);
		RequestList.requests.put(data.getName(), data.getValue()+"");
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
		Utils.log.info(arg0.getValue()+"("+arg0.getName()+") has revoked their friend request");
		RequestList.requests.remove(arg0.getName());
	}
	
	

}
