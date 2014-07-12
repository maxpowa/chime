package com.maxpowa.chime.util;

import java.util.HashMap;

public class User {

	private long lastSeen = System.currentTimeMillis();
	private String username = "Steve";
	private String motd = "I'm new here!";
	private String seen = "Main Menu";
	private HashMap<String, String> blocks = new HashMap<String,String>();
	private HashMap<String, String> friends = new HashMap<String,String>();

	public String getMotd() {
		return motd;
	}
	public void setMotd(String motd) {
		this.motd = motd;
	}
	public String getSeen() {
		return seen;
	}
	public void setSeen(String seen) {
		this.seen = seen;
	}
	public HashMap<String, String> getBlocks() {
		return blocks;
	}
	public void setBlocks(HashMap<String, String> blocks) {
		this.blocks = blocks;
	}
	public HashMap<String, String> getFriends() {
		return friends;
	}
	public void setFriends(HashMap<String, String> friends) {
		this.friends = friends;
	}
	public long getLastSeen() {
		return System.currentTimeMillis();
	}
	public long lastSeen() {
		return this.lastSeen;
	}
	public void setLastSeen(long lastSeen) {
		this.lastSeen = lastSeen;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}