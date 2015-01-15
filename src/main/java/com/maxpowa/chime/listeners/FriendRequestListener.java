package com.maxpowa.chime.listeners;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.maxpowa.chime.Chime;
import com.maxpowa.chime.data.RequestList;
import com.maxpowa.chime.data.User;
import com.maxpowa.chime.util.Notification;
import com.maxpowa.chime.util.Notification.NotificationType;
import com.maxpowa.chime.util.Utils;

public class FriendRequestListener implements ChildEventListener {

    private String refid = "";

    public FriendRequestListener(String key) {
        this.refid = key;
    }

    @Override
    public void onCancelled(FirebaseError error) {
        Utils.log.error("FriendRequestListener (" + refid + ") has errored! [" + error.getMessage() + "]");
    }

    @Override
    public void onChildAdded(DataSnapshot data, String key) {
        Utils.log.info("Got a new friend request from " + data.getValue() + " (" + data.getName() + ")");
        Chime.users.child(data.getName()).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onCancelled(FirebaseError error) {
                Utils.log.error("Fetching friend request info errored (" + error.getMessage() + ")");
            }

            @Override
            public void onDataChange(DataSnapshot data) {
                User tmp = data.getValue(User.class);
                Notification notify = new Notification("Friend Request from", tmp.getUsername(), 12, NotificationType.FRIENDREQUEST);
                Chime.notificationOverlay.queueTemporaryNotification(notify);
                Utils.log.info("Got info for " + tmp.getUsername() + " (" + tmp.getUUID() + ")");
                RequestList.requests.put(data.getName(), tmp);
            }

        });
    }

    @Override
    public void onChildChanged(DataSnapshot data, String key) {
        Utils.log.info(data.getName() + " modified their friend request => \"" + data.getValue() + "\"");
    }

    @Override
    public void onChildMoved(DataSnapshot arg0, String arg1) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onChildRemoved(DataSnapshot arg0) {
        Utils.log.info("The friend request from " + arg0.getValue() + "(" + arg0.getName() + ") has been removed, this is normal if you have added them as a friend");
        if (RequestList.requests.containsKey(arg0.getName()))
            RequestList.requests.remove(arg0.getName());
    }

}
