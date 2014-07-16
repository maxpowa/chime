package com.maxpowa.chime.listeners;

import net.minecraft.util.EnumChatFormatting;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.maxpowa.chime.Chime;
import com.maxpowa.chime.util.Notification;
import com.maxpowa.chime.util.Notification.NotificationType;
import com.maxpowa.chime.util.UserList;
import com.maxpowa.chime.util.Utils;

public class FriendEventListener implements ChildEventListener {
	
	private String refid = "";

	public FriendEventListener(String key) {
		this.refid = key;
	}

	@Override
	public void onCancelled(FirebaseError error) {
		Utils.log.error("FriendListener targeting "+ (UserList.users.get(refid) != null ? UserList.users.get(refid).getUsername() : refid) +" has errored! ("+ error.getMessage() +")");
	}

	@Override
	public void onChildAdded(DataSnapshot arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onChildChanged(DataSnapshot data, String key) {
		if (data.getName().equalsIgnoreCase("seen")) {
			Notification notify = new Notification("",EnumChatFormatting.YELLOW+UserList.users.get(refid).getUsername()+" is now "+data.getValue(), 0, NotificationType.STATUS);
			Chime.notificationOverlay.queueTemporaryNotification(notify, true);
		} else if (data.getName().equalsIgnoreCase("username")) {
			Notification notify = new Notification("",EnumChatFormatting.YELLOW+UserList.users.get(refid).getUsername()+" is now known as "+data.getValue(), 0, NotificationType.STATUS);
			Chime.notificationOverlay.queueTemporaryNotification(notify, true);
		} else if (data.getName().equalsIgnoreCase("lastSeen")) {
			if (UserList.users.get(refid).lastSeen()+30000L < data.getValue(Long.class)) {
				Notification notify = new Notification(EnumChatFormatting.YELLOW+UserList.users.get(refid).getUsername(),"Is online", 0, NotificationType.ONLINE);
				Chime.notificationOverlay.queueTemporaryNotification(notify);
			}
		} 
		//else {
			//Utils.log.info("Caught change of \""+data.getName()+"\" but ignored.");
		//}
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
