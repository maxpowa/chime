package com.maxpowa.chime.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class RequestList
{
	public static HashMap<String, String> requests = new HashMap<String, String>();
    private ArrayList<Entry<String, String>> requestsArray = new ArrayList<Entry<String, String>>();

    public RequestList()
    {
        this.loadUserList();
    }

    public void loadUserList()
    {
    	for (Entry<String, String> e : requests.entrySet()) {
    		requestsArray.add(e);
    	}
    }

    /**
     * Gets the ServerData instance stored for the given index in the list.
     */
    public Entry<String, String> getUserData(int index)
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
        Entry<String,String> userdata = this.getUserData(index1);
        this.requestsArray.set(index1, this.getUserData(index2));
        this.requestsArray.set(index2, userdata);
    }

    public void setUser(int index, Entry<String,String> user)
    {
        this.requestsArray.set(index, user);
    }

    public static void addUserAndShow(Entry<String,String> user)
    {
        RequestList instance = new RequestList();
        instance.loadUserList();

        for (int i = 0; i < instance.countUsers(); ++i)
        {
            Entry<String, String> userdata = instance.getUserData(i);

            if (userdata.getValue().equals(user.getValue()) && userdata.getKey().equals(user.getKey()))
            {
                instance.setUser(i, user);
                break;
            }
        }
    }
}