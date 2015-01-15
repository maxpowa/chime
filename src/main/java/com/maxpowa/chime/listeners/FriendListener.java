package com.maxpowa.chime.listeners;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.maxpowa.chime.data.User;
import com.maxpowa.chime.data.UserList;
import com.maxpowa.chime.gui.GuiFriendRequests;
import com.maxpowa.chime.gui.GuiFriendsList;
import com.maxpowa.chime.util.Utils;

public class FriendListener implements ValueEventListener {

    private String refid = "";

    public FriendListener(String key) {
        this.refid = key;
    }

    @Override
    public void onCancelled(FirebaseError error) {
        Utils.log.error("FriendListener targeting " + (UserList.users.get(refid) != null ? UserList.users.get(refid).getUsername() : refid) + " has errored! (" + error.getMessage() + ")");
    }

    @Override
    public void onDataChange(DataSnapshot data) {
        UserList.users.put(refid, data.getValue(User.class));
        // Utils.log.info("Updating "+data.getRef().getPath()+" => "+data.getValue().toString());
        if (Minecraft.getMinecraft() != null) {
            GuiScreen currentScreen;
            if ((currentScreen = Minecraft.getMinecraft().currentScreen) != null && (currentScreen instanceof GuiFriendsList || currentScreen instanceof GuiFriendRequests)) {
                Minecraft.getMinecraft().currentScreen.updateScreen();
            }
        }
    }

}
