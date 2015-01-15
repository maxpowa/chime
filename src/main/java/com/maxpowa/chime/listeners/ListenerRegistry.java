package com.maxpowa.chime.listeners;

import java.util.HashMap;

import com.maxpowa.chime.Chime;
import com.maxpowa.chime.data.UserList;

public class ListenerRegistry {

    private static HashMap<String, FriendHolder> registry = new HashMap<String, FriendHolder>();

    /**
     * Starts all listeners for the provided uuid and populates the user data
     * 
     * @param uuid
     */
    public static void addFriend(String uuid) {
        if (registry.containsKey(uuid))
            return;
        registry.put(uuid, new FriendHolder(uuid));
        registry.get(uuid).registerListeners();
    }

    /**
     * Stops any listeners associated with the specified user and removes their
     * data.
     * 
     * @param uuid
     */
    public static void removeFriend(String uuid) {
        if (!registry.containsKey(uuid))
            return;
        registry.get(uuid).unregisterListeners();
        registry.remove(uuid);
        UserList.users.remove(uuid);
    }

    private static class FriendHolder {

        private FriendEventListener eventListener;
        private FriendListener listener;
        private String refid;

        public FriendHolder(String refid) {
            this.refid = refid;
            this.eventListener = new FriendEventListener(refid);
            this.listener = new FriendListener(refid);
        }

        public void registerListeners() {
            Chime.users.child(refid).addChildEventListener(this.eventListener);
            Chime.users.child(refid).addValueEventListener(this.listener);
        }

        public void unregisterListeners() {
            Chime.users.child(refid).removeEventListener(this.eventListener);
            Chime.users.child(refid).removeEventListener(this.listener);
        }

    }

}
