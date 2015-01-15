package com.maxpowa.chime.data;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class UserList {
    /** List of User instances. */
    public static HashMap<String, User> users = new HashMap<String, User>();
    private ArrayList<User> usersArray = new ArrayList<User>();

    public UserList() {
        this.loadUserList();
    }

    public void loadUserList() {
        usersArray.clear();
        // Basic sorting, offline at the bottom and online up at the top of the
        // list
        ArrayDeque<User> temp = new ArrayDeque<User>();
        for (Entry<String, User> e : users.entrySet()) {
            if (e.getValue().isOnline())
                temp.addFirst(e.getValue());
            else
                temp.addLast(e.getValue());
        }
        usersArray = new ArrayList<User>(temp);
    }

    /**
     * Gets the ServerData instance stored for the given index in the list.
     */
    public User getUserData(int index) {
        return this.usersArray.get(index);
    }

    /**
     * Removes the ServerData instance stored for the given index in the list.
     */
    public void removeUserData(int index) {
        this.usersArray.remove(index);
    }

    /**
     * Counts the number of ServerData instances in the list.
     */
    public int countUsers() {
        return this.usersArray.size();
    }

    /**
     * Takes two list indexes, and swaps their order around.
     */
    public void swapUsers(int index1, int index2) {
        User userdata = this.getUserData(index1);
        this.usersArray.set(index1, this.getUserData(index2));
        this.usersArray.set(index2, userdata);
    }

    public void setUser(int index, User user) {
        this.usersArray.set(index, user);
    }

    public static void addUserAndShow(User user) {
        UserList instance = new UserList();
        instance.loadUserList();

        for (int i = 0; i < instance.countUsers(); ++i) {
            User userdata = instance.getUserData(i);

            if (userdata.getUsername().equals(user.getUsername()) && userdata.getUUID().equals(user.getUUID())) {
                instance.setUser(i, user);
                break;
            }
        }
    }
}