package com.maxpowa.chime.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RequestList
{
	public static HashMap<String, User> requests = new HashMap<String, User>();
    private ArrayList<User> requestsArray = new ArrayList<User>();

    public RequestList()
    {
        this.loadUserList();
    }

    public void loadUserList()
    {
    	requestsArray.clear();
    	for (Entry<String, User> e : requests.entrySet()) {
    		requestsArray.add(e.getValue());
    	}
    }

    /**
     * Gets the ServerData instance stored for the given index in the list.
     */
    public User getUserData(int index)
    {
        return this.requestsArray.get(index);
    }

    /**
     * Removes the ServerData instance stored for the given index in the list.
     */
    public void removeUserData(int index)
    {
        this.requestsArray.remove(index);
        //TODO: Remove from hashmap too
    }

    /**
     * Adds the given ServerData instance to the list.
     */
    public void addUserData(String username)
    {
    	//TODO: UUID searching
    }

    /**
     * Counts the number of ServerData instances in the list.
     */
    public int countUsers()
    {
        return this.requestsArray.size();
    }

    /**
     * Takes two list indexes, and swaps their order around.
     */
    public void swapUsers(int index1, int index2)
    {
        User userdata = this.getUserData(index1);
        this.requestsArray.set(index1, this.getUserData(index2));
        this.requestsArray.set(index2, userdata);
    }

    public void setUser(int index, User user)
    {
        this.requestsArray.set(index, user);
    }

    public static void addUserAndShow(User user)
    {
        RequestList instance = new RequestList();
        instance.loadUserList();

        for (int i = 0; i < instance.countUsers(); ++i)
        {
            User userdata = instance.getUserData(i);

            if (userdata.getUUID().equals(user.getUUID()) && userdata.getUsername().equals(user.getUsername()))
            {
                instance.setUser(i, user);
                break;
            }
        }
    }
}